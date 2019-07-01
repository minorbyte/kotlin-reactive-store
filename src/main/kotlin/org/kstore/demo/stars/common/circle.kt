package org.kstore.demo.stars.common


fun inCircle(position: Position, circlePosition: Position, radius: Int): Boolean =
        (position.x - circlePosition.x) * (position.x - circlePosition.x) + (position.y - circlePosition.y) * (position.y - circlePosition.y) <= (radius - 1) * (radius - 1)

fun inRadiusDo(position: Position, radius: Int, action: ((x: Int, y: Int) -> Unit)) {
    (-radius..radius).forEach { x ->
        (-radius..radius)
                .filter { y -> inCircle(Position(x + position.x, y + position.y), position, radius) }
                .forEach { y -> action(x + position.x, y + position.y) }
    }
}

fun inCircle(center: Position, radius: Int) =
        (-radius..radius).flatMap { x ->
            (-radius..radius)
                    .map { Position(center.x + x, center.y + it) }
                    .filter { inCircle(it, center, radius) }
        }

