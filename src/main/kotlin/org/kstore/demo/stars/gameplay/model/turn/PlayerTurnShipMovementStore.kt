package org.kstore.demo.stars.gameplay.model.turn

import org.kstore.demo.stars.GameDescription
import org.kstore.demo.stars.gameplay.model.player.ship.*
import org.kstore.demo.stars.rule.PlayerTurnStageType
import react.kstore.*
import react.kstore.action.*
import react.kstore.reaction.Validation.Companion.condition

class PlayerTurnShipMovementStore(
        gameDescription: GameDescription,
        currentPlayerShipsStore: Store<List<Ship>>,
        dispatcher: Dispatcher = CommonDispatcher
) : BasicStore<Map<ShipId, Int>>(
        dispatcher = dispatcher,
        initialState = gameDescription.currentPlayerTurnShipMovements.associateBy({ it.first }, { it.second }),
        reactions = {
            on(StartPlayerTurnStageAction::class) update { _, action ->
                if (action.turnStage != PlayerTurnStageType.MOVEMENT) mapOf()
                else currentPlayerShipsStore.state.associate { Pair(it.id, it.shipType.moves) }
            }
            on(ShipCreateAction::class) {
                update { state, action ->
                    state.plus(Pair(action.ship.id, action.ship.shipType.moves))
                }
            }
            on(ShipMoveAction::class) {
                validate { state, action ->
                    condition(
                            state.getOrDefault(action.shipId, 0) > 0,
                            "No more moves for ship ${action.shipId} in this turn"
                    )
                }
                update { state, action ->
                    state.plus(Pair(action.shipId, state.getOrDefault(action.shipId, 1) - 1))
                }
            }
            on(ShipFinalMovement::class) {
                validate { state, action ->
                    condition(
                            state.getOrDefault(action.shipId, 0) > 0,
                            "No more moves for ship ${action.shipId} in this turn"
                    )
                }
                update { state, action ->
                    state.plus(Pair(action.shipId, 0))
                }
            }
        }
)
