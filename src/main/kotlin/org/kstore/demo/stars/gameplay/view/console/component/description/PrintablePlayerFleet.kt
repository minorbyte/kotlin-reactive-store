package org.kstore.demo.stars.gameplay.view.console.component.description


data class PrintablePlayerFleet(
        val total: Int,
        val capitals: Int,
        val frigates: Int,
        val corvettes: Int,
        val fighters: Int,
        val scouts: Int,
        val power: Int,
        val playerName: String
)
