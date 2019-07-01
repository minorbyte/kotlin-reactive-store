package org.kstore.demo.stars.gameplay.model.player.tech

import org.kstore.demo.stars.gameplay.model.player.PlayerId
import org.kstore.demo.stars.rule.tech.TechId

data class SpendResearchPointsAction(
        val playerId: PlayerId,
        val researchPoints: Int
)

data class ResearchTechAction(
        val playerId: PlayerId,
        val techId: TechId
)

data class ResearchedTechAction(
        val playerId: PlayerId,
        val techId: TechId
)

data class CleanResearchedTechAction(
        val playerId: PlayerId
)
