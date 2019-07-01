package org.kstore.demo.stars.gameplay.view.screen

import org.kstore.demo.stars.KeyPressAction
import react.kstore.action.*
import javax.annotation.PreDestroy

abstract class BaseScreenReaction(
        activeScreenStore: ActiveScreenStore,
        screen: Screen
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
