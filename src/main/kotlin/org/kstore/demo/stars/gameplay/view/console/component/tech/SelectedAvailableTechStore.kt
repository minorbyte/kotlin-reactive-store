package org.kstore.demo.stars.gameplay.view.console.component.tech

import org.kstore.demo.stars.gameplay.model.player.tech.ResearchTechAction
import org.kstore.demo.stars.gameplay.model.turn.PlayerTurnStore
import org.kstore.demo.stars.rule.tech.Tech
import org.springframework.stereotype.Service
import react.kstore.reaction.on
import react.kstore.selection.ListItemStoreSelection

@Service
class SelectedAvailableTechStore(
        playerTurnStore: PlayerTurnStore,
        activePlayerAvailableTechStore: ActivePlayerAvailableTechStore
) : ListItemStoreSelection<Tech>(
        store = activePlayerAvailableTechStore,
        selectNextActionClass = SelectNextTech::class,
        prevActionClass = SelectPrevTech::class
) {
    init {
        on(ResearchSelectedAction::class) react { state, action ->
            state.ifPresent {
                dispatcher.action(ResearchTechAction(playerTurnStore.state.playerId, it.techId))
            }
        }
    }
}


