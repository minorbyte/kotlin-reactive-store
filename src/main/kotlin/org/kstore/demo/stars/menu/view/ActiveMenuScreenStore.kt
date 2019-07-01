package org.kstore.demo.stars.menu.view

import org.kstore.demo.stars.menu.view.MenuScreen.*
import org.springframework.stereotype.Service
import react.kstore.BasicStore
import react.kstore.reaction.on

@Service
class ActiveMenuScreenStore : BasicStore<MenuScreen>(
        MAIN_MENU
) {
    init {
        on(ShowMainMenuAction::class) update { _, _ ->
            MAIN_MENU
        }
        on(ShowLoadMenuAction::class) update { _, _ ->
            LOAD_GAME_MENU
        }
        on(ShowNewGameMenuAction::class) update { _, _ ->
            NEW_GAME_MENU
        }
    }
}
