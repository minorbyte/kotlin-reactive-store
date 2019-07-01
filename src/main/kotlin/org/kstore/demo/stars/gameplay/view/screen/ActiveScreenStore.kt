package org.kstore.demo.stars.gameplay.view.screen

import org.kstore.demo.stars.gameplay.view.active.ship.*
import org.kstore.demo.stars.gameplay.view.screen.Screen.*
import org.springframework.stereotype.Service
import react.kstore.BasicStore
import react.kstore.action.action
import react.kstore.reaction.on

@Service
class ActiveScreenStore : BasicStore<Screen>(
        MAIN_MAP
) {
    init {
        on(ShowMainMapAction::class) update { _, _ ->
            MAIN_MAP
        }
        on(ShowShipsListAction::class) update { _, _ ->
            action(SelectShipPageInListAction(0))
            action(SelectShipInListAction(0))

            SHIPS
        }
        on(ShowColonyViewAction::class) update { _, _ ->
            COLONY_VIEW
        }
        on(ShowKeyInfoAction::class) update { _, _ ->
            KEY_INFO
        }
//        on(ShowPlanetsAction::class) update { _, _ ->
//            PLANETS
//        }
        on(ShowTechAction::class) update { _, _ ->
            TECH
        }
        on(ShowMenuAction::class) update { _, _ ->
            MENU
        }
    }
}
