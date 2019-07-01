package org.kstore.demo.stars.gameplay.view.console.component.panel

import org.kstore.demo.stars.gameplay.model.turn.*
import org.springframework.stereotype.Service
import react.kstore.BasicStore
import react.kstore.dependency.dependsOn

@Service
class ScreenLowerPanelComponentStore(
        currentTurnStore: TurnStore
) : BasicStore<String>(
        initialState = ""
) {
    init {
        dependsOn {
            stores(currentTurnStore) {
                rewrite { turn: Turn -> printBottomPanel(turn.number.toString()) }
            }
        }
    }
}
