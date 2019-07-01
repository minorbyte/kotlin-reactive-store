package org.kstore.demo.stars.gameplay.view.console.component.colony.summary

import org.kstore.demo.stars.common.*
import org.kstore.demo.stars.gameplay.view.active.colony.IncomeView

const val EMPTY_SUMMARY_LINE = "                                      "
fun emptySummary() = listOf<String>().pad(7, EMPTY_SUMMARY_LINE)

fun printSummaryComponent(
        planetName: String,
        planetSize: String,
        planetMinerals: String,
        income: IncomeView
) = listOf(
        " PLANET: ${planetName.rightField(30)} ",
        " SIZE: ${planetSize.rightField(32)} ",
        " MINERALS: ${planetMinerals.rightField(28)} ",
        "--------------- INCOME -----------------",
        " MINERALS: ${("+" + income.resources).rightField(28)} ",
        " FUEL: ${("+" + income.fuel / 10.0).rightField(32)} ",
        " RESEARCH: ${("+" + income.researchPoints).rightField(25)} SP "
)

fun printShortSummaryComponent(
        planetName: String,
        planetSize: String,
        planetMinerals: String,
        colonyPlayer: String,
        colonyIncome: IncomeView
) = listOf(
        " PLANET: ${planetName.rightField(22)} ",
        " SIZE: ${planetSize.rightField(24)} ",
        " MINERALS: ${planetMinerals.rightField(20)} ",
        "--------------------------------",
        " COLONY: ${colonyPlayer.rightField(22)} ",
        " MINERALS: ${("+" + colonyIncome.resources).rightField(20)} ",
        " FUEL: ${("+" + colonyIncome.fuel / 10.0).rightField(24)} ",
        " RESEARCH: ${("+" + colonyIncome.researchPoints).rightField(17)} SP ",
        "       PRESS <I> TO COLONY VIEW "
)


fun printEnemyShortSummaryComponent(
        planetName: String,
        planetSize: String,
        planetMinerals: String,
        colonyPlayer: String
) = listOf(
        " PLANET: ${planetName.leftField(22)} ",
        " SIZE: ${planetSize.leftField(24)} ",
        " MINERALS: ${planetMinerals.leftField(20)} ",
        "--------------------------------",
        " COLONY: ${colonyPlayer.rightField(22)} ",
        "                                ",
        "          ENEMY COLONY          ",
        "                                ",
        "                                "
)
