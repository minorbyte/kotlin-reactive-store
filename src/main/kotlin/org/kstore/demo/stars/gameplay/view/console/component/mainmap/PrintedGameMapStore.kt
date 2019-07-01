package org.kstore.demo.stars.gameplay.view.console.component.mainmap

import org.kstore.demo.stars.common.ConsoleColor.BLUE
import org.kstore.demo.stars.common.mapOne
import org.kstore.demo.stars.gameplay.model.turn.PlayerTurn
import org.kstore.demo.stars.gameplay.model.turn.map.PlayerTurnMap
import org.kstore.demo.stars.gameplay.view.active.tile.Cursor
import org.springframework.stereotype.Service
import react.kstore.*
import react.kstore.dependency.dependsOn

@Service
class PrintedGameMapStore(
        visibleMapStore: Store<PlayerTurnMap>,
        playerTurnStore: Store<PlayerTurn>,
        gameMapCursorStore: Store<Cursor>
) : BasicStore<List<String>>(
        emptyMainMap().map { it.joinToString("") }
) {

    init {
        dependsOn {
            stores(
                    visibleMapStore,
                    gameMapCursorStore,
                    playerTurnStore
            ) {
                rewrite { gameMap, cursor, playerTurn ->
                    gameMap.tiles
                            .filter { gameMap.size.contain(it.key) }
                            .mapValues { it ->
                                MapTileBuilder(it.value, playerTurn.playerId)
                            }
                            .mapOne(cursor.position, TileChar()) {
                                it.copy(outerColor = BLUE)
                            }
                            .entries
                            .fold(emptyMainMap()) { acc, it ->
                                MapTilePrinter(acc, it.key, it.value)
                            }.map { it.joinToString("") }
                }
            }
        }
    }

}


