package org.kstore.demo.stars.gameplay.model.player.ship

import org.kstore.demo.stars.gameplay.model.player.PlayerId
import org.kstore.demo.stars.gameplay.model.player.resources.PlayerResources
import react.kstore.Store
import react.kstore.action.*
import react.kstore.reaction.*

const val JUMP_FUEL_MULTIPLIER = 20

class ShipFuelConsumptionValidator(
        private val playerResourcesStore: Store<Map<PlayerId, PlayerResources>>,
        private val shipsStore: Store<Map<Pair<PlayerId, ShipId>, Ship>>,
        dispatcher: Dispatcher = CommonDispatcher
) {

    init {
        dispatcher.intercept(ShipMoveAction::class, { action ->
            val resources = playerResourcesStore.state[action.playerId]!!
            val ship = shipsStore.state[action.playerId to action.shipId]!!

            if (resources.fuel - ship.shipType.fuelConsumption >= 0) action
            else FailedActionValidation(action, ValidationMessage("Not enough fuel to move ship ${ship.id}"))
        })

        dispatcher.intercept(ShipJumpAction::class, { action ->
            val resources = playerResourcesStore.state[action.playerId]!!
            val ship = shipsStore.state[action.playerId to action.shipId]!!

            if (resources.fuel - ship.shipType.fuelConsumption * JUMP_FUEL_MULTIPLIER >= 0) action
            else FailedActionValidation(action, ValidationMessage("Not enough fuel to move ship ${ship.id}"))
        })
    }
}
