package org.kstore.demo.stars.gameplay.model.turn.stage

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.kstore.RandomBean
import org.kstore.demo.stars.awaitThat
import org.kstore.demo.stars.gameplay.model.*
import org.kstore.demo.stars.gameplay.model.player.PlayerId
import org.kstore.demo.stars.gameplay.model.turn.*
import org.kstore.demo.stars.rule.PlayerTurnStageType.*
import org.kstore.store.TestSubscribable
import react.kstore.action.FailedAction

@StarsTest
internal class PlayerTurnStageStoreTest : GameDescriptionEnvironmentTest() {

    private lateinit var playerTurn: PlayerTurn
    private lateinit var playerTurnStore: TestSubscribable<PlayerTurn>

    @BeforeEach
    fun before() {
        playerTurn = PlayerTurn(gameDescription.currentTurn, gameDescription.currentPlayerId, gameDescription.currentPlayer().name)
        playerTurnStore = TestSubscribable(playerTurn)
    }

    @Test
    fun `initial state is taken from gameDescription`() {
        assertThat(PlayerTurnStageStore(gameDescription, playerTurnStore, dispatcher).state).isEqualTo(
                PlayerTurnStage(
                        playerId = gameDescription.currentPlayerId,
                        playerName = gameDescription.currentPlayer().name,
                        turnNumber = gameDescription.currentTurn,
                        turnStage = gameDescription.currentPlayerTurnStage
                )
        )
    }

    @Test
    fun `not updates from playerTurnStore when player and turn remains same`(
            @RandomBean playerName: String
    ) {
        val tested = PlayerTurnStageStore(gameDescription, playerTurnStore, dispatcher)

        val state = tested.state

        playerTurnStore.setState(playerTurn.copy(playerName = playerName))

        awaitThat {
            assertThat(tested.state).isSameAs(state)
        }
    }

    @Test
    fun `updates from playerTurnStore when player changes`(
            @RandomBean playerId: PlayerId
    ) {
        val tested = PlayerTurnStageStore(gameDescription.copy(currentPlayerTurnStage = MOVEMENT), playerTurnStore, dispatcher)

        playerTurnStore.setState(playerTurn.copy(playerId = playerId))

        awaitThat {
            assertThat(tested.state).isEqualTo(PlayerTurnStage(
                    playerId = playerId,
                    playerName = playerTurn.playerName,
                    turnNumber = playerTurn.turnNumber,
                    turnStage = INCOME
            ))
        }
    }

    @Test
    fun `updates from playerTurnStore when turn changes`() {
        val tested = PlayerTurnStageStore(gameDescription.copy(currentPlayerTurnStage = MOVEMENT), playerTurnStore, dispatcher)

        playerTurnStore.setState(playerTurn.copy(turnNumber = gameDescription.currentTurn + 1))

        awaitThat {
            assertThat(tested.state).isEqualTo(PlayerTurnStage(
                    playerId = playerTurn.playerId,
                    playerName = playerTurn.playerName,
                    turnNumber = playerTurn.turnNumber + 1,
                    turnStage = INCOME
            ))
        }
    }

    @Test
    fun `updates stage on StartPlayerTurnStageAction`() {
        val tested = PlayerTurnStageStore(gameDescription.copy(currentPlayerTurnStage = RESEARCH), playerTurnStore, dispatcher)

        dispatcher.action(StartPlayerTurnStageAction(BUILD))

        awaitThat {
            assertThat(tested.state).isEqualTo(PlayerTurnStage(
                    playerId = playerTurn.playerId,
                    playerName = playerTurn.playerName,
                    turnNumber = playerTurn.turnNumber,
                    turnStage = BUILD
            ))
        }
    }

    @Test
    fun `forbids stage update if StartPlayerTurnStageAction has incorrect stage`() {
        val tested = PlayerTurnStageStore(gameDescription.copy(currentPlayerTurnStage = RESEARCH), playerTurnStore, dispatcher)

        dispatcher.action(StartPlayerTurnStageAction(MOVEMENT))

        awaitThat {
            assertThat(tested.state).isEqualTo(PlayerTurnStage(
                    playerId = playerTurn.playerId,
                    playerName = playerTurn.playerName,
                    turnNumber = playerTurn.turnNumber,
                    turnStage = RESEARCH
            ))
        }
    }

    @Test
    fun `emits EndOfPlayerTurnAction if it is last stage on EndOfPlayerTurnStageAction`() {
        val tested = PlayerTurnStageStore(gameDescription.copy(currentPlayerTurnStage = MOVEMENT), playerTurnStore, dispatcher)

        dispatcher.action(EndOfPlayerTurnStageAction(MOVEMENT))

        awaitThat {
            assertThat(tested.state).isEqualTo(PlayerTurnStage(
                    playerId = playerTurn.playerId,
                    playerName = playerTurn.playerName,
                    turnNumber = playerTurn.turnNumber,
                    turnStage = MOVEMENT
            ))
            assertThat(emittedActions.lastOrNull(EndOfPlayerTurnAction::class)).isNotNull()
        }
    }

    @Test
    fun `emits EndOfPlayerTurnAction if it is not last stage on EndOfPlayerTurnStageAction`() {
        var emitted = false
        val tested = PlayerTurnStageStore(gameDescription.copy(currentPlayerTurnStage = RESEARCH), playerTurnStore, dispatcher)

        dispatcher.intercept(StartPlayerTurnStageAction::class) {
            emitted = true
            assertThat(it.turnStage).isEqualTo(BUILD)
            FailedAction(it)
        }

        dispatcher.action(EndOfPlayerTurnStageAction(RESEARCH))

        awaitThat {
            assertThat(tested.state).isEqualTo(PlayerTurnStage(
                    playerId = playerTurn.playerId,
                    playerName = playerTurn.playerName,
                    turnNumber = playerTurn.turnNumber,
                    turnStage = RESEARCH
            ))
            assertThat(emitted).isTrue()
        }
    }

    @Test
    fun `forbids stage update if EndOfPlayerTurnStageAction has incorrect stage`() {
        val tested = PlayerTurnStageStore(gameDescription.copy(currentPlayerTurnStage = RESEARCH), playerTurnStore, dispatcher)

        dispatcher.action(EndOfPlayerTurnStageAction(BUILD))

        awaitThat {
            assertThat(tested.state).isEqualTo(PlayerTurnStage(
                    playerId = playerTurn.playerId,
                    playerName = playerTurn.playerName,
                    turnNumber = playerTurn.turnNumber,
                    turnStage = RESEARCH
            ))
        }
    }

}
