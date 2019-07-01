package org.kstore.demo.stars.gameplay.model.player.colony

import org.kstore.demo.stars.common.Position
import org.kstore.demo.stars.gameplay.model.player.PlayerId
import org.kstore.demo.stars.rule.starsystem.ResourceMultiplier

typealias ColonyId = String

enum class ColonyPlanetType {
    SMALL, STANDARD, MOON
}

data class Colony(
        val id: ColonyId,
        val playerId: PlayerId,
        val position: Position,
        val planetResourceMultiplier: ResourceMultiplier,
        val planetMaxBuildings: Int,
        val planetSize: ColonyPlanetType,
        val name: String
)
