package org.kstore.demo.stars.gameplay.model.player.resources

import org.kstore.demo.stars.gameplay.model.player.PlayerId
import org.kstore.demo.stars.gameplay.model.turn.PlayerAction

data class PlayerIncomeAction(
        override val playerId: PlayerId,
        val resources: Int,
        val fuel: Int
): PlayerAction

data class PlayerResourcePaymentAction(
        override val playerId: PlayerId,
        val resources: Int
): PlayerAction

data class PlayerFuelPaymentAction(
        override val playerId: PlayerId,
        val fuel: Int
): PlayerAction

data class DoPlayerResearchAction(
        override val playerId: PlayerId,
        val researchPoints: Int
): PlayerAction
