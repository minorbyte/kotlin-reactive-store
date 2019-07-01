package org.kstore.demo.stars.gameplay.view.active.colony

import org.kstore.demo.stars.gameplay.model.player.colony.BuildingId

data class BuildingView(
        val id: BuildingId,
        val count: Int,
        val displayName: String
)
