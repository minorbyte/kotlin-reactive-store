package org.kstore.demo.stars.gameplay.view.active.ship

import org.kstore.demo.stars.gameplay.model.player.ship.*
import org.kstore.demo.stars.gameplay.model.turn.*
import org.kstore.demo.stars.gameplay.view.active.tile.*
import org.kstore.demo.stars.rule.ship.ShipType.*
import org.springframework.stereotype.Service
import react.kstore.action.*
import react.kstore.reaction.Validation.Companion.condition
import react.kstore.reaction.on
import react.kstore.selection.ListItemStoreSelection
import react.kstore.update.whenState
import java.util.*

@Service
class ActiveShipStore(
        playerTurnShipStore: PlayerTurnShipStore,
        activeTileStore: ActiveTileStore
) : ListItemStoreSelection<Ship>(
        store = playerTurnShipStore,
        selectNextActionClass = SelectNextShipAction::class,
        prevActionClass = SelectPrevShipAction::class,
        selectActionClass = SelectShipAction::class,
        deselectActionClass = DeselectAnyShipAction::class
) {
    init {
        whenState({ oldState, newState -> oldState != newState }) { ship: Optional<Ship> ->
            ship.ifPresent {
                CommonDispatcher.asyncAction(CursorSetPositionAction(it.position))
            }
        }
        on(SelectShipByIdAction::class) react { _, action ->
            action(SelectShipAction(playerTurnShipStore.state.indexOfFirst { it.id == action.id }))
        }
        on(EndOfPlayerTurnAction::class) react { _, _ ->
            CommonDispatcher.asyncAction(DeselectAnyShipAction())
        }
        on(ShipMoveAction::class) {
            validate { state, action ->
                condition(
                        state.isPresent && action.shipId == state.get().id,
                        "Can`t move unselected ship"
                )
            }
        }
        on(ActiveShipMoveAction::class) {
            react { state, action ->
                state.ifPresent {
                    action(ShipMoveAction(
                            position = action.position,
                            playerId = it.playerId,
                            shipId = it.id
                    ))
                }
            }
            validate { state, _ ->
                condition(
                        state.isPresent,
                        "No ship selected"
                )
            }
        }
        on(ActiveShipJumpAction::class) {
            react { state, action ->
                state.ifPresent {
                    action(CursorSetShipJumpAction())
                }
            }
            validate { state, _ ->
                condition(
                        state.isPresent,
                        "No ship selected"
                )
            }
        }
        on(ShipDestroyedAction::class) react { state, action ->
            state.ifPresent {
                if (action.shipId == it.id && action.playerId == it.playerId) {
                    asyncAction(SelectNextShipAction())
                }
            }
        }
        on(RepairSelectedShipAction::class) react { state, _ ->
            state.ifPresent {
                asyncAction(ShipRepairAction(it.playerId, it.id))
            }
        }
        on(LoadCorvetteToCurrentShipAction::class) {
            react { state, _ ->
                state.ifPresent { carrier ->
                    val corvette = activeTileStore.state.ships.firstOrNull { it.shipType == CORVETTE && it.playerId == carrier.playerId }

                    if (corvette != null) {
                        asyncAction(CarrierUploadAction(carrier.playerId, corvette.id, carrier.id))
                    }
                }
            }
            validate { state, _ ->
                condition(
                        state.isPresent && activeTileStore.state.ships.any { it.shipType == CORVETTE && it.playerId == state.get().playerId },
                        "No corvettes to upload"
                )
            }
        }
        on(LoadFighterToCurrentShipAction::class) {
            react { state, _ ->
                state.ifPresent { carrier ->
                    val corvette = activeTileStore.state.ships.firstOrNull { it.shipType == FIGHTER && it.playerId == carrier.playerId }

                    if (corvette != null) {
                        asyncAction(CarrierUploadAction(carrier.playerId, corvette.id, carrier.id))
                    }
                }
            }
            validate { state, _ ->
                condition(
                        state.isPresent && activeTileStore.state.ships.any { it.shipType == FIGHTER && it.playerId == state.get().playerId },
                        "No corvettes to upload"
                )
            }
        }
        on(UnloadCorvetteFromCurrentShipAction::class) {
            react { state, _ ->
                state.ifPresent {
                    asyncAction(CarrierDeployAction(it.playerId, it.id, CORVETTE))
                }
            }
            validate { state, _ ->
                condition(
                        state.isPresent && state.get().corvettes > 0,
                        "Ship not existed ot has no corvettes"
                )
            }
        }
        on(UnloadFighterFromCurrentShipAction::class) {
            react { state, _ ->
                state.ifPresent {
                    asyncAction(CarrierDeployAction(it.playerId, it.id, FIGHTER))
                }
            }
            validate { state, _ ->
                condition(
                        state.isPresent && state.get().fighters > 0,
                        "Ship not existed ot has no fighters"
                )
            }
        }
    }
}


