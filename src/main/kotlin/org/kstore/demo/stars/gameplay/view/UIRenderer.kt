package org.kstore.demo.stars.gameplay.view

import jline.console.ConsoleReader
import org.kstore.demo.stars.gameplay.view.screen.VisibleScreenStore
import java.io.PrintWriter
import java.util.concurrent.atomic.AtomicBoolean
import javax.annotation.PostConstruct

//@Component
class UIRenderer(
        val consoleReader: ConsoleReader,
        val visibleScreenStore: VisibleScreenStore
) {
    private val printWriter: PrintWriter = PrintWriter(consoleReader.output)
    private val wasUpdate: AtomicBoolean = AtomicBoolean(true)

    private var output = ""

    @PostConstruct
    fun init() {
        visibleScreenStore.subscribe {
            output = it
            wasUpdate.set(true)
        }
    }

    fun render() {
        if (wasUpdate.get()) {
            wasUpdate.set(false)
            consoleReader.clearScreen()
            printWriter.println(output)
            printWriter.flush()
        }
    }
}
