package org.kstore.demo.stars.gameplay.model.player.colony

import org.kstore.demo.stars.rule.blueprint.Blueprint

data class ColonyBuildQueue(
        val items: List<ColonyBuildQueueItem> = listOf()
)

data class ColonyBuildQueueItem(
        val id: ColonyId,
        val blueprint: Blueprint,
        val turnsLeft: Int
)
