package org.kstore.demo.stars.gameplay.view.screen.menu

import org.kstore.demo.stars.gameplay.view.screen.BasicScreenStore
import org.springframework.stereotype.Service
import react.kstore.dependency.dependsOn
import java.util.*

@Service
class MenuScreenStore(
        gameMenuItemStore: GameMenuItemStore
) : BasicScreenStore(
) {
    init {
        val itemsList = gameMenuItemStore.items.keys.toList()

        dependsOn {
            stores(
                    gameMenuItemStore
            ) {
                rewrite { selectedItem: Optional<String> ->
                    printMenu(itemsList, selectedItem)
                }
            }
        }
    }
}
