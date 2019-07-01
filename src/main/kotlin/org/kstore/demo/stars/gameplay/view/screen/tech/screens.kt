package org.kstore.demo.stars.gameplay.view.screen.tech

import org.kstore.demo.stars.common.*


fun printTechScreen(
        researchedTechs: List<ResearchedTechViewItem>,
        researcheableTechs: List<ResearcheableTechViewItem>
) =
        mutableListOf("================================= TECHNOLOGIES ===============================")
                .append("         RESEARCHED               |            AVAILABLE TO RESEARCH          ")
                .append(
                        researchedTechs
                                .map {
                                    " ${it.name.leftField(20)} ${it.affects.toString().rightField(11)} "
                                }
                                .pad(23, "".padEnd(34))
                                .zip(
                                        researcheableTechs
                                                .map { tech ->
                                                    (
                                                            " ${tech.name.leftField(20)} ${tech.affects.toString().rightField(9)} " +
                                                                    if (tech.alreadySpent > 0) "${tech.alreadySpent.toString().rightField(4)}/${tech.cost.toString().leftField(4)}"
                                                                    else "     ${tech.cost.toString().leftField(4)}"
                                                            )
                                                            .let {
                                                                if (tech.researched) "$it<-" else "$it  "
                                                            }
                                                            .let {
                                                                if (tech.selected) "\u001B[47;30m$it\u001B[0m"
                                                                else it
                                                            }
                                                }
                                                .pad(23, "".padEnd(43))
                                ) { left, right ->
                                    "$left|$right"
                                }
                )
                .append("------------------------------------------------------------------------------")
                .append("                             PRESS <U> TO CLOSE                               ")
                .toList()
