package org.kstore.demo.stars.gameplay.view.screen.tech

import org.kstore.demo.stars.gameplay.view.console.component.tech.*
import org.kstore.demo.stars.gameplay.view.screen.*
import org.kstore.demo.stars.gameplay.view.screen.Screen.TECH
import org.springframework.stereotype.Component
import react.kstore.action.asyncAction

@Component
class TechReaction(
        activeScreenStore: ActiveScreenStore
) : BaseScreenReaction(activeScreenStore, TECH) {

    override fun processKey(char: Char) {
        when (char) {
            'u' -> returnToMainMap()
            's' -> selectTechDown()
            'w' -> selectTechUp()
            'b' -> researchSelected()
        }
    }

    private fun returnToMainMap() = asyncAction(ShowMainMapAction())
    private fun selectTechDown() = asyncAction(SelectNextTech())
    private fun selectTechUp() = asyncAction(SelectPrevTech())
    private fun researchSelected() = asyncAction(ResearchSelectedAction())
}
