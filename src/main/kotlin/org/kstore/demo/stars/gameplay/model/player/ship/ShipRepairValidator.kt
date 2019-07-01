package org.kstore.demo.stars.gameplay.model.player.ship

import org.kstore.demo.stars.gameplay.model.player.PlayerId
import org.kstore.demo.stars.gameplay.model.player.colony.*
import org.kstore.demo.stars.rule.blueprint.*
import react.kstore.action.Dispatcher
import react.kstore.reaction.*

class ShipRepairValidator(
        dispatcher: Dispatcher,
        val playerShipsStore: PlayerShipsStore,
        val colonyStore: ColonyStore,
        val colonyBuildingsStore: ColonyBuildingsStore
) {

    init {
        dispatcher.intercept(ShipRepairAction::class) { action ->
            val repairAmount = repairAmount(action.playerId, action.shipId)

            if (repairAmount > 0) action.copy(amount = repairAmount)
            else FailedActionValidation(action, ValidationMessage("Repait can`t be done outside a colony with shipyard"))
        }
    }

    fun canRepair(playerId: PlayerId, shipId: ShipId): Boolean {
        return repairAmount(playerId, shipId) > 0
    }

    private fun repairAmount(playerId: PlayerId, shipId: ShipId): Int {
        val ship = playerShipsStore.state[playerId to shipId] ?: return 0

        val colony = colonyStore.state.firstOrNull { it.position == ship.position && it.playerId == ship.playerId } ?: return 0

        val colonyBuildings = colonyBuildingsStore.state[colony.id] ?: return 0

        if (colonyBuildings.any { it.blueprint.blueprintId == SHIP_YARD_LEVEL3_ID }) {
            return 200
        }

        if (colonyBuildings.any { it.blueprint.blueprintId == SHIP_YARD_LEVEL2_ID }) {
            return 100
        }

        if (colonyBuildings.any { it.blueprint.blueprintId == SHIP_YARD_LEVEL1_ID }) {
            return 50
        }

        return 0
    }

}


