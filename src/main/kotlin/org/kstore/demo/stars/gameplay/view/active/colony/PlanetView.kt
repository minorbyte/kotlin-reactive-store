package org.kstore.demo.stars.gameplay.view.active.colony

import org.kstore.demo.stars.common.Position
import org.kstore.demo.stars.rule.starsystem.ResourceMultiplier

data class PlanetView(
        val position: Position,
        val planetName: String,
        val planetSize: String,
        val resourceMultiplier: ResourceMultiplier,
        val maxBuildings: Int
)
