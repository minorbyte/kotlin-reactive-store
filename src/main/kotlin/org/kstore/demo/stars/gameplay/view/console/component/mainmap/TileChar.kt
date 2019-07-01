package org.kstore.demo.stars.gameplay.view.console.component.mainmap

import org.kstore.demo.stars.common.ConsoleColor
import org.kstore.demo.stars.common.ConsoleColor.WHITE

internal data class TileChar(
        var char: Char = ' ',
        var innerColor: ConsoleColor = WHITE,
        var outerColor: ConsoleColor = WHITE,
        var bold: Boolean = false
)
