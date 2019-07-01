package org.kstore.demo.stars.gameplay.view.screen.info

import org.kstore.demo.stars.common.pad


fun printInfo(mainMapKeyBindings: Map<String, String>) = listOf(
        "================================= KEY INFO ===================================",
        " MAIN MAP                                                                     "
)
        .plus(mainMapKeyBindings.map { "<${it.key}>".padEnd(5) + " - " + it.value.padEnd(70) })
        .pad(26, "".padEnd(78))
        .plus("                             PRESS <U> TO CLOSE                               ")
