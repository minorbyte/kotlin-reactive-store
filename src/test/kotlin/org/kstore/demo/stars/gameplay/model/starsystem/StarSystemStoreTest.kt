package org.kstore.demo.stars.gameplay.model.starsystem

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.kstore.demo.stars.gameplay.model.*

@StarsTest
internal class StarSystemStoreTest : GameDescriptionEnvironmentTest() {

    private lateinit var tested: StarSystemStore

    @BeforeEach
    fun before() {
        tested = StarSystemStore(gameDescription)
    }

    @Test
    fun `store takes data from gameDescription`() {
        assertThat(tested.state.size).isEqualTo(gameDescription.starSystem.size)
        assertThat(tested.state.name).isEqualTo(gameDescription.starSystem.name)
        assertThat(tested.state.planets).isEqualTo(gameDescription.starSystem.planets.map {
            Planet(
                    id = it.id,
                    position = it.position,
                    title = it.title,
                    planetType = it.planetType,
                    resourceMultiplier = it.resourceMultiplier
            )
        })
        assertThat(tested.state.star).isEqualTo(gameDescription.starSystem.star.let {
            Star(
                    id = it.id,
                    title = it.title,
                    position = it.position,
                    radius = it.radius
            )
        })
    }

}
