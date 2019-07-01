package org.kstore.demo.stars.gameplay.view.screen.mainmap

import org.kstore.demo.stars.common.append

fun printMap(gameMap: List<String>, uiDescription: List<String>) =
        mutableListOf("=================================== MAP ======================================")
                .append(
                        (0..27).map {
                            (gameMap.getOrNull(it) ?: "") + "|" + (uiDescription.getOrNull(it) ?: "")
                        }
                )





































//                        .::.
//                     .:'  .:
//          ,MMM8&&&.:'   .:'
//         MMMMM88&&&&  .:'
//        MMMMM88&&&&&&:'
//        MMMMM88&&&&&&
//      .:MMMMM88&&&&&&
//    .:'  MMMMM88&&&&
//  .:'   .:'MMM8&&&'
//  :'  .:'
//  '::'

//
//
//          ,MMM8&&&.
//         MMMMM88&&&&
//        MMMMM88&&&&&&:
//        MMMMM88&&&&&&
//        MMMMM88&&&&&&
//         MMMMM88&&&&
//          'MMM8&&&'
//
//

//               _____
//            .-'.  ':'-.
//          .' ::: .:    '.
//         /   :::::'      \
//        ;.    ':::`` `    ;
//        |      `'::..::;  |
//        ; '   .;::::::`   ;
//         \ o ::;`'::::   /
//          '.           .'
//            '-.___ _.-'


//         .-"""-.
//  _    ."       ".
// (_)   :         :
//       '.       .'
//        `'''''`
//

fun star(title: String) = arrayOf(
        "  .            .                ",
        " STAR: ${title.padEnd(24, ' ')} ",
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
        "******     .          .         "
)

fun planet(title: String, minerals: String) = arrayOf(
        "  .            ______           ",
        " PLANET: ${title.padEnd(22, ' ')} ",
        "          .' ::: .:    '.       ",
        " *       /   :::::'      \\      ",
        "     .  ;.    ':::`` `    ;     ",
        "        |      `'::..::;  |     ",
        " *      \\ '   .;::::::`   /     ",
        " SIZE: NORMAL :;`'::::   /  *   ",
        " MINERALS: ${minerals.padEnd(20, ' ')} "
)

fun smallPlanet(title: String, minerals: String) = arrayOf(
        "  .            .                ",
        " PLANET: ${title.padEnd(22, ' ')} ",
        "                      .         ",
        " *       .    .----.  .         ",
        "     .    . .\"`::; \" .          ",
        "             :, \":  :     .     ",
        " *           '.::...'           ",
        " SIZE: SMALL   .        .   *   ",
        " MINERALS: ${minerals.padEnd(20, ' ')} "
)

fun gasGiant(title: String) = arrayOf(
        "  .            .                ",
        " PLANET: ${title.padEnd(22, ' ')} ",
        "           ,MMM8&&&.            ",
        " *        MMMMM88&&&&           ",
        "     .   MMMMM88&&&&&&:         ",
        "         MMMMM88&&&&&&    .     ",
        " *       MMMMM88&&&&&&          ",
        "   .   .  MMMMM88&&&&   .   *   ",
        " SIZE: GAS GIANT (INHABITABLE)  "
)

fun gasGiantRinged(title: String) = arrayOf(
        "  .            .         .::.   ",
        " PLANET: ${title.padEnd(22, ' ')} ",
        "           ,MMM8&&&.:'   .:'    ",
        " *        MMMMM88&&&&  .:'      ",
        "     .   MMMMM88&&&&&&:'        ",
        "         MMMMM88&&&&&&    .     ",
        " *     .:MMMMM88&&&&&&          ",
        "   . .:'  MMMMM88&&&&   .   *   ",
        " SIZE: GAS GIANT (INHABITABLE)  "
)


fun moon(title: String, minerals: String) = arrayOf(
        "  .            .                ",
        " MOON: ${title.padEnd(24, ' ')} ",
        "                      .         ",
        " *       .    _       .         ",
        "     .    .  (_)                ",
        "                  .  .    .     ",
        " *           .                  ",
        "   .   .       .        .   *   ",
        "                                "
)
//
//fun colony(
//        title: String,
//        size: String,
//        minerals: String,
//        colonyPlayer: String,
//        colonyIncome: ColonyIncome
//) = arrayOf(
//        " PLANET: ${title.leftField(22)} ",
//        " SIZE: ${size.leftField(24)} ",
//        " MINERALS: ${minerals.leftField(20)} ",
//        "--------------------------------",
//        " COLONY: ${colonyPlayer.rightField(22)} ",
//        " MINERALS:                 ${("+" + colonyIncome.resources).rightField(4)} ",
//        " FUEL:                     ${("+" + colonyIncome.fuel).rightField(4)} ",
//        " RESEARCH:              ${("+" + colonyIncome.researchPoints).rightField(4)} SP ",
//        "       PRESS <I> TO COLONY VIEW "
//)
//
//fun enemyColony(
//        title: String,
//        size: String,
//        minerals: String,
//        colonyPlayer: String
//) = arrayOf(
//        " PLANET: ${title.leftField(22)} ",
//        " SIZE: ${size.leftField(24)} ",
//        " MINERALS: ${minerals.leftField(20)} ",
//        "--------------------------------",
//        " COLONY: ${colonyPlayer.rightField(22)} ",
//        "                                ",
//        "          ENEMY COLONY          ",
//        "                                ",
//        "                                "
//)
