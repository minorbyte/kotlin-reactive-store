package org.kstore.demo.stars.gameplay.model.turn

import org.assertj.core.api.Assertions.assertThat
import org.awaitility.Awaitility.await
import org.awaitility.Duration.ONE_HUNDRED_MILLISECONDS
import org.junit.jupiter.api.*
import org.kstore.action.*
import org.kstore.demo.stars.awaitThat
import org.kstore.demo.stars.gameplay.model.*
import org.kstore.demo.stars.gameplay.model.player.PlayerId
import org.kstore.store.TestSubscribable

@StarsTest
internal class PlayerTurnStoreTest : GameDescriptionEnvironmentTest() {

    private lateinit var turn: TestSubscribable<Turn>

    @BeforeEach
    fun before() {
        turn = TestSubscribable(Turn(gameDescription.currentTurn))
    }

    @AfterEach
    fun after() {
        turn.stop()
    }

    @Test
    fun `initial store takes data from gameDescription`() {
        assertThat(PlayerTurnStore(gameDescription, turn, dispatcher).state).isEqualTo(PlayerTurn(
                gameDescription.currentTurn,
                gameDescription.currentPlayerId,
                gameDescription.players.first { it.id == gameDescription.currentPlayerId }.name
        ))
    }

    @Test
    fun `when turn store updates turn first player turn starts`() {
        val tested = PlayerTurnStore(gameDescription, turn, dispatcher)

        turn.setState(Turn(gameDescription.currentTurn + 1))

        val first = gameDescription.players.sortedBy { it.moveOrder }.first()

        awaitThat {
            assertThat(tested.state).isEqualTo(PlayerTurn(gameDescription.currentTurn + 1, first.id, first.name))
        }
    }

    @Test
    fun `when turn store updates, but not turn, state not changes`() {
        val tested = PlayerTurnStore(gameDescription, turn, dispatcher)

        turn.setState(Turn(gameDescription.currentTurn))

        val current = gameDescription.currentPlayer()

        await().pollDelay(ONE_HUNDRED_MILLISECONDS).untilAsserted {
            assertThat(tested.state).isEqualTo(PlayerTurn(gameDescription.currentTurn, current.id, current.name))
        }
    }

    @Test
    fun `switch to next player on EndCurrentPlayerTurnAction`() {
        val currentPlayer = gameDescription.players.sortedBy { it.moveOrder }.first()
        val nextPlayer = gameDescription.players.sortedBy { it.moveOrder }[1]

        val tested = PlayerTurnStore(gameDescription.copy(currentPlayerId = currentPlayer.id), turn, dispatcher)

        dispatcher.action(EndOfPlayerTurnAction())

        awaitThat {
            assertThat(tested.state).isEqualTo(PlayerTurn(gameDescription.currentTurn, nextPlayer.id, nextPlayer.name))
        }
    }

    @Test
    fun `switch to first player and emit EndOfTurnAction on EndCurrentPlayerTurnAction at last player`(
            @SubscribeTo emittedActions: EmittedActions
    ) {
        val lastPlayer = gameDescription.players.sortedBy { it.moveOrder }.last()

        val tested = PlayerTurnStore(gameDescription.copy(currentPlayerId = lastPlayer.id), turn, dispatcher)

        dispatcher.action(EndOfPlayerTurnAction())

        await().pollDelay(ONE_HUNDRED_MILLISECONDS).untilAsserted {
            assertThat(tested.state).isEqualTo(PlayerTurn(gameDescription.currentTurn, lastPlayer.id, lastPlayer.name))
            assertThat(emittedActions.last()).isInstanceOf(EndOfTurnAction::class.java)
        }
    }

    @Test
    fun `forbid PlayerAction for inactive player`(
            @SubscribeTo emittedActions: EmittedActions
    ) {
        val first = gameDescription.players.sortedBy { it.moveOrder }.first()
        val second = gameDescription.players.sortedBy { it.moveOrder }[1]

        PlayerTurnStore(gameDescription.copy(currentPlayerId = first.id), turn, dispatcher)

        dispatcher.action(SomePlayerAction(second.id))

        await().pollDelay(ONE_HUNDRED_MILLISECONDS).untilAsserted {
            assertThat(emittedActions.lastOrNull(SomePlayerAction::class)).isNull()
        }
    }

    data class SomePlayerAction(
            override val playerId: PlayerId
    ) : PlayerAction
}
