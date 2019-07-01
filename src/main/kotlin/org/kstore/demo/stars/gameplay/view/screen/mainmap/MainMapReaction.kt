package org.kstore.demo.stars.gameplay.view.screen.mainmap

import org.kstore.demo.stars.gameplay.view.active.EndHumanPlayerTurn
import org.kstore.demo.stars.gameplay.view.active.colony.BuildColonyAtCursor
import org.kstore.demo.stars.gameplay.view.active.ship.*
import org.kstore.demo.stars.gameplay.view.active.tile.*
import org.kstore.demo.stars.gameplay.view.screen.*
import org.kstore.demo.stars.gameplay.view.screen.Screen.MAIN_MAP
import org.springframework.stereotype.Component
import react.kstore.action.asyncAction

@Component
class MainMapReaction(
        activeScreenStore: ActiveScreenStore
) : BaseScreenReaction(activeScreenStore, MAIN_MAP) {
    override fun processKey(char: Char) {
        when (char) {
            '\t' -> deselectAnyShip()
            'a' -> moveCursor(-1, 0)
            's' -> moveCursor(0, +1)
            'w' -> moveCursor(0, -1)
            'd' -> moveCursor(+1, 0)
            'n' -> endTurn()
            'e' -> selectNextShip()
            'i' -> showColony()
            'p' -> showFleet()
            't' -> showTech()
//            'm' -> showPlanets()
            'o' -> showKeyBindings()
            'b' -> buildColony()
            'r' -> repairShip()
            'v' -> bombColony()
            'j' -> jumpShip()
            'y' -> loadCorvette()
            'u' -> loadFighter()
            'g' -> unloadCorvette()
            'h' -> unloadFighter()
            'm' -> showMenu()
        }
    }

    private fun deselectAnyShip() {
        asyncAction(DeselectAnyShipAction())
        asyncAction(CursorSetFreeAction())
    }

    private fun endTurn() = asyncAction(EndHumanPlayerTurn())

    private fun selectNextShip() {
        asyncAction(SelectNextShipAction())
        asyncAction(CursorSetShipMovementAction())
    }

    private fun buildColony() = asyncAction(BuildColonyAtCursor())

    private fun repairShip() = asyncAction(RepairSelectedShipAction())

    private fun bombColony() = asyncAction(BombBySelectedShipAction())

    private fun showColony() = asyncAction(EnterColonyAtCursor())

    private fun showFleet() = asyncAction(ShowFleetAtCursor())

    private fun showMenu() = asyncAction(ShowMenuAction())

    private fun showPlanets() = asyncAction(ShowPlanetsAction())

    private fun showTech() = asyncAction(ShowTechAction())

    private fun jumpShip() = asyncAction(ActiveShipJumpAction())

    private fun showKeyBindings() = asyncAction(ShowKeyInfoAction())

    private fun moveCursor(deltaX: Int, deltaY: Int) = asyncAction(CursorMoveAction(deltaX, deltaY))

    private fun loadCorvette() = asyncAction(LoadCorvetteToCurrentShipAction())
    private fun loadFighter() = asyncAction(LoadFighterToCurrentShipAction())
    private fun unloadCorvette() = asyncAction(UnloadCorvetteFromCurrentShipAction())
    private fun unloadFighter() = asyncAction(UnloadFighterFromCurrentShipAction())
}
