package org.kstore.demo.stars.gameplay.model.turn

import org.kstore.demo.stars.GameDescription
import react.kstore.BasicStore

class TurnStore(
        gameDescription: GameDescription
) : BasicStore<Turn>(
        initialState = Turn(gameDescription.currentTurn),
        reactions = {
            on(EndOfTurnAction::class) update { state, _ ->
                state.copy(
                        number = state.number + 1
                )
            }
        }
)
