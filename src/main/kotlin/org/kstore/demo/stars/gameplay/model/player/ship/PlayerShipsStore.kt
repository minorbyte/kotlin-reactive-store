package org.kstore.demo.stars.gameplay.model.player.ship

import org.kstore.demo.stars.GameDescription
import org.kstore.demo.stars.common.*
import org.kstore.demo.stars.gameplay.model.player.PlayerId
import org.kstore.demo.stars.gameplay.model.player.colony.BuildColonyAction
import org.kstore.demo.stars.gameplay.model.player.resources.PlayerFuelPaymentAction
import org.kstore.demo.stars.gameplay.model.starsystem.StarSystem
import org.kstore.demo.stars.gameplay.view.active.tile.CursorSetPositionAction
import org.kstore.demo.stars.rule.ship.ShipType.*
import org.kstore.demo.stars.rule.tech.TechId
import react.kstore.*
import react.kstore.action.*
import react.kstore.optional.notNull
import react.kstore.reaction.*
import react.kstore.reaction.Validation.Companion.condition
import react.kstore.reaction.Validation.Companion.fails
import react.kstore.reaction.Validation.Companion.success
import java.lang.Math.*
import java.util.*

class PlayerShipsStore(
        dispatcher: Dispatcher = CommonDispatcher,
        gameDescription: GameDescription,
        playerTechStore: Store<Map<PlayerId, List<TechId>>>,
        private val starSystemStore: Store<StarSystem>
) : BasicStore<Map<Pair<PlayerId, ShipId>, Ship>>(
        dispatcher = dispatcher,
        initialState = gameDescription.players
                .flatMap { player ->
                    player.ships.map {
                        Ship(
                                id = it.id,
                                position = it.position,
                                playerId = player.id,
                                shipType = it.shipType,
                                hp = it.hp
                        )
                    }
                }
                .associateBy({
                    Pair(it.playerId, it.id)
                }, {
                    it
                }),
        reactions = {
            on(ShipMoveAction::class) {
                update { state, action ->
                    notNull(state, state[action.playerId to action.shipId]) { thisShip ->
                        val enemyShip = state.values.firstOrNull { it.position == action.position && it.playerId != action.playerId }

                        dispatcher.asyncAction(PlayerFuelPaymentAction(action.playerId, thisShip.shipType.fuelConsumption))

                        if (enemyShip != null) {
                            dispatcher.asyncAction(BattleStartAction(
                                    thisShip,
                                    enemyShip,
                                    playerTechStore.state.getOrDefault(thisShip.playerId, emptyList()),
                                    playerTechStore.state.getOrDefault(enemyShip.playerId, emptyList())
                            ))
                            dispatcher.asyncAction(CursorSetPositionAction(thisShip.position))
                            dispatcher.asyncAction(ShipMovedAction(action.playerId, action.shipId, thisShip.position))
                            state
                        } else {
                            dispatcher.asyncAction(CursorSetPositionAction(action.position))
                            dispatcher.asyncAction(ShipMovedAction(action.playerId, action.shipId, action.position))
                            state.plus(
                                    action.playerId to action.shipId to state[action.playerId to action.shipId]!!.copy(position = action.position)
                            )
                        }
                    }
                }
                validate { state, action ->
                    val ship = state[action.playerId to action.shipId] ?: return@validate fails("Ship not found")

                    if (abs(ship.position.x - action.position.x) + abs(ship.position.y - action.position.y) > 1) {
                        fails("Ship can't move more than 1 cell")
                    } else success()
                }
            }
            on(FailedActionValidation::class) {
                react { state, action ->
                    val failed = action.failed
                    if (failed is ShipMoveAction) {
                        state[failed.playerId to failed.shipId]!!.let {
                            dispatcher.action(CursorSetPositionAction(it.position))
                        }
                    }
                }
            }
            on(ShipCreateAction::class) {
                update { state, action ->
                    state.plus(action.ship.playerId to action.ship.id to action.ship)
                }
                validate { _, action ->
                    condition(
                            action.ship.playerId == action.playerId,
                            "Can't have different playerIds"
                    )
                }
            }
            on(BuildColonyAction::class) {
                validate { state, action ->
                    condition(
                            state[action.playerId to action.shipId]!!.shipType.canColonize,
                            "Ship can`t colonize"
                    )
                }
            }
            on(CarrierUploadAction::class) {
                update { state, action ->
                    val uploaded = state[action.playerId to action.uploadedShipId]!!
                    val carrier = state[action.playerId to action.carrierShipId]!!

                    state
                            .minus(action.playerId to action.uploadedShipId)
                            .plus(action.playerId to action.carrierShipId to when (uploaded.shipType) {
                                FIGHTER -> carrier.copy(fighters = carrier.fighters + 1)
                                CORVETTE -> carrier.copy(corvettes = carrier.corvettes + 1)
                                else -> carrier
                            })
                }
                validate { state, action ->
                    condition(
                            state.containsKey(action.playerId to action.carrierShipId) && state.containsKey(action.playerId to action.uploadedShipId),
                            "Not existed ships"
                    )
                }
                validate { state, action ->
                    val uploadedShipType = state[action.playerId to action.uploadedShipId]!!.shipType
                    val carrier = state[action.playerId to action.carrierShipId]!!

                    condition(
                            when (uploadedShipType) {
                                CORVETTE -> carrier.corvettes < carrier.shipType.maxCorvettes
                                FIGHTER -> carrier.fighters < carrier.shipType.maxFighters
                                else -> false
                            },
                            "Not enough space"
                    )
                }
            }
            on(CarrierDeployAction::class) {
                update { state, action ->
                    val carrier = state[action.playerId to action.carrierShipId]!!

                    dispatcher.asyncAction(ShipCreateAction(Ship(
                            playerId = carrier.playerId,
                            position = carrier.position,
                            id = UUID.randomUUID().toString(),
                            shipType = action.shipType
                    ), carrier.playerId))

                    state
                            .plus(action.playerId to action.carrierShipId to when (action.shipType) {
                                FIGHTER -> carrier.copy(fighters = carrier.fighters - 1)
                                CORVETTE -> carrier.copy(corvettes = carrier.corvettes - 1)
                                else -> carrier
                            })
                }
                validate { state, action ->
                    condition(
                            state.containsKey(action.playerId to action.carrierShipId),
                            "Not existed ships"
                    )
                }
                validate { state, action ->
                    val carrier = state[action.playerId to action.carrierShipId]!!

                    condition(
                            when (action.shipType) {
                                CORVETTE -> carrier.corvettes > 0
                                FIGHTER -> carrier.fighters > 0
                                else -> false
                            },
                            "Not enough ships in hangar"
                    )
                }
            }
            on(ShipRepairAction::class) {
                update { state, action ->
                    state.plus(action.playerId to action.shipId to state[action.playerId to action.shipId]!!.let {
                        it.copy(
                                hp = min(it.hp + action.amount, it.shipType.maxHp)
                        )
                    })
                }
                validate { state, action ->
                    condition(
                            state.containsKey(action.playerId to action.shipId) &&
                                    state[action.playerId to action.shipId]!!.hp < state[action.playerId to action.shipId]!!.shipType.maxHp,
                            "Already has maximum hp"
                    )
                }
            }
        }
) {

    private val random = Random()

    init {
        on(BattleFinishAction::class) update { state, action ->
            val attacker = state[action.attacker.playerId to action.attacker.id]!!.let {
                it.copy(hp = it.hp - action.attackerDamage)
            }

            val defender = state[action.defender.playerId to action.defender.id]!!.let {
                it.copy(hp = it.hp - action.defenderDamage)
            }

            state.saveShipIfAlive(attacker).saveShipIfAlive(defender)
        }
        on(ShipJumpAction::class) {
            update { state, action ->
                notNull(state, state[action.playerId to action.shipId]) { thisShip ->
                    dispatcher.asyncAction(PlayerFuelPaymentAction(action.playerId, thisShip.shipType.fuelConsumption * JUMP_FUEL_MULTIPLIER))

                    val jumpPosition = inCircle(action.position, when (random.nextInt(100)) {
                        in 0..25 -> 1
                        in 25..75 -> 2
                        in 75..100 -> 3
                        else -> 1
                    }).let { it[random.nextInt(it.size)] }

                    val enemyShip = state.values.firstOrNull { it.position == jumpPosition && it.playerId != action.playerId }

                    var newThisShipPosition = jumpPosition

                    val newState = when {
                        enemyShip != null && enemyShip.shipType.canJump -> {
                            val key = action.playerId to action.shipId
                            newThisShipPosition = findNearestFreePosition(thisShip.playerId, jumpPosition, { true })
                            state.plus(
                                    key to state[key]!!.copy(position = newThisShipPosition)
                            )
                        }
                        enemyShip != null && !enemyShip.shipType.canJump -> {
                            val thisShipKey = action.playerId to action.shipId
                            val enemyShipKey = enemyShip.playerId to enemyShip.id

                            state.plus(thisShipKey to state[thisShipKey]!!.copy(position = jumpPosition))
                                    .plus(enemyShipKey to state[enemyShipKey]!!.copy(
                                            position = findNearestFreePosition(enemyShip.playerId, enemyShip.position, { it != jumpPosition })
                                    ))
                        }
                        else -> {
                            val key = action.playerId to action.shipId
                            state.plus(key to state[key]!!.copy(position = jumpPosition))
                        }
                    }

                    dispatcher.asyncAction(ShipMovedAction(action.playerId, action.shipId, newThisShipPosition))
                    newState
                }
            }
            validate { state, action ->
                val ship = state[action.playerId to action.shipId] ?: return@validate fails("Ship not found")

                condition(
                        ship.shipType.canJump,
                        "Can't jump"
                )
            }
        }
    }

    private fun findNearestFreePosition(playerId: PlayerId, position: Position, additionalCondition: (Position) -> Boolean): Position {

        var radius = 1
        var resultPosition: Position? = null

        do {
            inRadiusDo(position, radius) { x, y ->
                val currentPosition = Position(x, y)
                if (resultPosition == null
                        && state.values.none { it.position == currentPosition && it.playerId != playerId }
                        && starSystemStore.state.size.contain(currentPosition)
                        && additionalCondition(currentPosition)
                ) {
                    resultPosition = currentPosition
                }
            }
            radius++
        } while (resultPosition == null)
        return resultPosition!!
    }

    private fun Map<Pair<PlayerId, ShipId>, Ship>.saveShipIfAlive(ship: Ship) =
            if (ship.hp > 0) this.plus(ship.playerId to ship.id to ship)
            else this.minus(ship.playerId to ship.id)
}

