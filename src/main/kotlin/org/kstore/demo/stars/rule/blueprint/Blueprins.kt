package org.kstore.demo.stars.rule.blueprint

import org.kstore.demo.stars.rule.ship.ShipType
import java.util.*

typealias BlueprintId = String

const val SHIP_YARD_LEVEL3_ID = "SHIPYARD LEVEL 3"
const val SHIP_YARD_LEVEL2_ID = "SHIPYARD LEVEL 2"
const val SHIP_YARD_LEVEL1_ID = "SHIPYARD LEVEL 1"

const val MINE_ID = "MINE"
const val REFINERY_ID = "REFINERY"
const val LABORATORY_ID = "LABORATORY"

const val MINE_INCOME = 100
const val REFINERY_INCOME = 1
const val LABORATORY_INCOME = 5

object Blueprints : Map<BlueprintId, Blueprint> by listOf(
        BuildingBlueprint(
                blueprintId = MINE_ID,
                name = MINE_ID,
                cost = 100,
                turnsTotal = 1
        ),
        BuildingBlueprint(
                blueprintId = REFINERY_ID,
                name = REFINERY_ID,
                cost = 500,
                turnsTotal = 2
        ),
        BuildingBlueprint(
                blueprintId = SHIP_YARD_LEVEL1_ID,
                name = SHIP_YARD_LEVEL1_ID,
                cost = 100,
                turnsTotal = 2,
                single = true
        ),
        BuildingBlueprint(
                blueprintId = SHIP_YARD_LEVEL2_ID,
                name = SHIP_YARD_LEVEL2_ID,
                cost = 100,
                turnsTotal = 3,
                prerequisite = Optional.of(SHIP_YARD_LEVEL1_ID),
                hides = Optional.of(SHIP_YARD_LEVEL1_ID),
                single = true
        ),
        BuildingBlueprint(
                blueprintId = SHIP_YARD_LEVEL3_ID,
                name = SHIP_YARD_LEVEL3_ID,
                cost = 100,
                turnsTotal = 5,
                prerequisite = Optional.of(SHIP_YARD_LEVEL2_ID),
                hides = Optional.of(SHIP_YARD_LEVEL2_ID),
                single = true
        ),
        BuildingBlueprint(
                blueprintId = LABORATORY_ID,
                name = LABORATORY_ID,
                cost = 400,
                turnsTotal = 2
        ),
        ShipBlueprint(
                blueprintId = "FIGHTER",
                name = "FIGHTER",
                turnsTotal = 1,
                prerequisite = Optional.of(SHIP_YARD_LEVEL1_ID),
                shipType = ShipType.FIGHTER
        ),
        ShipBlueprint(
                blueprintId = "SCOUT",
                name = "SCOUT",
                turnsTotal = 1,
                prerequisite = Optional.of(SHIP_YARD_LEVEL1_ID),
                shipType = ShipType.SCOUT
        ),
        ShipBlueprint(
                blueprintId = "CORVETTE",
                name = "CORVETTE",
                turnsTotal = 1,
                prerequisite = Optional.of(SHIP_YARD_LEVEL1_ID),
                shipType = ShipType.CORVETTE
        ),
        ShipBlueprint(
                blueprintId = "FRIGATE",
                name = "FRIGATE",
                turnsTotal = 2,
                prerequisite = Optional.of(SHIP_YARD_LEVEL2_ID),
                shipType = ShipType.FRIGATE
        ),
        ShipBlueprint(
                blueprintId = "CAPITAL SHIP",
                name = "CAPITAL SHIP",
                turnsTotal = 3,
                prerequisite = Optional.of(SHIP_YARD_LEVEL3_ID),
                shipType = ShipType.CAPITAL
        )
).associateBy({ it.blueprintId })
