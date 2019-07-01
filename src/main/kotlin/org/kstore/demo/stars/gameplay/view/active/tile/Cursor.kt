package org.kstore.demo.stars.gameplay.view.active.tile

import org.kstore.demo.stars.common.Position

data class Cursor(
        val position: Position,
        val state: CursorState
)
