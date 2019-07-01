package org.kstore.demo.stars.gameplay.model.starsystem

import org.kstore.demo.stars.common.Area


data class StarSystem(
        val name: String,
        val size: Area,
        val star: Star,
        val planets: List<Planet> = listOf()
)

