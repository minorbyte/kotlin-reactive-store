package org.kstore.demo.stars.gameplay

import mu.KotlinLogging
import org.kstore.demo.stars.GameDescription
import org.kstore.demo.stars.gameplay.save.SaveGameConfig
import org.kstore.demo.stars.gameplay.view.screen.ShowMainMapAction
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import react.kstore.action.CommonDispatcher

class GameplayModule(
        private val applicationContext: ApplicationContext
) {

    private val logger = KotlinLogging.logger(this::class.qualifiedName!!)

    private var context: AnnotationConfigApplicationContext? = null

    @Synchronized
    fun start(gameDescription: GameDescription) {
        logger.info("Module 'Gameplay' is starting")
        val new = AnnotationConfigApplicationContext()
        new.beanFactory.registerResolvableDependency(GameDescription::class.java, gameDescription)
        new.register(GameplayConfig::class.java)
        new.register(SaveGameConfig::class.java)
        new.parent = applicationContext
        new.refresh()
        context = new

        CommonDispatcher.asyncAction(ShowMainMapAction())
    }

    @Synchronized
    fun stop() {
        logger.info("Module 'Gameplay' is stopping")
        context?.close()
    }

}
