package org.kstore.demo.stars.gameplay.view.console.component.colony.buildings

import org.kstore.demo.stars.common.*
import org.kstore.demo.stars.gameplay.view.active.colony.BuildingsView
import java.util.*


const val EMPTY_BUILDING_LINE = "                                      "

fun emptyBuildings() = listOf<String>().pad(17, EMPTY_BUILDING_LINE)

fun printBuildingsComponent(
        buildings: BuildingsView,
        buildingSelectionIndex: Optional<Int>
) = mutableListOf(
        "-------------- BUILDINGS ${(buildings.totalCount.toString().take(2) + "/" + buildings.maxCount.toString().take(2)).rightField(5)}----------"
).append(
        buildings
                .buildings
                .map {
                    "${it.displayName.leftField(30)} ${it.count.toString().rightField(7)}"
                }
                .mapIndexed { index, value ->
                    if (buildingSelectionIndex.orElse(-1) == index) "\u001B[47m$value\u001B[0m" else value
                }
                .exact(17, EMPTY_BUILDING_LINE)
                .map { " $it " }
).toList()

