package org.kstore.demo.stars.common

data class Area(
        val width: Int,
        val height: Int,
        val x: Int = 0,
        val y: Int = 0
) {
    constructor(width: Int, height: Int, position: Position) : this(width, height, position.x, position.y)

    fun contain(position: Position) =
            position.y < height + y
                    && position.x < width + x
                    && position.y >= y
                    && position.x >= x
}
