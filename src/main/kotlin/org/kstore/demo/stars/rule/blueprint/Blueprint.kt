package org.kstore.demo.stars.rule.blueprint

import java.util.*

interface Blueprint {
    val blueprintId: BlueprintId
    val name: String
    val cost: Int
    val turnsTotal: Int
    val prerequisite: Optional<BlueprintId>
}
