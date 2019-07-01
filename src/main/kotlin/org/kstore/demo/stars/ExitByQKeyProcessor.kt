package org.kstore.demo.stars

import jline.console.ConsoleReader
import org.springframework.boot.*
import org.springframework.context.ApplicationContext
import react.kstore.action.CommonDispatcher
import javax.annotation.PostConstruct


class ExitByQKeyProcessor(
        private val consoleReader: ConsoleReader,
        private val applicationContext: ApplicationContext
) {

    @PostConstruct
    fun init() {
        CommonDispatcher.subscribe(KeyPressAction::class) {
            if (it.char == 'q') {
                consoleReader.clearScreen()
                consoleReader.flush()
                System.exit(SpringApplication.exit(applicationContext, ExitCodeGenerator { 0 }))
            }
        }
    }
}
