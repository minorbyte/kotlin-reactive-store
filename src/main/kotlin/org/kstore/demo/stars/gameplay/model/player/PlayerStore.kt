package org.kstore.demo.stars.gameplay.model.player

import org.kstore.demo.stars.GameDescription
import react.kstore.BasicStore

class PlayerStore(
        gameDescription: GameDescription
) : BasicStore<Map<PlayerId, Player>>(
        initialState = gameDescription.players.associateBy({
            it.id
        }, {
            Player(it.id, it.moveOrder, it.ai, it.name)
        })
)
