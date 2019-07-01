package org.kstore.demo.stars.gameplay.view.active.colony

import org.kstore.demo.stars.gameplay.model.player.colony.BuildingId
import org.kstore.demo.stars.rule.blueprint.BlueprintId


data class DemolishBuildingAction(
        val buildingId: BuildingId
)

data class AddToBuildQueueAction(
        val blueprintId: BlueprintId
)

data class RemoveFromBuildQueueAction(
        val index: Int
)

class BuildColonyAtCursor
