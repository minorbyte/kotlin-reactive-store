package org.kstore.demo.stars.gameplay.view.active.ship

import org.kstore.demo.stars.gameplay.model.player.ship.Ship
import org.kstore.demo.stars.gameplay.model.turn.map.Tile
import org.kstore.demo.stars.gameplay.view.active.tile.ActiveTileStore
import org.springframework.stereotype.Service
import react.kstore.BasicStore
import react.kstore.selection.*

@Service
class ActiveTileShipPageStore(
        activeTileStore: ActiveTileStore
) : BasicStore<List<Ship>>(
        initialState = listOf(),
        dependsOn = {
            stores(
                    ListPageStoreSelection(
                            store = BasicStore<List<Ship>>(
                                    initialState = listOf(),
                                    dependsOn = { stores(activeTileStore) rewrite { tile: Tile -> tile.ships } }
                            ),
                            pageSize = 20,
                            selectNextActionClass = SelectNextShipPageInListAction::class,
                            prevActionClass = SelectPrevShipPageInListAction::class,
                            selectActionClass = SelectShipPageInListAction::class,
                            deselectActionClass = DeselectShipPageInListAction::class
                    ).reduce { it.orElse(Paged(0, listOf())).page }
            ) rewrite {
                it
            }
        }

)

