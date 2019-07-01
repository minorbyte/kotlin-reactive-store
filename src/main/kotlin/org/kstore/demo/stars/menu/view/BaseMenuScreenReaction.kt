package org.kstore.demo.stars.menu.view

import org.kstore.demo.stars.KeyPressAction
import react.kstore.action.*
import javax.annotation.PreDestroy

abstract class BaseMenuScreenReaction(
        activeScreenStore: ActiveMenuScreenStore,
        screen: MenuScreen
) {

    private val subscription: Subscription = CommonDispatcher.subscribe(KeyPressAction::class) {
        if (activeScreenStore.state == screen)
            processKey(it.char)
    }

    abstract fun processKey(char: Char)

    @PreDestroy
    fun destroy() {
        subscription.unsubscribe()
    }
}
