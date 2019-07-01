package org.kstore.demo.stars.gameplay.view.active.colony

import org.kstore.demo.stars.gameplay.model.player.colony.BuildColonyAction
import org.kstore.demo.stars.gameplay.model.turn.map.PlanetTile
import org.kstore.demo.stars.gameplay.view.active.ship.ActiveShipStore
import org.kstore.demo.stars.gameplay.view.active.tile.ActiveTileStore
import org.springframework.stereotype.Service
import react.kstore.action.*

@Service
class ColonyBuildReaction(
        activeTileStore: ActiveTileStore,
        activeShipStore: ActiveShipStore
) {

    init {
        CommonDispatcher.subscribe(BuildColonyAtCursor::class) {
            val tile = activeTileStore.state

            activeShipStore.state.ifPresent { ship ->
                if (tile is PlanetTile) {
                    asyncAction(BuildColonyAction(
                            tile.planet.id,
                            tile.planet,
                            ship.playerId,
                            ship.id,
                            tile.planet.id
                    ))
                }
            }
        }
    }

}
