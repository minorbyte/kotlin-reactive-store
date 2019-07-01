package org.kstore.demo.stars.menu.view

import org.kstore.demo.stars.UIRenderService
import org.kstore.demo.stars.common.append
import org.kstore.demo.stars.menu.view.MenuScreen.*
import org.springframework.stereotype.Service
import react.kstore.BasicStore
import react.kstore.dependency.dependsOn


@Service
class VisibleScreenStore(
        uiRenderService: UIRenderService,
        activeMenuScreenStore: ActiveMenuScreenStore,
        mainMenuScreenStore: MainGameMenuScreenStore,
        newGameMenuScreenStore: NewGameGameMenuScreenStore,
        loadGameMenuScreenStore: LoadGameMenuScreenStore
) : BasicStore<String>(
        ""
) {

    init {
        dependsOn {
            stores(
                    activeMenuScreenStore,
                    mainMenuScreenStore,
                    newGameMenuScreenStore,
                    loadGameMenuScreenStore
            ) {
                rewrite { screen,
                          mainMenuScreen,
                          newGameMenuScreen,
                          loadGameMenuScreen ->

                    mutableListOf("================================= GAME MENU ==================================")
                            .append(
                                    when (screen) {
                                        MAIN_MENU -> mainMenuScreen
                                        LOAD_GAME_MENU -> loadGameMenuScreen
                                        NEW_GAME_MENU -> newGameMenuScreen
                                    }
                            )
                            .append("==============================================================================")
                            .take(27)
                            .joinToString("\n") { "|$it|" }
                }
            }
        }

        this.subscribe {
            uiRenderService.updateScreen(it)
        }
    }

//    private fun printScreen(content: String): String {
//        return join("\n", content).take(27).joinToString("\n") { "|$it|" }
//    }

}
