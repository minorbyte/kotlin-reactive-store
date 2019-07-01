package org.kstore.demo.stars.gameplay.model.player

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.kstore.demo.stars.gameplay.model.*

@StarsTest
internal class PlayerStoreTest : GameDescriptionEnvironmentTest() {

    private lateinit var tested: PlayerStore

    @BeforeEach
    fun before() {
        tested = PlayerStore(gameDescription)
    }

    @Test
    fun `store takes data from gameDescription`() {
        assertThat(tested.state.size).isEqualTo(gameDescription.players.size)
        assertThat(tested.state.values).containsExactlyInAnyOrderElementsOf(gameDescription.players.map {
            Player(it.id, it.moveOrder, it.ai, it.name)
        })
        assertThat(tested.state.entries).allMatch { it.key == it.value.id }
    }

}
