package org.kstore.demo.stars.gameplay.model.player.resources.income

typealias Fuel = Int

fun Fuel.print(): String = (this.toDouble() / 10).toString()

data class Income(
        val resources: Int = 0,
        val fuel: Fuel = 0,
        val researchPoints: Int = 0
)
