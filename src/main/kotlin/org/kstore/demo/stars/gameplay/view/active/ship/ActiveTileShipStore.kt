package org.kstore.demo.stars.gameplay.view.active.ship

import org.kstore.demo.stars.gameplay.model.player.ship.Ship
import org.springframework.stereotype.Service
import react.kstore.reaction.on
import react.kstore.selection.ListItemStoreSelection

@Service
class ActiveTileShipStore(
        activeTileShipPageStore: ActiveTileShipPageStore
) : ListItemStoreSelection<Ship>(
        store = activeTileShipPageStore,
        selectNextActionClass = SelectNextShipInListAction::class,
        prevActionClass = SelectPrevShipInListAction::class,
        selectActionClass = SelectShipInListAction::class,
        deselectActionClass = DeselectShipInListAction::class
) {
    init {
        on(PutCursorOnSelectedShipAction::class) {
            react { state, _ ->
                state.ifPresent {
                    dispatcher.action(SelectShipByIdAction(state.get().id))
                }
            }
        }
    }
}

