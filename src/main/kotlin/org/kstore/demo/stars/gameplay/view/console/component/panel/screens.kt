package org.kstore.demo.stars.gameplay.view.console.component.panel

import org.kstore.demo.stars.common.*


fun printBottomPanel(turnNumber: String) = "" +
        "|------------------------------------------------------------------------------|\n" +
        "| <O> - KEY INFO |::::::::::::::::::::::::::::::::::| TURN ${turnNumber.rightField(3)} | <N> NEXT TURN |"

fun printTopPanel(playerName: String, fuel: String, fuelPlus: String, resources: String, resourcesPlus: String, research: String) =
        "| ${playerName.leftField(32)} | FUEL ${fuel.rightField(4)} ${fuelPlus.rightField(5)} | RES ${resources.rightField(4)} " +
                "${resourcesPlus.rightField(5)} | RP ${research.rightField(3)} |"
