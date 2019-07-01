package org.kstore.demo.stars.gameplay.model.starsystem

import org.kstore.demo.stars.common.Position

data class Star(
        val id: String,
        val title: String,
        val radius: Int = 2,
        val position: Position
)
