package org.kstore.demo.stars.gameplay.view.screen.colony

import org.kstore.demo.stars.common.append


fun printColonyScreen(
        buildings: List<String>,
        buildQueue: List<String>,
        summary: List<String>
) =
        mutableListOf("================================= COLONY VIEW ================================")
                .append(
                        summary.plus(buildings).zip(buildQueue) { left, right ->
                            "$left|$right"
                        }
                )
                .append("                             PRESS <U> TO CLOSE                               ")
                .toList()
