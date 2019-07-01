package org.kstore.demo.stars.gameplay.model.player.ship

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.kstore.*
import org.kstore.demo.stars.*
import org.kstore.demo.stars.common.*
import org.kstore.demo.stars.gameplay.model.*
import org.kstore.demo.stars.gameplay.model.player.PlayerId
import org.kstore.demo.stars.gameplay.model.player.resources.PlayerFuelPaymentAction
import org.kstore.demo.stars.gameplay.model.starsystem.StarSystem
import org.kstore.demo.stars.rule.ship.ShipType
import org.kstore.demo.stars.rule.ship.ShipType.*
import org.kstore.demo.stars.rule.tech.TechId
import org.kstore.store.TestStore
import java.lang.Math.abs

@StarsTest
internal class PlayerShipsStoreTest : GameDescriptionEnvironmentTest() {

    private lateinit var playerTechStore: TestStore<Map<PlayerId, List<TechId>>>
    private lateinit var starSystemStore: TestStore<StarSystem>


    @BeforeEach
    fun before() {
        playerTechStore = TestStore(mapOf())
        starSystemStore = TestStore(RANDOM.nextObject())
    }

    @Test
    fun `initial store is empty`() {
        val tested = PlayerShipsStore(dispatcher, gameDescription, playerTechStore, starSystemStore)

        assertThat(tested.state).isEqualTo(
                gameDescription.players
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
                        })
        )
    }

    @Test
    fun `ShipCreateAction adds ship`(@RandomBean ship: Ship) {
        val tested = PlayerShipsStore(dispatcher, gameDescription.copy(
                players = gameDescription.players.map {
                    it.copy(
                            ships = listOf()
                    )
                }
        ), playerTechStore, starSystemStore)

        dispatcher.action(ShipCreateAction(ship, ship.playerId))

        awaitThat {
            assertThat(tested.state).hasSize(1)

            assertThat(tested.state.values.first().position).isEqualTo(ship.position)
            assertThat(tested.state.values.first().id).isEqualTo(ship.id)
            assertThat(tested.state.values.first().shipType).isEqualTo(ship.shipType)
            assertThat(tested.state.values.first().hp).isEqualTo(ship.hp)
            assertThat(tested.state.values.first().playerId).isEqualTo(ship.playerId)
        }
    }

    @Test
    fun `ShipCreateAction can't have different playerIds`(@RandomBean ship: Ship, @RandomBean playerId: PlayerId) {
        val tested = PlayerShipsStore(dispatcher, gameDescription.copy(
                players = gameDescription.players.map {
                    it.copy(
                            ships = listOf()
                    )
                }
        ), playerTechStore, starSystemStore)

        dispatcher.action(ShipCreateAction(ship, playerId))

        delayAwaitThat {
            assertThat(emittedActions.lastOrNull(ShipCreateAction::class)).isNull()
            assertThat(tested.state).hasSize(0)
        }
    }

    @Test
    fun `ship changes position on ShipMoveAction and emits ShipMovedAction`(@RandomBean ship: ShipDescription) {
        val tested = PlayerShipsStore(dispatcher, gameDescription.copy(
                players = gameDescription.players.map {
                    it.copy(
                            ships = if (it.id != gameDescription.currentPlayerId) listOf() else listOf(ship)
                    )
                }
        ), playerTechStore, starSystemStore)

        val position = positionRandomOffset(ship.position, 1)

        dispatcher.action(ShipMoveAction(gameDescription.currentPlayerId, ship.id, position))

        awaitThat {
            assertThat(tested.state).hasSize(1)

            assertThat(tested.state.values.first().position).isEqualTo(position)
            assertThat(tested.state.values.first().id).isEqualTo(ship.id)
            assertThat(tested.state.values.first().shipType).isEqualTo(ship.shipType)
            assertThat(tested.state.values.first().hp).isEqualTo(ship.hp)
            assertThat(tested.state.values.first().playerId).isEqualTo(gameDescription.currentPlayerId)

            val shipMovedAction = emittedActions.last(ShipMovedAction::class)

            assertThat(shipMovedAction.position).isEqualTo(position)
            assertThat(shipMovedAction.shipId).isEqualTo(ship.id)
            assertThat(shipMovedAction.playerId).isEqualTo(gameDescription.currentPlayerId)
        }
    }

    @Test
    fun `ship spents fuel on ShipMoveAction and emits ShipMovedAction`(@RandomBean ship: ShipDescription) {
        val tested = PlayerShipsStore(dispatcher, gameDescription.copy(
                players = gameDescription.players.map {
                    it.copy(
                            ships = if (it.id != gameDescription.currentPlayerId) listOf() else listOf(ship)
                    )
                }
        ), playerTechStore, starSystemStore)

        val position = when (RANDOM.nextInt(4)) {
            0 -> ship.position.copy(x = ship.position.x - 1)
            1 -> ship.position.copy(x = ship.position.x + 1)
            2 -> ship.position.copy(y = ship.position.y - 1)
            3 -> ship.position.copy(y = ship.position.y + 1)
            else -> ship.position
        }

        dispatcher.action(ShipMoveAction(gameDescription.currentPlayerId, ship.id, position))

        awaitThat {
            val shipMovedAction = emittedActions.last(PlayerFuelPaymentAction::class)

            assertThat(shipMovedAction.fuel).isEqualTo(ship.shipType.fuelConsumption)
            assertThat(shipMovedAction.playerId).isEqualTo(gameDescription.currentPlayerId)
        }
    }

    @Test
    fun `ship can't move more than 1 tile`(@RandomBean ship: ShipDescription) {
        val tested = PlayerShipsStore(dispatcher, gameDescription.copy(
                players = gameDescription.players.map {
                    it.copy(
                            ships = if (it.id != gameDescription.currentPlayerId) listOf() else listOf(ship)
                    )
                }
        ), playerTechStore, starSystemStore)

        val position = positionRandomOffset(ship.position, 2)

        dispatcher.action(ShipMoveAction(gameDescription.currentPlayerId, ship.id, position))

        awaitThat {
            assertThat(tested.state).hasSize(1)

            assertThat(tested.state.values.first().position).isEqualTo(ship.position)
            assertThat(tested.state.values.first().id).isEqualTo(ship.id)
            assertThat(tested.state.values.first().shipType).isEqualTo(ship.shipType)
            assertThat(tested.state.values.first().hp).isEqualTo(ship.hp)
            assertThat(tested.state.values.first().playerId).isEqualTo(gameDescription.currentPlayerId)

            assertThat(emittedActions.lastOrNull(ShipMovedAction::class)).isNull()
        }
    }

    @Test
    fun `ship attacks enemy ship at target position on ShipMoveAction and emits ShipMovedAction`(
            @RandomBean ship: ShipDescription,
            @RandomBean enemyShip: ShipDescription
    ) {
        val enemyPlayerId = gameDescription.players.first { it.id != gameDescription.currentPlayerId }.id
        val enemyPosition = when (RANDOM.nextInt(4)) {
            0 -> ship.position.copy(x = ship.position.x - 1)
            1 -> ship.position.copy(x = ship.position.x + 1)
            2 -> ship.position.copy(y = ship.position.y - 1)
            3 -> ship.position.copy(y = ship.position.y + 1)
            else -> ship.position
        }

        val tested = PlayerShipsStore(dispatcher, gameDescription.mapAllPlayers {
            it.copy(
                    ships = when (it.id) {
                        gameDescription.currentPlayerId -> listOf(ship)
                        enemyPlayerId -> listOf(enemyShip.copy(position = enemyPosition))
                        else -> listOf()
                    },
                    techs = RANDOM.nextList(TechId::class)
            )
        }, playerTechStore, starSystemStore)


        dispatcher.action(ShipMoveAction(gameDescription.currentPlayerId, ship.id, enemyPosition))

        awaitThat {
            assertThat(tested.state).hasSize(2)

            assertThat(tested.state[gameDescription.currentPlayerId to ship.id]!!).matches {
                it.position == ship.position
            }

            val shipMovedAction = emittedActions.last(ShipMovedAction::class)
            assertThat(shipMovedAction.position).isEqualTo(ship.position)
            assertThat(shipMovedAction.shipId).isEqualTo(ship.id)
            assertThat(shipMovedAction.playerId).isEqualTo(gameDescription.currentPlayerId)

            val battleStartAction = emittedActions.last(BattleStartAction::class)
            assertThat(battleStartAction.attacker.id).isEqualTo(ship.id)
            assertThat(battleStartAction.defender.id).isEqualTo(enemyShip.id)
            assertThat(battleStartAction.attackerTechs).isEmpty()
            assertThat(battleStartAction.defenderTechs).isEmpty()

        }
    }

    @Test
    fun `capital ships and frigates can jump and emits ShipJumpedAction`(
            @RandomBean shipTemplate: ShipDescription
    ) {
        val ship = shipTemplate.copy(
                shipType = RANDOM.next(CAPITAL, FRIGATE)
        )

        gameDescription = gameDescription.copy(
                starSystem = gameDescription.starSystem.copy(
                        size = Area(
                                height = abs(RANDOM.nextInt(100)),
                                width = abs(RANDOM.nextInt(100))
                        )
                ),
                players = gameDescription.players.map {
                    it.copy(
                            ships = if (it.id != gameDescription.currentPlayerId) listOf() else listOf(ship)
                    )
                }
        )

        val tested = PlayerShipsStore(dispatcher, gameDescription, playerTechStore, starSystemStore)

        val position = Position(
                x = RANDOM.nextInt(gameDescription.starSystem.size.width),
                y = RANDOM.nextInt(gameDescription.starSystem.size.height)
        )

        dispatcher.action(ShipJumpAction(gameDescription.currentPlayerId, ship.id, position))

        delayAwaitThat {
            val shipMovedAction = emittedActions.last(ShipMovedAction::class)

            assertThat(shipMovedAction).isNotNull()
            assertThat(inCircle(shipMovedAction.position, position, 3)).isTrue()
            assertThat(shipMovedAction.shipId).isEqualTo(ship.id)
            assertThat(shipMovedAction.playerId).isEqualTo(gameDescription.currentPlayerId)

            assertThat(tested.state).hasSize(1)

            assertThat(inCircle(tested.state.values.first().position, position, 3)).isTrue()
            assertThat(tested.state.values.first().id).isEqualTo(ship.id)
            assertThat(tested.state.values.first().shipType).isEqualTo(ship.shipType)
            assertThat(tested.state.values.first().hp).isEqualTo(ship.hp)
            assertThat(tested.state.values.first().playerId).isEqualTo(gameDescription.currentPlayerId)
        }
    }

    @Test
    fun `capital ships and frigates spent fuel on jump by PlayerFuelPaymentAction`(
            @RandomBean shipTemplate: ShipDescription
    ) {
        val ship = shipTemplate.copy(
                shipType = RANDOM.next(CAPITAL, FRIGATE)
        )

        gameDescription = gameDescription.copy(
                starSystem = gameDescription.starSystem.copy(
                        size = Area(
                                height = abs(RANDOM.nextInt(100)),
                                width = abs(RANDOM.nextInt(100))
                        )
                ),
                players = gameDescription.players.map {
                    it.copy(
                            ships = if (it.id != gameDescription.currentPlayerId) listOf() else listOf(ship)
                    )
                }
        )

        val tested = PlayerShipsStore(dispatcher, gameDescription, playerTechStore, starSystemStore)

        val position = Position(
                x = RANDOM.nextInt(gameDescription.starSystem.size.width),
                y = RANDOM.nextInt(gameDescription.starSystem.size.height)
        )

        dispatcher.action(ShipJumpAction(gameDescription.currentPlayerId, ship.id, position))

        delayAwaitThat {
            val paymentAction = emittedActions.last(PlayerFuelPaymentAction::class)

            assertThat(paymentAction).isNotNull()
            assertThat(paymentAction.fuel).isEqualTo(ship.shipType.fuelConsumption * JUMP_FUEL_MULTIPLIER)
            assertThat(paymentAction.playerId).isEqualTo(gameDescription.currentPlayerId)
        }
    }

    @Test
    fun `only capital ships and frigates can jump`(
            @RandomBean shipTemplate: ShipDescription
    ) {
        val ship = shipTemplate.copy(
                shipType = RANDOM.except({ it.isIn(CAPITAL, FRIGATE) }, { nextObject(ShipType::class) })
        )

        gameDescription = gameDescription.copy(
                starSystem = gameDescription.starSystem.copy(
                        size = Area(
                                height = abs(RANDOM.nextInt(100)),
                                width = abs(RANDOM.nextInt(100))
                        )
                ),
                players = gameDescription.players.map {
                    it.copy(
                            ships = if (it.id != gameDescription.currentPlayerId) listOf() else listOf(ship)
                    )
                }
        )

        val tested = PlayerShipsStore(dispatcher, gameDescription, playerTechStore, starSystemStore)

        val position = Position(
                x = RANDOM.nextInt(gameDescription.starSystem.size.width),
                y = RANDOM.nextInt(gameDescription.starSystem.size.height)
        )

        dispatcher.action(ShipJumpAction(gameDescription.currentPlayerId, ship.id, position))

        delayAwaitThat {
            assertThat(emittedActions.lastOrNull(ShipMovedAction::class)).isNull()

            assertThat(tested.state).hasSize(1)

            assertThat(tested.state.values.first().position).isEqualTo(ship.position)
            assertThat(tested.state.values.first().id).isEqualTo(ship.id)
            assertThat(tested.state.values.first().shipType).isEqualTo(ship.shipType)
            assertThat(tested.state.values.first().hp).isEqualTo(ship.hp)
            assertThat(tested.state.values.first().playerId).isEqualTo(gameDescription.currentPlayerId)
        }
    }

    @Test
    fun `when jump destination is occupied by capital or frigate then nearby empty tile is selected`(
            @RandomBean shipTemplate: ShipDescription
    ) {
        val enemyPlayerId = RANDOM.except({ it == gameDescription.currentPlayerId }, { gameDescription.players.randomItem().id })
        val ship = shipTemplate.copy(
                shipType = RANDOM.next(CAPITAL, FRIGATE)
        )

        gameDescription = gameDescription.copy(
                starSystem = gameDescription.starSystem.copy(
                        size = Area(
                                height = abs(RANDOM.nextInt(100)),
                                width = abs(RANDOM.nextInt(100))
                        )
                )
        )
        starSystemStore.setState(starSystemStore.state.copy(size = gameDescription.starSystem.size))

        val position = Position(
                x = RANDOM.nextInt(gameDescription.starSystem.size.width),
                y = RANDOM.nextInt(gameDescription.starSystem.size.height)
        )


        val emptyPosition = positionRandomOffset(position, RANDOM.nextInt(2))

        gameDescription = gameDescription.copy(
                players = gameDescription.players.map {
                    it.copy(
                            ships = when (it.id) {
                                gameDescription.currentPlayerId -> listOf(ship)
                                enemyPlayerId -> inCircle(position, 10)
                                        .filter { it != emptyPosition }
                                        .map {
                                            ship.copy(
                                                    id = RANDOM.nextString(),
                                                    position = it
                                            )
                                        }
                                else -> listOf()
                            }
                    )
                }
        )

        val tested = PlayerShipsStore(dispatcher, gameDescription, playerTechStore, starSystemStore)

        dispatcher.action(ShipJumpAction(gameDescription.currentPlayerId, ship.id, position))

        delayAwaitThat {
            val shipMovedAction = emittedActions.lastOrNull(ShipMovedAction::class)

            assertThat(shipMovedAction).isNotNull()
            assertThat(shipMovedAction!!.position).isEqualTo(emptyPosition)
            assertThat(shipMovedAction.shipId).isEqualTo(ship.id)
            assertThat(shipMovedAction.playerId).isEqualTo(gameDescription.currentPlayerId)
        }
    }

    @Test
    fun `when jump destination is occupied by light ship then enemy ship is moved to nearby tile`(
            @RandomBean shipTemplate: ShipDescription
    ) {
        val enemyPlayerId = RANDOM.except({ it == gameDescription.currentPlayerId }, { gameDescription.players.randomItem().id })
        val ship = shipTemplate.copy(
                shipType = RANDOM.next(CAPITAL, FRIGATE)
        )
        val enemyShip = shipTemplate.copy(
                shipType = RANDOM.next(SCOUT, FIGHTER, CORVETTE)
        )

        gameDescription = gameDescription.copy(
                starSystem = gameDescription.starSystem.copy(
                        size = Area(
                                height = abs(RANDOM.nextInt(100)),
                                width = abs(RANDOM.nextInt(100))
                        )
                )
        )
        starSystemStore.setState(starSystemStore.state.copy(size = gameDescription.starSystem.size))

        val position = Position(
                x = RANDOM.nextInt(gameDescription.starSystem.size.width),
                y = RANDOM.nextInt(gameDescription.starSystem.size.height)
        )

        gameDescription = gameDescription.copy(
                players = gameDescription.players.map {
                    it.copy(
                            ships = when (it.id) {
                                gameDescription.currentPlayerId -> listOf(ship)
                                enemyPlayerId -> inCircle(position, 10)
                                        .map {
                                            enemyShip.copy(
                                                    id = RANDOM.nextString(),
                                                    position = it
                                            )
                                        }
                                else -> listOf()
                            }
                    )
                }
        )

        val tested = PlayerShipsStore(dispatcher, gameDescription, playerTechStore, starSystemStore)

        dispatcher.action(ShipJumpAction(gameDescription.currentPlayerId, ship.id, position))

        delayAwaitThat {
            val shipMovedAction = emittedActions.lastOrNull(ShipMovedAction::class)

            assertThat(shipMovedAction).isNotNull()
            assertThat(inCircle(shipMovedAction!!.position, position, 3)).isTrue()
            assertThat(shipMovedAction.shipId).isEqualTo(ship.id)
            assertThat(shipMovedAction.playerId).isEqualTo(gameDescription.currentPlayerId)

            val changedPositions = gameDescription.players.first { it.id == enemyPlayerId }.ships.map {
                it.position to tested.state[enemyPlayerId to it.id]!!.position
            }.filter { it.first != it.second }

            assertThat(changedPositions).hasSize(1)
            assertThat(changedPositions.first().first).isEqualTo(shipMovedAction.position)
        }
    }

    @Test
    fun `jump fuel consumption equals to 20 moves`(
            @RandomBean shipTemplate: ShipDescription
    ) {
        assertThat(JUMP_FUEL_MULTIPLIER).isEqualTo(20)
    }

    private fun positionRandomOffset(position: Position, radius: Int) =
            when (RANDOM.nextInt(4)) {
                0 -> position.copy(x = position.x - radius)
                1 -> position.copy(x = position.x + radius)
                2 -> position.copy(y = position.y - radius)
                else -> position.copy(y = position.y + radius)
            }
}
