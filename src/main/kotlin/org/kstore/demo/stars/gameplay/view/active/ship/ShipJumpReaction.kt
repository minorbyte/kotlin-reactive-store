package org.kstore.demo.stars.gameplay.view.active.ship

import org.kstore.demo.stars.gameplay.model.player.ship.ShipJumpAction
import org.kstore.demo.stars.gameplay.view.active.tile.*
import org.kstore.demo.stars.gameplay.view.active.tile.CursorState.JUMP
import org.springframework.stereotype.Service
import react.kstore.action.CommonDispatcher.subscribe
import react.kstore.action.action


@Service
class ShipJumpReaction(
        activeShipStore: ActiveShipStore,
        cursorStore: CursorStore
) {
    init {
        subscribe(ActiveShipJumpAction::class) {
            activeShipStore.state.ifPresent { ship ->
                when (cursorStore.state.state) {
                    JUMP -> action(ShipJumpAction(ship.playerId, ship.id, cursorStore.state.position))
                    else -> action(CursorSetShipJumpAction())
                }
            }
        }
    }

}
