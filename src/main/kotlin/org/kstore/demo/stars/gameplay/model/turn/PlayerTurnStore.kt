package org.kstore.demo.stars.gameplay.model.turn

import org.kstore.demo.stars.GameDescription
import org.kstore.demo.stars.gameplay.model.player.PlayerId
import react.kstore.*
import react.kstore.action.*
import react.kstore.dependency.dependsOn
import react.kstore.reaction.Validation.Companion.condition
import react.kstore.reaction.on

class PlayerTurnStore(
        gameDescription: GameDescription,
        turnStore: Subscribable<Turn>,
        dispatcher: Dispatcher = CommonDispatcher
) : BasicStore<PlayerTurn>(
        dispatcher = dispatcher,
        initialState = PlayerTurn(
                turnNumber = gameDescription.currentTurn,
                playerId = gameDescription.currentPlayerId,
                playerName = gameDescription.players.first { it.id == gameDescription.currentPlayerId }.name
        )
) {

    internal var players: List<Pair<PlayerId, String>> = gameDescription.players.sortedBy { it.moveOrder }.map { Pair(it.id, it.name) }
    internal var index: Int = players.indexOfFirst { it.first == gameDescription.currentPlayerId }

    init {
        dependsOn {
            stores(
                    turnStore
            ) merge { turn, state ->
                if (state.turnNumber == turn.number) state
                else {
                    index = 0

                    players[index].let {
                        PlayerTurn(
                                turnNumber = turn.number,
                                playerId = it.first,
                                playerName = it.second
                        )
                    }
                }
            }
        }
        on(EndOfPlayerTurnAction::class) update { state, _ ->
            if (index >= players.size - 1) {
                asyncAction(EndOfTurnAction())
                state
            } else {
                index += 1
                state.copy(
                        playerId = players[index].first,
                        playerName = players[index].second
                )
            }
        }
        on(PlayerAction::class) validate { state, action ->
            condition(
                    action.playerId == state.playerId,
                    "Player actions are allowed only for active player (${state.playerId}). $action"
            )
        }
    }

}
