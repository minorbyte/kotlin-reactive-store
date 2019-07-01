package org.kstore.demo.stars.gameplay.view.screen.menu

import org.kstore.demo.stars.gameplay.view.screen.*
import org.kstore.demo.stars.gameplay.view.screen.Screen.MENU
import org.springframework.stereotype.Component
import react.kstore.action.asyncAction

@Component
class MenuReaction(
        activeScreenStore: ActiveScreenStore
) : BaseScreenReaction(activeScreenStore, MENU) {
    override fun processKey(char: Char) {
        when (char) {
            'w' -> up()
            's' -> down()
            'u' -> back()
            'b' -> press()
        }
    }

    private fun back() = asyncAction(ShowMainMapAction())
    private fun down() = asyncAction(MenuItemDownAction())
    private fun up() = asyncAction(MenuItemUpAction())
    private fun press() = asyncAction(MenuItemPressAction())

}
