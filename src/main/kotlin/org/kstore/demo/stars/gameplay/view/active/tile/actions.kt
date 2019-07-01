package org.kstore.demo.stars.gameplay.view.active.tile

import org.kstore.demo.stars.common.Position
import org.kstore.demo.stars.gameplay.model.player.ship.PositionAction

class EnterColonyAtCursor

class ShowFleetAtCursor

data class CursorMoveAction(val deltaX: Int, val deltaY: Int)
//data class CursorMoveAction(override val position: Position): PositionAction
data class CursorSetPositionAction(override val position: Position) : PositionAction

class CursorSetFreeAction
class CursorSetShipMovementAction
class CursorSetShipJumpAction

