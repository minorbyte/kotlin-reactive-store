package org.kstore.demo.stars.gameplay.model.player.ship

import org.kstore.demo.stars.gameplay.model.player.PlayerId
import org.kstore.demo.stars.gameplay.model.player.colony.*
import react.kstore.action.Dispatcher
import react.kstore.reaction.*

class BombColonyValidator(
        dispatcher: Dispatcher,
        private val playerShipsStore: PlayerShipsStore,
        private val colonyStore: ColonyStore
) {

    init {
        dispatcher.intercept(BombColonyAction::class) { action ->
            if (canBomb(action.playerId, action.shipId)) action
            else FailedActionValidation(action, ValidationMessage("No ship or enemy colony selected"))
        }
    }

    fun canBomb(playerId: PlayerId, shipId: ShipId): Boolean {
        val ship = playerShipsStore.state[playerId to shipId]
                ?: return false

        colonyStore.state.firstOrNull { it.position == ship.position && it.playerId != ship.playerId }
                ?: return false

        return true
    }

}


