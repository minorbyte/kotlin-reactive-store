package org.kstore.demo.stars.gameplay.view.active.tile

import org.kstore.demo.stars.gameplay.model.turn.map.*
import org.kstore.demo.stars.gameplay.view.screen.*
import org.springframework.stereotype.Service
import react.kstore.*
import react.kstore.action.asyncAction

@Service
class ActiveTileStore(
        mapStore: Store<PlayerTurnMap>,
        cursorStore: Store<Cursor>
) : BasicStore<Tile>(
        initialState = SpaceTile(),
        dependsOn = {
            stores(
                    mapStore,
                    cursorStore
            ) rewrite { map, cursor ->
                map.tiles.getOrDefault(cursor.position, SpaceTile())
            }
        },
        reactions = {
            on(EnterColonyAtCursor::class) {
                react { _, _ ->
                    asyncAction(ShowColonyViewAction())
                }
                validate("No colony at cursor") { tile, _ ->
                    tile is ColonyTile
                }
            }
            on(ShowFleetAtCursor::class) {
                react { _, _ ->
                    asyncAction(ShowShipsListAction())
                }
                validate("No fleet at cursor") { tile, _ ->
                    tile.ships.isNotEmpty()
                }
            }
        }
)
