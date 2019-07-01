package org.kstore.demo.stars.gameplay.view.console.component.shiplist

import org.kstore.demo.stars.gameplay.model.turn.PlayerTurnShipMovementStore
import org.kstore.demo.stars.gameplay.view.active.ship.*
import org.springframework.stereotype.Service
import react.kstore.BasicStore
import react.kstore.dependency.dependsOn


@Service
class ShipListStore(
        activeTileShipPageStore: ActiveTileShipPageStore,
        activeTileShipStore: ActiveTileShipStore,
        playerTurnShipMovementStore: PlayerTurnShipMovementStore
) : BasicStore<List<String>>(
        emptyList()
) {

    init {
        dependsOn {
            stores(
                    activeTileShipPageStore,
                    activeTileShipStore,
                    playerTurnShipMovementStore
            ) {
                rewrite { shipPage, selectedShip, movements ->
                    printShipsScreen(shipPage, selectedShip, movements.mapValues { it.value.toString() })
                }
            }
        }
    }

}
