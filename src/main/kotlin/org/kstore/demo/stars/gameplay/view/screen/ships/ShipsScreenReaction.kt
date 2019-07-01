package org.kstore.demo.stars.gameplay.view.screen.ships

import org.kstore.demo.stars.gameplay.view.active.ship.*
import org.kstore.demo.stars.gameplay.view.active.tile.CursorSetFreeAction
import org.kstore.demo.stars.gameplay.view.active.tile.CursorSetShipMovementAction
import org.kstore.demo.stars.gameplay.view.screen.*
import org.kstore.demo.stars.gameplay.view.screen.Screen.SHIPS
import org.springframework.stereotype.Component
import react.kstore.action.*

@Component
class ShipsScreenReaction(
        activeScreenStore: ActiveScreenStore
) : BaseScreenReaction(activeScreenStore, SHIPS) {

    override fun processKey(char: Char) {
        when (char) {
            'u' -> returnToMainMap()
            's' -> selectDown()
            'w' -> selectUp()
            'a' -> selectLeft()
            'd' -> selectRight()
            'p' -> cursorToShip()
        }
    }

    private fun returnToMainMap() = asyncAction(ShowMainMapAction())
    private fun selectDown() = asyncAction(SelectNextShipInListAction())
    private fun selectUp() = asyncAction(SelectPrevShipInListAction())
    private fun selectLeft() = asyncAction(SelectPrevShipPageInListAction())
    private fun selectRight() = asyncAction(SelectNextShipPageInListAction())
    private fun cursorToShip() {
        action(CursorSetFreeAction())
        action(PutCursorOnSelectedShipAction())
        action(ShowMainMapAction())
        action(CursorSetShipMovementAction())
    }

}
