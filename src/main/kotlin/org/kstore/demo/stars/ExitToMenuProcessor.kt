package org.kstore.demo.stars

import org.kstore.demo.stars.gameplay.GameplayModule
import org.kstore.demo.stars.menu.MenuModule
import react.kstore.action.CommonDispatcher
import javax.annotation.PostConstruct

class ExitToMenuProcessor(
        private val gameplayModule: GameplayModule,
        private val menuModule: MenuModule
) {

    @PostConstruct
    fun init() {
        CommonDispatcher.subscribe(ExitToMenu::class) {
            gameplayModule.stop()
            menuModule.start()
        }
    }
}
