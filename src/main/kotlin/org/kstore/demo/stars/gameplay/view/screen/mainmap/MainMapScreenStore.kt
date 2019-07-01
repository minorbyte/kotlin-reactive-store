package org.kstore.demo.stars.gameplay.view.screen.mainmap

import org.kstore.demo.stars.gameplay.view.console.component.description.UITileDescriptionStore
import org.kstore.demo.stars.gameplay.view.console.component.mainmap.PrintedGameMapStore
import org.kstore.demo.stars.gameplay.view.screen.BasicScreenStore
import org.springframework.stereotype.Service
import react.kstore.dependency.dependsOn

@Service
class MainMapScreenStore(
        gameMapComponentStore: PrintedGameMapStore,
        uiTileDescriptionStore: UITileDescriptionStore
) : BasicScreenStore(
) {

    init {
        dependsOn {
            stores(
                    gameMapComponentStore,
                    uiTileDescriptionStore
            ) {
                rewrite { gameMap, uiDescription ->
                    printMap(gameMap, uiDescription)
                }
            }
        }
    }
}
