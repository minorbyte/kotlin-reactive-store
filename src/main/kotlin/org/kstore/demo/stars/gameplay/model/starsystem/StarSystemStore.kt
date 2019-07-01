package org.kstore.demo.stars.gameplay.model.starsystem

import org.kstore.demo.stars.GameDescription
import react.kstore.BasicStore

class StarSystemStore(
        gameDescription: GameDescription
) : BasicStore<StarSystem>(
        initialState = gameDescription.starSystem.let { starSystem ->
            StarSystem(
                    name = starSystem.name,
                    size = starSystem.size,
                    star = starSystem.star.let {
                        Star(
                                id = it.id,
                                title = it.title,
                                position = it.position,
                                radius = it.radius
                        )
                    },
                    planets = starSystem.planets.map {
                        Planet(
                                id = it.id,
                                position = it.position,
                                title = it.title,
                                planetType = it.planetType,
                                resourceMultiplier = it.resourceMultiplier
                        )
                    }
            )
        }
)

