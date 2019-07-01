package org.kstore.demo.stars.gameplay.view.screen.planets

import org.kstore.demo.stars.common.*


fun printPlanetsScreen(
        planets: List<PlanetsViewRow>
//        buildings: List<String>,
//        buildQueue: List<String>,
//        summary: List<String>
) =
        mutableListOf("================================= PLANETS ====================================")
                .append(
                        planets.flatMap { printPlanetRow(it) }
                )
                .append("                             PRESS <U> TO CLOSE                               ")
                .toList()


fun printPlanetRow(
        row: PlanetsViewRow
) =
        mutableListOf(" NAME: ${row.title.leftField(70)} ")
                .append(
                        " NAME: ${row.planetType.name.leftField(29)} RESOURCES: ${row.resourceMultiplier.name.leftField(29)} ",
                        " HABITABLE: ${( if (row.planetType.habitable) "TRUE" else "FALSE").leftField(24)} MAX BUILDINGS: ${( if (row.planetType.habitable) "${row.planetType.maxBuildings}" else "-").leftField(24)} ",
                        "------------------------------------------------------------------------------"
                )
                .append("                             PRESS <U> TO CLOSE                               ")
                .toList()
