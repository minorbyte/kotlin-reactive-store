package org.kstore.demo.stars

import jline.console.ConsoleReader
import react.kstore.action.CommonDispatcher
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.annotation.*

val allowedChars = "qwertyuiop[]\\asdfghjkl;'zxcvbnm,./ `1234567890-=\t".toCharArray()

class UIInputService(
        private val consoleReader: ConsoleReader
) {

    private val executor = Executors.newSingleThreadScheduledExecutor()

    @PostConstruct
    fun init() {
        allowedChars.forEach { char ->
            consoleReader.addTriggeredAction(char) {
                CommonDispatcher.asyncAction(KeyPressAction(char))
            }
        }
        consoleReader.addTriggeredAction('q') {
            consoleReader.clearScreen()
            consoleReader.flush()
            System.exit(0)
        }

        executor.scheduleAtFixedRate({
            consoleReader.readLine()
        }, 0, 1, MILLISECONDS)
    }

    @PreDestroy
    fun stop() {
        executor.shutdownNow()
    }

}
