package org.kstore.demo.stars.rule.ship

enum class ShipType(
        val moves: Int,
        val radarRadius: Int,
        val minAttackPower: Int,
        val maxAttackPower: Int,
        val defense: Int,
        val baseComputerLevel: Int,
        val baseManeuverability: Int,
        val size: Int,
        val maxHp: Int,
        val order: Int,
        val cost: Int,
        val canColonize: Boolean,
        val canJump: Boolean,
        val fuelConsumption: Int,
        val maxCorvettes: Int,
        val maxFighters: Int
) {
     CAPITAL(3, 4, 80,100,   30, 2, 0,6,  200, 1, 1000, true,  true,  2, 3, 10),
     FRIGATE(4, 3, 50, 80,   20, 2, 0,4,  100, 2,  500, true,  true,  1, 1, 5),
    CORVETTE(4, 2, 30, 50,   10, 1, 1,3,   80, 3,  300, false, false, 0, 0, 0),
     FIGHTER(1, 1, 5,  15,    0, 1, 3,1,   50, 4,  100, false, false, 0, 0, 0),
       SCOUT(6, 6, 1,   1,    0, 1, 3,1,   10, 5,  100, false, false, 0, 0, 0)
}
