package org.kstore.demo.stars.gameplay.model.starsystem

import org.kstore.demo.stars.common.Position
import org.kstore.demo.stars.rule.starsystem.*
import org.kstore.demo.stars.rule.starsystem.PlanetType.STANDARD
import org.kstore.demo.stars.rule.starsystem.ResourceMultiplier.NORMAL

data class Planet(
        val id: String,
        val title: String,
        val position: Position,
        val planetType: PlanetType = STANDARD,
        val resourceMultiplier: ResourceMultiplier = NORMAL,
        val habitable: Boolean = planetType.habitable,
        val maxBuildings: Int = planetType.maxBuildings
)

