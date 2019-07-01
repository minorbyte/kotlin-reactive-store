package org.kstore.demo.stars.gameplay.view.active

import org.kstore.demo.stars.gameplay.model.player.colony.BombColonyAction
import org.kstore.demo.stars.gameplay.view.active.colony.SelectedColonyIdViewStore
import org.kstore.demo.stars.gameplay.view.active.ship.*
import org.springframework.stereotype.Service
import react.kstore.action.*
import java.lang.Math.max


@Service
class BombColonyReaction(
        activeShipStore: ActiveShipStore,
        selectedColonyIdViewStore: SelectedColonyIdViewStore
) {
    init {
        CommonDispatcher.subscribe(BombBySelectedShipAction::class) {
            activeShipStore.state.ifPresent { ship ->
                selectedColonyIdViewStore.state.ifPresent { colonyId ->
                    asyncAction(BombColonyAction(colonyId, ship.id, ship.playerId, max(0, ship.shipType.size - 3)))
                }
            }
        }

    }
}
