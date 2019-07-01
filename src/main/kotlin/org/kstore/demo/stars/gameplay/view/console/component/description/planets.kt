package org.kstore.demo.stars.gameplay.view.console.component.description

import org.kstore.demo.stars.rule.starsystem.ResourceMultiplier

fun printStarDescription(starName: String) = listOf(
        "  .            .                ",
        " STAR: ${starName.padEnd(24, ' ')} ",
        "                      .         ",
        " *       .            .         ",
        "     .    .                     ",
        "****              .  .    .     ",
        "**********   .                  ",
        "*************  .        .   *   ",
        "*****************               ",
        "*******************  .          ",
        "*********************       .   ",
        "*********************   .       ",
        "*********************           ",
        "********************            ",
        "******************     .        ",
        "***************                 ",
        "************                    ",
        "*******    .          .         ",
        "**  *             *          .  ",
        "                                ",
        "           .          .         ",
        "        .                    .  ",
        "           .       .     *      ",
        "                 *              ",
        "    .                     .     ",
        "          .              .      "
)


fun printPlanetDescription(planetName: String, planetMinerals: String) = listOf(
        "  .            ______           ",
        " PLANET: ${planetName.padEnd(22, ' ')} ",
        "          .' ::: .:    '.       ",
        " *       /   :::::'      \\      ",
        "     .  ;.    ':::`` `    ;     ",
        "        |      `'::..::;  |     ",
        " *      \\ '   .;::::::`   /     ",
        " SIZE: NORMAL :;`'::::   /  *   ",
        " MINERALS: ${planetMinerals.padEnd(20, ' ')} "
)

fun printSmallPlanetDescription(planetName: String, planetMinerals: String) = listOf(
        "  .            .                ",
        " PLANET: ${planetName.padEnd(22, ' ')} ",
        "                      .         ",
        " *       .    .----.  .         ",
        "     .    . .\"`::; \" .          ",
        "             :, \":  :     .     ",
        " *           '.::...'           ",
        " SIZE: SMALL   .        .   *   ",
        " MINERALS: ${planetMinerals.padEnd(20, ' ')} "
)

fun printGasGiantDescription(planetName: String) = listOf(
        "  .            .                ",
        " PLANET: ${planetName.padEnd(22, ' ')} ",
        "           ,MMM8&&&.            ",
        " *        MMMMM88&&&&           ",
        "     .   MMMMM88&&&&&&:         ",
        "         MMMMM88&&&&&&    .     ",
        " *       MMMMM88&&&&&&          ",
        "   .   .  MMMMM88&&&&   .   *   ",
        " SIZE: GAS GIANT (INHABITABLE)  "
)

fun printRingedGasGiantDescription(planetName: String) = listOf(
        "  .            .         .::.   ",
        " PLANET: ${planetName.padEnd(22, ' ')} ",
        "           ,MMM8&&&.:'   .:'    ",
        " *        MMMMM88&&&&  .:'      ",
        "     .   MMMMM88&&&&&&:'        ",
        "         MMMMM88&&&&&&    .     ",
        " *     .:MMMMM88&&&&&&          ",
        "   . .:'  MMMMM88&&&&   .   *   ",
        " SIZE: GAS GIANT (INHABITABLE)  "
)


fun printMoonDescription(planetName: String, planetMinerals: String) = listOf(
        "  .            .                ",
        " MOON: ${planetName.padEnd(24, ' ')} ",
        "                      .         ",
        " *       .    _       .         ",
        "     .    .  (_)                ",
        "                  .  .    .     ",
        " *           .                  ",
        "   .   .       .        .   *   ",
        " MINERALS: ${planetMinerals.padEnd(20, ' ')} "
)

fun printMinerals(resourceMultiplier: ResourceMultiplier) = when (resourceMultiplier) {
    ResourceMultiplier.DEPLETED -> "DEPLETED (x0.1)"
    ResourceMultiplier.POOR -> "POOR (x0.5)"
    ResourceMultiplier.NORMAL -> "NORMAL"
    ResourceMultiplier.RICH -> "RICH (x2)"
    ResourceMultiplier.VERY_RICH -> "VERY RICH (x3)"
}
