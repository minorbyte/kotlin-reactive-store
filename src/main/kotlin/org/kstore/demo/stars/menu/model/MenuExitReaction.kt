package org.kstore.demo.stars.menu.model

import jline.console.ConsoleReader
import org.springframework.stereotype.Component
import react.kstore.action.*
import javax.annotation.PreDestroy

@Component
class MenuExitReaction(
        private val consoleReader: ConsoleReader
) {

    private val subscription: Subscription = CommonDispatcher.subscribe(MenuExit::class) {
        consoleReader.clearScreen()
        consoleReader.flush()
        System.exit(0)
    }

    @PreDestroy
    fun destroy() {
        subscription.unsubscribe()
    }
}
