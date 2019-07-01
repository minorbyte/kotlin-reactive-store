package org.kstore.demo.stars.gameplay.view.screen.menu

import org.kstore.demo.stars.common.*
import java.util.*


fun printMenu(
        items: List<String>,
        selectedItem: Optional<String>
) =
        mutableListOf("================================= GAME MENU ==================================")
                .append(
                        listOf<String>().pad(10, "".padEnd(78))
                )
                .append(
                        items.map {
                            val result = " " + it.leftField(12) + " "

                            if (selectedItem.isPresent && selectedItem.get() == it) "\u001B[47;30m$result\u001B[0m"
                            else result
                        }.map {
                            "                           $it                                     "
                        }
                )
                .pad(26, "".padEnd(78))
//                .plus("                             PRESS <U> TO CLOSE                               ")

