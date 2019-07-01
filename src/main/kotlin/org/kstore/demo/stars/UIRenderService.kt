package org.kstore.demo.stars

import jline.console.ConsoleReader
import java.io.PrintWriter
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit.MILLISECONDS
import java.util.concurrent.atomic.AtomicBoolean
import javax.annotation.*

class UIRenderService(
        private val consoleReader: ConsoleReader
) {

    private val lock = Unit
    private val printWriter: PrintWriter = PrintWriter(consoleReader.output)
    private val wasUpdate: AtomicBoolean = AtomicBoolean(true)

    private var buffer = ""
    private val executor = Executors.newSingleThreadScheduledExecutor()

    @PostConstruct
    fun init() {
        executor.scheduleAtFixedRate({
            if (wasUpdate.getAndSet(false)) {
                synchronized(lock) {
                    consoleReader.clearScreen()
                    printWriter.println(buffer)
                    printWriter.flush()
                }
            }
        }, 0, 100, MILLISECONDS)
    }

    @PreDestroy
    fun stop() {
        executor.shutdownNow()
    }

    fun updateScreen(screen: String) {
        synchronized(lock) {
            buffer = screen
            wasUpdate.set(true)
        }
    }

}
