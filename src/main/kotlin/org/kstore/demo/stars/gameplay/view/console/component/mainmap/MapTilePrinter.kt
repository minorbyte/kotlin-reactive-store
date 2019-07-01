package org.kstore.demo.stars.gameplay.view.console.component.mainmap

import org.kstore.demo.stars.common.*
import org.kstore.demo.stars.common.ConsoleColor.WHITE


internal object MapTilePrinter : (MutableList<MutableList<String>>, Position, TileChar) -> MutableList<MutableList<String>> {

    override fun invoke(map: MutableList<MutableList<String>>, position: Position, value: TileChar): MutableList<MutableList<String>> {
        var x = position.x * 3

        map[position.y][x++] = printOuterChar('[', value.outerColor, value.bold)
        map[position.y][x++] = printInnerChar(value.char, value.innerColor, value.bold)
        map[position.y][x] = printOuterChar(']', value.outerColor, value.bold)
        return map
    }

    private fun printInnerChar(
            char: Char,
            color: ConsoleColor,
            bold: Boolean = false
    ): String {
        val decoration = if (bold) ";1" else ""

        return if (color == WHITE && !bold) char.toString()
        else "\u001B[3${color.colorNumber}${decoration}m$char\u001B[0m"
    }

    private fun printOuterChar(
            char: Char,
            color: ConsoleColor,
            bold: Boolean = false
    ): String {
        val decoration = if (bold) ";1" else ""

        return if (color == WHITE && !bold) char.toString()
        else "\u001B[3${color.colorNumber}${decoration}m$char\u001B[0m"
    }
}
