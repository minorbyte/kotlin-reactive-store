package org.kstore.demo.stars.menu

import mu.KotlinLogging
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext

class MenuModule(
        private val applicationContext: ApplicationContext
) {

    private val logger = KotlinLogging.logger(this::class.qualifiedName!!)

    private var context: AnnotationConfigApplicationContext? = null

    @Synchronized
    fun start() {
        logger.info("Module 'Menu' is starting")
        val new = AnnotationConfigApplicationContext()
        new.register(MenuConfig::class.java)
//        new.beanFactory.registerSingleton("module", this)
        new.parent = applicationContext
        new.refresh()
        context = new
    }

    @Synchronized
    fun stop() {
        logger.info("Module 'Menu' is stopping")
        context?.close()
    }

}
