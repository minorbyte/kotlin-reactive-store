package org.kstore.demo.stars.gameplay.model.turn.stage

import org.kstore.demo.stars.gameplay.model.player.PlayerId
import org.kstore.demo.stars.rule.TurnStage

data class PlayerTurnStage(
        val turnNumber: Int,
        val playerId: PlayerId,
        val playerName: String = "Player",
        val turnStage: TurnStage
)
