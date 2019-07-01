package org.kstore.demo.stars.gameplay.view.active

import org.kstore.demo.stars.gameplay.model.turn.EndOfPlayerTurnStageAction
import org.kstore.demo.stars.rule.PlayerTurnStageType
import org.springframework.stereotype.Service
import react.kstore.action.*

class EndHumanPlayerTurn

@Service
class EntOfTurnReaction {

    init {
        CommonDispatcher.subscribe(EndHumanPlayerTurn::class) {
            action(EndOfPlayerTurnStageAction(PlayerTurnStageType.MOVEMENT))
        }
    }

}
