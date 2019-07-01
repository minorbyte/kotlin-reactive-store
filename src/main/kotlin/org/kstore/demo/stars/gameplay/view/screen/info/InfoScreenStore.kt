package org.kstore.demo.stars.gameplay.view.screen.info

import org.springframework.stereotype.Service
import react.kstore.BasicStore

//https://github.com/oleksiyp/mockk

val mainMapKeyBindings = mapOf(
        "W" to "MOVE UP",
        "A" to "MOVE LEFT",
        "S" to "MOVE DOWN",
        "D" to "MOVE RIGHT",
        "E" to "SELECT NEXT SHIP",
        "B" to "BUILD COLONY",
        "I" to "COLONY INFO",
        "TAB" to "SELECT CURSOR",
        "N" to "NEXT TURN"
)

@Service
class InfoScreenStore : BasicStore<List<String>>(
        initialState = printInfo(mainMapKeyBindings)
)
