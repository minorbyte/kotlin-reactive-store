package org.kstore.demo.stars.gameplay.view.console.component.colony.buildqueue

import org.kstore.demo.stars.common.*
import org.kstore.demo.stars.gameplay.view.active.colony.*
import java.util.*


const val EMPTY_BUILD_QUEUE_LINE = "                                   "

fun emptyBuildQueue() = listOf<String>().pad(26, EMPTY_BUILD_QUEUE_LINE)

fun printBuildQueueComponent(
        buildQueue: List<BuildQueueView>,
        buildAvailable: List<AvailableToBuildView>,
        buildQueueSelection: Optional<Int>
) = mutableListOf("------------ BUILD QUEUE ------------")
        .append(
                buildQueue
                        .map { "${it.displayName.leftField(31)} ${it.turnsLeft.toString().rightField(3)}" }
                        .mapIndexed { index, value ->
                            if (buildQueueSelection.orElse(-1) == index) "\u001B[47m$value\u001B[0m" else value
                        }
                        .exact(5, EMPTY_BUILD_QUEUE_LINE)
                        .map { " $it " }
        )
        .append("                       <-> TO REMOVE ")
        .append("----------- ADD TO QUEUE ------------")
        .append(" NAME                      COST TIME ")
        .append(
                buildAvailable
                        .map { "${it.displayName.leftField(24)} ${it.cost.toString().rightField(4)} ${it.turns.toString().rightField(5)}" }
                        .mapIndexed { index, value ->
                            if (buildQueueSelection.orElse(-1) - 5 == index) "\u001B[47m$value\u001B[0m" else value
                        }
                        .exact(16, EMPTY_BUILD_QUEUE_LINE)
                        .map { " $it " }
        )
        .append("                        <B> TO BUILD ")
        .toList()
