package org.kstore.demo.stars.gameplay.view.screen.colony

import org.kstore.demo.stars.gameplay.view.console.component.colony.buildingspanel.*
import org.kstore.demo.stars.gameplay.view.screen.*
import org.kstore.demo.stars.gameplay.view.screen.Screen.COLONY_VIEW
import org.kstore.demo.stars.gameplay.view.screen.BaseScreenReaction
import org.springframework.stereotype.Component
import react.kstore.action.asyncAction

@Component
class ColonyReaction(
        activeScreenStore: ActiveScreenStore
) : BaseScreenReaction(activeScreenStore, COLONY_VIEW) {
    override fun processKey(char: Char) {
        when (char) {
            'u' -> returnToMainMap()
            'a' -> selectNextColonyBlock()
            'd' -> selectNextColonyBlock()
            's' -> selectNextItem()
            'w' -> selectPrevItem()
            'b' -> buildSelected()
            '-' -> demolishSelected()
        }
    }

    private fun returnToMainMap() = asyncAction(ShowMainMapAction())
    private fun selectNextColonyBlock() = asyncAction(SelectNextColonyBlock())
    private fun selectNextItem() = asyncAction(SelectNextColonyBlockItem())
    private fun selectPrevItem() = asyncAction(SelectPrevColonyBlockItem())
    private fun buildSelected() = asyncAction(BuildSelectedColonyItemAction())
    private fun demolishSelected() = asyncAction(RemoveSelectedColonyItemAction())
}
