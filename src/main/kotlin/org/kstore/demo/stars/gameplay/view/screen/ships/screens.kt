package org.kstore.demo.stars.gameplay.view.screen.ships

import org.kstore.demo.stars.common.*


fun printShipsScreen(
        shipList: List<String>
) =
        mutableListOf("================================= SHIPS ======================================")
                .append(
                        shipList.pad(25, "".padEnd(78))
                )
                .append(" PRESS: <P> TO SELECT, <U> TO CLOSE                                           ")
                .toList()
