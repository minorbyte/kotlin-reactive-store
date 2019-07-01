package org.kstore.demo.stars.gameplay.model.turn

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.kstore.demo.stars.awaitThat
import org.kstore.demo.stars.gameplay.model.*
import react.kstore.action.action

@StarsTest
internal class TurnStoreTest : GameDescriptionEnvironmentTest() {

    private lateinit var tested: TurnStore

    @BeforeEach
    fun before() {
        tested = TurnStore(gameDescription)
    }

    @Test
    fun `initial store takes data from gameDescription`() {
        assertThat(tested.state).isEqualTo(Turn(gameDescription.currentTurn))
    }

    @Test
    fun `after end of turn turn number is increased`() {
        action(EndOfTurnAction())

        awaitThat {
            assertThat(tested.state).isEqualTo(Turn(gameDescription.currentTurn + 1))
        }
    }
}
