package org.kstore.demo.stars.rule.blueprint

import org.kstore.demo.stars.rule.ship.ShipType
import java.util.*

data class ShipBlueprint(
        override val blueprintId: BlueprintId,
        override val name: String,
        override val turnsTotal: Int,
        override val prerequisite: Optional<BlueprintId> = Optional.empty(),
        val shipType: ShipType,
        override val cost: Int = shipType.cost
) : Blueprint
