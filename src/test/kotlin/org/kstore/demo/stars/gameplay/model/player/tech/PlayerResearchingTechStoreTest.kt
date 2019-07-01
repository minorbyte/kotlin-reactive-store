package org.kstore.demo.stars.gameplay.model.player.tech

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kstore.demo.stars.*
import org.kstore.demo.stars.gameplay.model.*
import org.kstore.demo.stars.gameplay.model.player.resources.DoPlayerResearchAction
import org.kstore.demo.stars.rule.tech.TechTree
import org.kstore.randomItem
import java.util.*

@StarsTest
internal class PlayerResearchingTechStoreTest : GameDescriptionEnvironmentTest() {

    @Test
    fun `store takes data from gameDescription`() {
        val tested = PlayerResearchingTechStore(dispatcher, gameDescription)
        assertThat(tested.state).isEqualTo(
                gameDescription.players.associateBy({ it.id }, {
                    it.researchedTech?.let { Optional.of(ResearchingTech(it.first, it.second)) } ?: Optional.empty()
                })
        )
    }

    @Test
    fun `player starts to research tech`() {
        gameDescription = gameDescription.copy(players = gameDescription.players.map { it.copy(researchedTech = null) })

        val tested = PlayerResearchingTechStore(dispatcher, gameDescription)
        val techId = TechTree.keys.toList().randomItem()

        dispatcher.action(ResearchTechAction(gameDescription.currentPlayerId, techId))

        awaitThat {
            assertThat(tested.state[gameDescription.currentPlayerId]).isEqualTo(Optional.of(ResearchingTech(techId, 0)))
        }
    }

    @Test
    fun `player starts to research tech when is already researching another one`() {
        gameDescription = gameDescription.copy(players = gameDescription.players.map { it.copy(researchedTech = Pair(RANDOM.nextString(), RANDOM.nextInt())) })

        val tested = PlayerResearchingTechStore(dispatcher, gameDescription)
        val techId = TechTree.keys.toList().randomItem()

        dispatcher.action(ResearchTechAction(gameDescription.currentPlayerId, techId))

        awaitThat {
            assertThat(tested.state[gameDescription.currentPlayerId]).isEqualTo(Optional.of(ResearchingTech(techId, 0)))
        }
    }

    @Test
    fun `player cancels tech research `() {
        val techId = TechTree.keys.toList().randomItem()
        gameDescription = gameDescription.copy(players = gameDescription.players.map { it.copy(researchedTech = Pair(techId, RANDOM.nextInt())) })

        val tested = PlayerResearchingTechStore(dispatcher, gameDescription)

        dispatcher.action(CleanResearchedTechAction(gameDescription.currentPlayerId))

        awaitThat {
            assertThat(tested.state[gameDescription.currentPlayerId]).isEmpty()
        }
    }

    @Test
    fun `player spends his RP and tech is not researched`() {
        val tech = TechTree.values.toList().randomItem()
        val rp = RANDOM.nextInt(tech.cost / 2)
        val spentRp = RANDOM.nextInt(tech.cost - rp - 1)

        gameDescription = gameDescription.copy(players = gameDescription.players.map {
            it.copy(
                    researchedTech = Pair(tech.techId, spentRp)
            )
        })

        val tested = PlayerResearchingTechStore(dispatcher, gameDescription)

        dispatcher.action(DoPlayerResearchAction(gameDescription.currentPlayerId, rp))

        awaitThat {
            assertThat(emittedActions.lastOrNull(ResearchedTechAction::class)).isNull()
            assertThat(tested.state[gameDescription.currentPlayerId]).isEqualTo(Optional.of(ResearchingTech(tech.techId, spentRp + rp)))
        }
    }

    @Test
    fun `player spends his RP and tech is researched`() {
        val tech = TechTree.values.toList().randomItem()
        val rp = RANDOM.nextInt(tech.cost / 2)
        val spentRp = tech.cost - rp + 1

        gameDescription = gameDescription.copy(players = gameDescription.players.map {
            it.copy(
                    researchedTech = Pair(tech.techId, spentRp)
            )
        })

        val tested = PlayerResearchingTechStore(dispatcher, gameDescription)

        dispatcher.action(DoPlayerResearchAction(gameDescription.currentPlayerId, rp))

        awaitThat {
            assertThat(emittedActions.last(ResearchedTechAction::class)).isEqualTo(ResearchedTechAction(gameDescription.currentPlayerId, tech.techId))
            assertThat(tested.state[gameDescription.currentPlayerId]).isEmpty()
        }
    }
}
