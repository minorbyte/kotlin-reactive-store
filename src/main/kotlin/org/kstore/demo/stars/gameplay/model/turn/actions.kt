package org.kstore.demo.stars.gameplay.model.turn

import org.kstore.demo.stars.gameplay.model.player.PlayerId
import org.kstore.demo.stars.rule.TurnStage


interface PlayerAction {
    val playerId: PlayerId
}

open class EndOfTurnAction
open class EndOfPlayerTurnAction
open class EndOfPlayerTurnStageAction(val turnStage: TurnStage)
open class StartPlayerTurnStageAction(val turnStage: TurnStage)
