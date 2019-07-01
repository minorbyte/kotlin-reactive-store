package org.kstore.demo.stars.gameplay.view.active.colony

import org.kstore.demo.stars.rule.blueprint.BlueprintId

data class AvailableToBuildView(
        val id: BlueprintId,
        val displayName: String,
        val turns: Int,
        val cost: Int
)
