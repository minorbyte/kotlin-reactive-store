package org.kstore.demo.stars.menu.model

import com.fasterxml.jackson.databind.ObjectMapper
import jline.console.ConsoleReader
import org.kstore.demo.stars.*
import org.springframework.stereotype.Component
import react.kstore.action.*
import java.io.File
import javax.annotation.PreDestroy

@Component
class LoadGameReaction(
        mapper: ObjectMapper,
        consoleReader: ConsoleReader
) {

    private val subscription: Subscription = CommonDispatcher.subscribe(MenuLoadGame::class) {
        val gameDescription = mapper.readValue(File("game${it.index}.sav"), GameDescriptionImpl::class.java)
        CommonDispatcher.asyncAction(StartGame(gameDescription))
        consoleReader.clearScreen()
        println("LOADING GAME...")
        consoleReader.flush()
    }

    @PreDestroy
    fun destroy() {
        subscription.unsubscribe()
    }
}
