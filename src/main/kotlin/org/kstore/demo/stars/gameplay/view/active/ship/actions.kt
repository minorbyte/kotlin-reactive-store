package org.kstore.demo.stars.gameplay.view.active.ship

import org.kstore.demo.stars.common.Position
import org.kstore.demo.stars.gameplay.model.player.ship.ShipId
import react.kstore.selection.*


data class SelectShipAction(override val index: Int) : SelectExactListItemAction
data class SelectShipByIdAction(val id: ShipId)
class SelectNextShipAction : SelectNextListItemAction
class SelectPrevShipAction : SelectPreviousListItemAction
class DeselectAnyShipAction : DeselectListItemAction

data class ActiveShipMoveAction(val position: Position)

class SelectNextShipInListAction : SelectNextListItemAction
class SelectPrevShipInListAction : SelectPreviousListItemAction
class SelectShipInListAction(override val index: Int) : SelectExactListItemAction
class DeselectShipInListAction : DeselectListItemAction


class SelectPrevShipPageInListAction : SelectPreviousListPageAction
class SelectNextShipPageInListAction : SelectNextListPageAction
class SelectShipPageInListAction(override val index: Int) : SelectExactListPageAction
class DeselectShipPageInListAction : DeselectPageItemAction


class PutCursorOnSelectedShipAction

class RepairSelectedShipAction
class BombBySelectedShipAction

class ActiveShipJumpAction


class LoadCorvetteToCurrentShipAction
class LoadFighterToCurrentShipAction
class UnloadCorvetteFromCurrentShipAction
class UnloadFighterFromCurrentShipAction
