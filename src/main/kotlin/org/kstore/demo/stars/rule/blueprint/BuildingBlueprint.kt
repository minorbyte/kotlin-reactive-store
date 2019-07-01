package org.kstore.demo.stars.rule.blueprint

import java.util.*

data class BuildingBlueprint(
        override val blueprintId: BlueprintId,
        override val name: String,
        override val cost: Int,
        override val turnsTotal: Int,
        override val prerequisite: Optional<BlueprintId> = Optional.empty(),
        val hides: Optional<BlueprintId> = Optional.empty(),
        val single: Boolean = false
) : Blueprint
