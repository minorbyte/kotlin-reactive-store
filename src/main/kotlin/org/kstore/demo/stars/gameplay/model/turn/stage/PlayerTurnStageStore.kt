package org.kstore.demo.stars.gameplay.model.turn.stage

import org.kstore.demo.stars.GameDescription
import org.kstore.demo.stars.gameplay.model.turn.*
import org.kstore.demo.stars.rule.PlayerTurnStageType.INCOME
import react.kstore.*
import react.kstore.action.*
import react.kstore.dependency.dependsOn
import react.kstore.reaction.Validation.Companion.condition
import react.kstore.reaction.on

class PlayerTurnStageStore(
        gameDescription: GameDescription,
        playerTurnStore: Subscribable<PlayerTurn>,
        dispatcher: Dispatcher = CommonDispatcher
) : BasicStore<PlayerTurnStage>(
        dispatcher = dispatcher,
        initialState = PlayerTurnStage(
                turnNumber = gameDescription.currentTurn,
                playerId = gameDescription.currentPlayerId,
                playerName = gameDescription.players.first { it.id == gameDescription.currentPlayerId }.name,
                turnStage = gameDescription.currentPlayerTurnStage
        )
) {

    init {
        dependsOn {
            stores(playerTurnStore) merge { playerTurn, state ->
                if (playerTurn.playerId == state.playerId && playerTurn.turnNumber == state.turnNumber) state
                else {
                    dispatcher.asyncAction(StartPlayerTurnStageAction(INCOME))
                    PlayerTurnStage(
                            playerId = playerTurn.playerId,
                            playerName = playerTurn.playerName,
                            turnNumber = playerTurn.turnNumber,
                            turnStage = INCOME
                    )
                }
            }
            on(StartPlayerTurnStageAction::class) {
                update { state, action ->
                    logger.info("Started stage: ${action.turnStage}")
                    state.copy(turnStage = action.turnStage)
                }
                validate { state, action ->
                    condition(
                            state.turnStage.next() == action.turnStage || action.turnStage == INCOME && state.turnStage == INCOME,
                            "Invalid state ${action.turnStage}. Expected ${state.turnStage.next()}"
                    )
                }
            }
            on(EndOfPlayerTurnStageAction::class) {
                react { _, endPlayerTurnStageAction ->
                    logger.info("Finished stage: ${endPlayerTurnStageAction.turnStage}")

                    if (endPlayerTurnStageAction.turnStage.entOfTurn) dispatcher.action(EndOfPlayerTurnAction())
                    else dispatcher.action(StartPlayerTurnStageAction(endPlayerTurnStageAction.turnStage.next()))
                }
                validate { state, action ->
                    condition(
                            state.turnStage == action.turnStage,
                            "Invalid state ${action.turnStage}. Expected ${state.turnStage}"
                    )
                }
            }
        }
    }
}
