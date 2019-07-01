package org.kstore.demo.stars.gameplay.model.player.tech

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kstore.demo.stars.awaitThat
import org.kstore.demo.stars.gameplay.model.*
import org.kstore.demo.stars.rule.tech.TechTree
import org.kstore.randomItem

@StarsTest
internal class PlayerTechStoreTest : GameDescriptionEnvironmentTest() {

    @Test
    fun `store takes data from gameDescription`() {
        val tested = PlayerTechStore(dispatcher, gameDescription)
        assertThat(tested.state).isEqualTo(gameDescription.players.associateBy({ it.id }, { it.techs }))
    }

    @Test
    fun `researched tech added to state`() {
        gameDescription = gameDescription.copy(
                players = gameDescription.players.map {
                    if (it.id == gameDescription.currentPlayerId) it.copy(techs = listOf())
                    else it
                }
        )

        val tested = PlayerTechStore(dispatcher, gameDescription)
        val techId = TechTree.keys.toList().randomItem()

        dispatcher.action(ResearchedTechAction(gameDescription.currentPlayerId, techId))

        awaitThat {
            assertThat(tested.state[gameDescription.currentPlayerId]).isEqualTo(listOf(techId))
        }
    }

    @Test
    fun `can't start tech research if it is already researched`() {
        val techId = TechTree.keys.toList().randomItem()
        gameDescription = gameDescription.copy(
                players = gameDescription.players.map {
                    if (it.id == gameDescription.currentPlayerId) it.copy(techs = listOf(techId))
                    else it
                }
        )

        val tested = PlayerTechStore(dispatcher, gameDescription)

        dispatcher.action(ResearchTechAction(gameDescription.currentPlayerId, techId))

        awaitThat {
            assertThat(emittedActions.lastOrNull(ResearchTechAction::class)).isNull()
        }
    }
}
