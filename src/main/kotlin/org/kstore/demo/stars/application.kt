package org.kstore.demo.stars

import jline.console.ConsoleReader
import org.kstore.demo.stars.gameplay.GameplayModule
import org.kstore.demo.stars.menu.MenuModule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.*
import java.util.concurrent.CountDownLatch
import javax.annotation.PostConstruct


@Configuration
class Application {

    @Bean
    fun consoleReader() = ConsoleReader(System.`in`, System.out)

    @Bean
    fun uiRenderService(consoleReader: ConsoleReader) = UIRenderService(consoleReader)

    @Bean
    fun closeLatch() = CountDownLatch(1)

    @Bean
    fun errorConsumer() = ErrorActionLogger()

    @Bean
    fun exitConsumer(consoleReader: ConsoleReader, applicationContext: ApplicationContext) = ExitByQKeyProcessor(consoleReader, applicationContext)

    @Bean
    fun uiInputService(consoleReader: ConsoleReader) = UIInputService(consoleReader)

    @Bean
    fun menuModule(applicationContext: ApplicationContext) = MenuModule(applicationContext)

    @Bean
    fun gameplayModule(applicationContext: ApplicationContext) = GameplayModule(applicationContext)

    @Bean
    fun startGameProcessor(
            gameplayModule: GameplayModule,
            menuModule: MenuModule
    ) = StartGameProcessor(gameplayModule, menuModule)

    @Bean
    fun exitToMenuProcessor(
            gameplayModule: GameplayModule,
            menuModule: MenuModule
    ) = ExitToMenuProcessor(gameplayModule, menuModule)

    @Autowired
    lateinit var menuModule: MenuModule

    @PostConstruct
    fun init() {
        menuModule.start()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val context = SpringApplication.run(Application::class.java, *args)
            context.registerShutdownHook()

            val closeLatch = context.getBean(CountDownLatch::class.java)
            Runtime.getRuntime().addShutdownHook(object : Thread() {
                override fun run() {
                    closeLatch.countDown()
                }
            })
            closeLatch.await()
        }
    }
}


