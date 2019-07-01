package org.kstore.demo.stars.gameplay.model.turn.stage

import org.kstore.demo.stars.gameplay.model.player.PlayerId
import org.kstore.demo.stars.gameplay.model.player.colony.*
import org.kstore.demo.stars.gameplay.model.player.resources.*
import org.kstore.demo.stars.gameplay.model.player.resources.income.Income
import org.kstore.demo.stars.gameplay.model.turn.*
import org.kstore.demo.stars.rule.PlayerTurnStageType.*
import react.kstore.Store
import react.kstore.action.Dispatcher
import javax.annotation.PreDestroy

class PlayerTurnStageProcessor(
        private val dispatcher: Dispatcher,
        private val playersTurnTurnStore: Store<PlayerTurn>,
        private val colonyStore: Store<List<Colony>>,
        private val playerIncomeStore: Store<Map<PlayerId, Income>>
) {
    private val startPlayerTurnStageActionSubscription = dispatcher.subscribe(StartPlayerTurnStageAction::class) { action ->
        playersTurnTurnStore.state.let {
            when (action.turnStage) {
                BUILD -> processBuild(it.playerId)
                INCOME -> processIncome(it.playerId)
                RESEARCH -> processResearch(it.playerId)
            }
        }

        if (!action.turnStage.endsByUser) dispatcher.action(EndOfPlayerTurnStageAction(action.turnStage))
    }

    @PreDestroy
    fun destroy() {
        startPlayerTurnStageActionSubscription.unsubscribe()
    }

    private fun processIncome(playerId: PlayerId) {
        val (resources, fuel, _) = playerIncomeStore.state.getOrDefault(playerId, Income())
        dispatcher.action(PlayerIncomeAction(
                playerId = playerId,
                fuel = fuel,
                resources = resources
        ))
    }

    private fun processBuild(playerId: PlayerId) {
        colonyStore
                .state
                .filter { it.playerId == playerId }
                .forEach { colony ->
                    dispatcher.action(DecreaseTurnsLeftBuildQueueAction(colony.id))
                }
    }

    private fun processResearch(playerId: PlayerId) {
        val (_, _, researchPoints) = playerIncomeStore.state.getOrDefault(playerId, Income())
        dispatcher.action(DoPlayerResearchAction(playerId, researchPoints))
    }
}
