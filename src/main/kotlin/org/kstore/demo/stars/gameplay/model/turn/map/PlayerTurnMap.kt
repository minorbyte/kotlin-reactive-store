package org.kstore.demo.stars.gameplay.model.turn.map

import org.kstore.demo.stars.common.*

data class PlayerTurnMap(
        val tiles: Map<Position, Tile> = mapOf(),
        val size: Area = Area(1, 1)
)
