package org.kstore.demo.stars.rule.tech

import org.kstore.demo.stars.common.append
import org.kstore.demo.stars.rule.tech.TechAffects.*
import java.util.*


typealias TechId = String

const val RADAR_TECH_ID = "RADAR"
const val REPAIR_TECH_ID = "REPAIR"

object TechTree : Map<TechId, Tech> by mutableListOf(
        Tech(
                techId = RADAR_TECH_ID,
                name = "Long range planet radar",
                cost = 1500,
                multiplier = 2.0,
                affects = SPECIAL
        ),
        Tech(
                techId = REPAIR_TECH_ID,
                name = "Ship repair",
                cost = 1000,
                multiplier = 1.0,
                affects = SPECIAL
        )
).append(
        spawnTechs(
                idTemplate = "W",
                nameTemplate = "Advanced weapons ",
                baseCost = 1000,
                costIncrease = { cost: Int, level: Int -> cost + cost * level / 2 },
                baseMultiplier = 1.0,
                multiplierIncrease = { _: Double, level: Int -> 0.1 * level },
                affects = ATTACK
        )
).append(
        spawnTechs(
                idTemplate = "C",
                nameTemplate = "Advanced computer level ",
                baseCost = 800,
                costIncrease = { cost: Int, level: Int -> cost + cost * level / 2 },
                baseMultiplier = 1.0,
                multiplierIncrease = { _: Double, level: Int -> 0.1 * level },
                affects = COMPUTER
        )
).append(
        spawnTechs(
                idTemplate = "D",
                nameTemplate = "Advanced drives ",
                baseCost = 900,
                costIncrease = { cost: Int, level: Int -> cost + cost * level / 2 },
                baseMultiplier = 1.0,
                multiplierIncrease = { multiplier: Double, level: Int -> multiplier + 0.1 * level },
                affects = MANEUVERABILITY
        )
).append(
        spawnTechs(
                idTemplate = "R",
                nameTemplate = "Efficient refinery ",
                baseCost = 500,
                costIncrease = { cost: Int, level: Int -> cost + cost * level / 2 },
                baseMultiplier = 1.0,
                multiplierIncrease = { _: Double, level: Int -> 0.1 * level },
                affects = REFINERY
        )
).append(
        spawnTechs(
                idTemplate = "M",
                nameTemplate = "Robotic mines ",
                baseCost = 700,
                costIncrease = { cost: Int, level: Int -> cost + cost * level / 2 },
                baseMultiplier = 1.0,
                multiplierIncrease = { _: Double, level: Int -> 0.1 * level },
                affects = MINE
        )
)

        .associateBy({ it.techId }) {


    fun maxMultiplier(affects: TechAffects, techs: List<TechId>) = techs.fold(1.0, { acc, techId ->
        maxOf(this[techId]?.takeIf { it.affects == affects }?.multiplier ?: 1.0, acc)
    })
}

fun spawnTechs(
        idTemplate: String,
        affects: TechAffects,
        nameTemplate: String,
        baseCost: Int,
        costIncrease: (Int, Int) -> Int,
        baseMultiplier: Double,
        multiplierIncrease: (Double, Int) -> Double
) =
        (1..10).fold(listOf<Tech>()) { acc, index ->
            acc.plus(
                    Tech(
                            techId = idTemplate + index,
                            name = nameTemplate + index,
                            cost = baseCost + costIncrease(baseCost, index),
                            multiplier = baseMultiplier + multiplierIncrease(baseMultiplier, index),
                            affects = affects,
                            prerequisite = if (index == 1) Optional.empty() else Optional.of(idTemplate + (index - 1)),
                            hides = if (index == 1) Optional.empty() else Optional.of(idTemplate + (index - 1))
                    )
            )
        }

data class Tech(
        val techId: TechId,
        val affects: TechAffects,
        val name: String,
        val cost: Int,
        val multiplier: Double,
        val prerequisite: Optional<TechId> = Optional.empty(),
        val hides: Optional<TechId> = Optional.empty()
)

enum class TechAffects {
    MINE, REFINERY, ATTACK, COMPUTER, MANEUVERABILITY, SPECIAL
}
