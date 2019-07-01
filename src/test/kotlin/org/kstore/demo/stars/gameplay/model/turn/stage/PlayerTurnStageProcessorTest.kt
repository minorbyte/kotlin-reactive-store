package org.kstore.demo.stars.gameplay.model.turn.stage

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.kstore.demo.stars.*
import org.kstore.demo.stars.gameplay.model.*
import org.kstore.demo.stars.gameplay.model.player.PlayerId
import org.kstore.demo.stars.gameplay.model.player.colony.*
import org.kstore.demo.stars.gameplay.model.player.resources.*
import org.kstore.demo.stars.gameplay.model.player.resources.income.Income
import org.kstore.demo.stars.gameplay.model.turn.*
import org.kstore.demo.stars.rule.PlayerTurnStageType.*
import org.kstore.store.TestStore

@StarsTest
internal class PlayerTurnStageProcessorTest : DispatcherEnvironmentTest() {

    private lateinit var playerTurn: PlayerTurn
    private lateinit var colonies: List<Colony>
    private lateinit var playerIncome: Map<PlayerId, Income>

    private lateinit var playersTurnStore: TestStore<PlayerTurn>
    private lateinit var colonyStore: TestStore<List<Colony>>
    private lateinit var playerIncomeStore: TestStore<Map<PlayerId, Income>>

    @BeforeEach
    fun before() {
        playerTurn = RANDOM.nextObject()
        colonies = RANDOM.nextList()
        colonies = colonies.mapIndexed { index, colony ->
            if (index < colonies.size / 2) colony.copy(playerId = playerTurn.playerId) else colony
        }
        playerIncome = RANDOM.nextMap<PlayerId, Income>().plus(playerTurn.playerId to RANDOM.nextObject())

        playersTurnStore = TestStore(playerTurn)
        colonyStore = TestStore(colonies)
        playerIncomeStore = TestStore(playerIncome)
    }

    @Test
    fun `process BUILD`() {
        PlayerTurnStageProcessor(dispatcher, playersTurnStore, colonyStore, playerIncomeStore)

        dispatcher.action(StartPlayerTurnStageAction(BUILD))

        awaitThat {
            val actions = emittedActions.last(colonies.size / 2, DecreaseTurnsLeftBuildQueueAction::class)
            assertThat(actions.map { (it as DecreaseTurnsLeftBuildQueueAction).colonyId })
                    .isEqualTo(
                            colonies.filterIndexed { index, _ -> index < colonies.size / 2 }.map { it.id }
                    )
        }
    }

    @Test
    fun `process INCOME`() {
        PlayerTurnStageProcessor(dispatcher, playersTurnStore, colonyStore, playerIncomeStore)

        dispatcher.action(StartPlayerTurnStageAction(INCOME))

        awaitThat {
            val incomeAction = emittedActions.last(PlayerIncomeAction::class)

            assertThat(incomeAction.playerId).isEqualTo(playerTurn.playerId)
            assertThat(incomeAction.fuel).isEqualTo(playerIncome[playerTurn.playerId]!!.fuel)
            assertThat(incomeAction.resources).isEqualTo(playerIncome[playerTurn.playerId]!!.resources)
        }
    }

    @Test
    fun `process RESEARCH`() {
        PlayerTurnStageProcessor(dispatcher, playersTurnStore, colonyStore, playerIncomeStore)

        dispatcher.action(StartPlayerTurnStageAction(RESEARCH))

        awaitThat {
            val researchAction = emittedActions.last(DoPlayerResearchAction::class)
            assertThat(researchAction.playerId).isEqualTo(playerTurn.playerId)
            assertThat(researchAction.researchPoints).isEqualTo(playerIncome[playerTurn.playerId]!!.researchPoints)
        }
    }
}
