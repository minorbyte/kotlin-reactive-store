package org.kstore.demo.stars.gameplay.view.active.colony

data class BuildingsView(
        val buildings: List<BuildingView> = emptyList(),
        val totalCount: Int,
        val maxCount: Int
)
