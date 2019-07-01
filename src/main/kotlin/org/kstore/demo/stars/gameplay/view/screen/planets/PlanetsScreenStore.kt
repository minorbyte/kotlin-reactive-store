package org.kstore.demo.stars.gameplay.view.screen.planets

import org.kstore.demo.stars.common.Position
import org.kstore.demo.stars.gameplay.model.turn.map.*
import org.kstore.demo.stars.gameplay.view.screen.BasicScreenStore
import org.kstore.demo.stars.rule.starsystem.*
import org.springframework.stereotype.Service
import react.kstore.dependency.dependsOn

@Service
class PlanetsScreenStore(
        playerTurnMapStore: PlayerTurnMapStore
) : BasicScreenStore(
) {
    init {
        dependsOn {
            stores(
                    playerTurnMapStore
            ) {
                rewrite { map: PlayerTurnMap ->

                    printPlanetsScreen(
                            map.tiles
                                    .filter { it.value is PlanetTile || it.value is ColonyTile }
                                    .map {
                                        val value = it.value
                                        when (value) {
                                            is PlanetTile -> PlanetsViewRow(
                                                    id = value.planet.id,
                                                    title = value.planet.title,
                                                    position = value.planet.position,
                                                    planetType = value.planet.planetType,
                                                    resourceMultiplier = value.planet.resourceMultiplier
                                            )
                                            is ColonyTile -> PlanetsViewRow(
                                                    id = value.planet.id,
                                                    title = value.planet.title,
                                                    position = value.planet.position,
                                                    planetType = value.planet.planetType,
                                                    resourceMultiplier = value.planet.resourceMultiplier
                                            )
                                            else -> throw IllegalStateException()
                                        }
                                    }
                    )
                }
            }
        }
    }
}

data class PlanetsViewRow(
        val id: String,
        val title: String,
        val position: Position,
        val planetType: PlanetType,
        val resourceMultiplier: ResourceMultiplier
)
