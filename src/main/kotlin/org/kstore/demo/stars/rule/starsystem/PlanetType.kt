package org.kstore.demo.stars.rule.starsystem

enum class PlanetType(
        val habitable: Boolean,
        val maxBuildings: Int = 0
) {
    MOON(true, 5),
    SMALL(true, 10),
    GAS_GIANT(false),
    STANDARD(true, 20),
    GAS_GIANT_RINGED(false)
}
