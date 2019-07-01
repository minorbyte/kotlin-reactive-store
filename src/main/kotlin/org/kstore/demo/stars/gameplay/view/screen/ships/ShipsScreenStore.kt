package org.kstore.demo.stars.gameplay.view.screen.ships

import org.kstore.demo.stars.gameplay.view.console.component.shiplist.ShipListStore
import org.kstore.demo.stars.gameplay.view.screen.BasicScreenStore
import org.springframework.stereotype.Service
import react.kstore.dependency.dependsOn

@Service
class ShipsScreenStore(
        shipListStore: ShipListStore
) : BasicScreenStore(
) {
    init {
        dependsOn {
            stores(
                    shipListStore
            ) {
                rewrite { shipList: List<String> ->
                    printShipsScreen(shipList)
                }
            }
        }
    }
}
