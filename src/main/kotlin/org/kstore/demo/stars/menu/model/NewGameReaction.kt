package org.kstore.demo.stars.menu.model

import jline.console.ConsoleReader
import org.kstore.demo.stars.*
import org.springframework.stereotype.Component
import react.kstore.action.*
import javax.annotation.PreDestroy

@Component
class NewGameReaction(
        maps: List<GameDescription>,
        consoleReader: ConsoleReader
) {

    private val subscription: Subscription = CommonDispatcher.subscribe(MenuStartNewGame::class) { action ->
        CommonDispatcher.asyncAction(StartGame(maps.first { it.name == action.map }))
        consoleReader.clearScreen()
        println("STARTING NEW GAME...")
        consoleReader.flush()

//        Validations:
//        "Player with same move order already exists"

//        "Player with same name already exists"
    }

    @PreDestroy
    fun destroy() {
        subscription.unsubscribe()
    }
}
