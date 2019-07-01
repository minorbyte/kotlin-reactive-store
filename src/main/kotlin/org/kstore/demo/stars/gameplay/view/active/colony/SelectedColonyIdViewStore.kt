package org.kstore.demo.stars.gameplay.view.active.colony

import org.kstore.demo.stars.gameplay.model.player.colony.*
import org.kstore.demo.stars.gameplay.model.turn.map.ColonyTile
import org.kstore.demo.stars.gameplay.view.active.tile.*
import org.springframework.stereotype.Service
import react.kstore.BasicStore
import java.util.*

@Service
class SelectedColonyIdViewStore(
        activeTileStore: ActiveTileStore
) : BasicStore<Optional<ColonyId>>(
        initialState = Optional.empty(),
        dependsOn = {
            stores(
                    activeTileStore
            ) rewrite { tile ->
                when (tile) {
                    is ColonyTile -> Optional.of(tile.colony.id)
                    else -> Optional.empty()
                }
            }
        }
)
