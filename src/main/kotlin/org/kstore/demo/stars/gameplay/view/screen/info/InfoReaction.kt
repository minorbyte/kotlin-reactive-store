package org.kstore.demo.stars.gameplay.view.screen.info

import org.kstore.demo.stars.gameplay.view.screen.*
import org.kstore.demo.stars.gameplay.view.screen.Screen.KEY_INFO
import org.kstore.demo.stars.gameplay.view.screen.BaseScreenReaction
import org.springframework.stereotype.Component
import react.kstore.action.asyncAction

@Component
class InfoReaction(
        activeScreenStore: ActiveScreenStore
) : BaseScreenReaction(activeScreenStore, KEY_INFO) {

    override fun processKey(char: Char) {
        when (char) {
            'u' -> returnToMainMap()
        }
    }

    private fun returnToMainMap() = asyncAction(ShowMainMapAction())
}
