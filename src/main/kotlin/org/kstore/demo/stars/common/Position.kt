package org.kstore.demo.stars.common

data class Position(
        val x: Int,
        val y: Int
) {

    fun isInRadiusOf(position: Position, radius: Int): Boolean {
        return inCircle(this, position, radius + 1)
    }

}
