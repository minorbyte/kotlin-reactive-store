package org.kstore.demo.stars.gameplay.view.active.tile

import org.kstore.demo.stars.common.Position
import org.kstore.demo.stars.gameplay.model.player.ship.ShipMoveAction
import org.kstore.demo.stars.gameplay.view.active.ship.ActiveShipMoveAction
import org.kstore.demo.stars.gameplay.view.active.tile.CursorState.*
import org.springframework.stereotype.Service
import react.kstore.BasicStore
import react.kstore.action.CommonDispatcher.action
import react.kstore.reaction.FailedActionValidation

@Service
class CursorStore : BasicStore<Cursor>(
        initialState = Cursor(Position(5, 5), FREE),
        reactions = {
            on(CursorSetPositionAction::class) {
                update { state, action ->
                    state.copy(position = action.position)
                }
            }
            on(CursorMoveAction::class) {
                react { state, action ->
                    val position = Position(
                            state.position.x + action.deltaX,
                            state.position.y + action.deltaY
                    )
                    action(CursorSetPositionAction(position))

                    if (state.state == MOVE) {
                        action(ActiveShipMoveAction(position))
                    }
                }
            }
            on(CursorSetFreeAction::class) {
                update { state, _ ->
                    state.copy(
                            state = FREE
                    )
                }
            }
            on(CursorSetShipMovementAction::class) {
                update { state, _ ->
                    state.copy(
                            state = MOVE
                    )
                }
            }
            on(CursorSetShipJumpAction::class) {
                update { state, _ ->
                    state.copy(
                            state = JUMP
                    )
                }
            }
        }
)
