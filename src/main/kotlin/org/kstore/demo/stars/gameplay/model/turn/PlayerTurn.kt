package org.kstore.demo.stars.gameplay.model.turn

import org.kstore.demo.stars.gameplay.model.player.PlayerId

data class PlayerTurn(
        val turnNumber: Int,
        val playerId: PlayerId,
        val playerName: String = "Player"
)
