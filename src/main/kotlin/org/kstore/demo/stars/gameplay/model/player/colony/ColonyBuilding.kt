package org.kstore.demo.stars.gameplay.model.player.colony

import org.kstore.demo.stars.rule.blueprint.BuildingBlueprint
import java.util.*

typealias BuildingId = String

data class ColonyBuilding(
        val id: BuildingId = UUID.randomUUID().toString(),
        val colonyId: ColonyId,
        val blueprint: BuildingBlueprint
)
