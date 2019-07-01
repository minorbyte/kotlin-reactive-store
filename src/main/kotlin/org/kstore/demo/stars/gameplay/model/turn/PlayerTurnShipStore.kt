package org.kstore.demo.stars.gameplay.model.turn

import org.kstore.demo.stars.gameplay.model.player.PlayerId
import org.kstore.demo.stars.gameplay.model.player.ship.*
import react.kstore.*
import react.kstore.action.*

class PlayerTurnShipStore(
        playerTurnStore: Subscribable<PlayerTurn>,
        shipStore: Subscribable<Map<Pair<PlayerId, ShipId>, Ship>>,
        dispatcher: Dispatcher = CommonDispatcher
) : BasicStore<List<Ship>>(
        dispatcher = dispatcher,
        initialState = emptyList(),
        dependsOn = {
            stores(
                    playerTurnStore,
                    shipStore.reduce { it.values }
            ).rewrite { turn, ships ->
                ships.filter { it.playerId == turn.playerId }
            }
        }
)
