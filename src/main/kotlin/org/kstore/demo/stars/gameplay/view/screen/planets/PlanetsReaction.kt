package org.kstore.demo.stars.gameplay.view.screen.planets

import org.kstore.demo.stars.gameplay.view.screen.*
import org.kstore.demo.stars.gameplay.view.screen.Screen.PLANETS
import org.kstore.demo.stars.gameplay.view.screen.BaseScreenReaction
import org.springframework.stereotype.Component
import react.kstore.action.asyncAction

@Component
class PlanetsReaction(
        activeScreenStore: ActiveScreenStore
) : BaseScreenReaction(activeScreenStore, PLANETS) {

    override fun processKey(char: Char) {
        when (char) {
            'u' -> returnToMainMap()
            's' -> selectDown()
            'w' -> selectUp()
            'c' -> openColony()
            'p' -> cursorToPlanet()
        }
    }

    private fun returnToMainMap() = asyncAction(ShowMainMapAction())
    private fun selectDown() = asyncAction(SelectNextPlanetAction())
    private fun selectUp() = asyncAction(SelectPrevPlanetAction())
    private fun openColony() = asyncAction(OpenSelectedColonyAction())
    private fun cursorToPlanet() = asyncAction(PutCursorOnSelectedPlanetAction())

}
