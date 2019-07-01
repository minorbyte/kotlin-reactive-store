package org.kstore.demo.stars.gameplay.view.console.component.description

import org.kstore.demo.stars.gameplay.model.player.resources.PlayerResourcesStore
import org.kstore.demo.stars.gameplay.model.player.ship.*
import org.kstore.demo.stars.gameplay.model.turn.PlayerTurnShipMovementStore
import org.kstore.demo.stars.gameplay.model.turn.map.*
import org.kstore.demo.stars.gameplay.view.active.ship.ActiveShipStore
import org.kstore.demo.stars.gameplay.view.active.tile.*
import org.springframework.stereotype.Service
import react.kstore.*
import react.kstore.optional.present
import java.util.*


@Service
class UIScreenShipDescriptionViewStore(
        activeTileStore: Subscribable<Tile>,
        activeShipStore: ActiveShipStore,
        playerTurnShipMovementStore: PlayerTurnShipMovementStore,
        shipRepairValidator: ShipRepairValidator,
        bombColonyValidator: BombColonyValidator,
        playerResourcesStore: PlayerResourcesStore,
        cursorStore: CursorStore
) : BasicStore<Optional<List<String>>>(
        initialState = Optional.empty(),
        dependsOn = {
            stores(
                    activeTileStore,
                    activeShipStore,
                    playerTurnShipMovementStore,
                    playerResourcesStore,
                    cursorStore
            ) rewrite { tile, maybeShip, movements, resources, cursor ->
                present(Optional.empty(), maybeShip) { ship ->

                    val allowedToBuildColony = tile is PlanetTile && tile.planet.habitable

                    Optional.of(
                            printShip(
                                    ship.playerId.capitalize(),
                                    ship.shipType.name.capitalize(),
                                    ship.hp.toString(),
                                    ship.shipType.maxHp.toString(),
                                    ship.shipType.minAttackPower.toString(),
                                    ship.shipType.maxAttackPower.toString(),
                                    ship.shipType.defense.toString(),
                                    ship.shipType.baseComputerLevel.toString(),
                                    ship.shipType.baseManeuverability.toString(),
                                    ship.shipType.radarRadius.toString(),
                                    movements.getOrDefault(ship.id, 0).toString(),
                                    ship.fighters.toString(),
                                    ship.shipType.maxFighters.toString(),
                                    ship.corvettes.toString(),
                                    ship.shipType.maxCorvettes.toString(),
                                    allowedToBuildColony,
                                    ship.shipType.canJump,
                                    shipRepairValidator.canRepair(ship.playerId, ship.id),
                                    bombColonyValidator.canBomb(ship.playerId, ship.id),
                                    ship.shipType.canJump && ship.shipType.fuelConsumption * JUMP_FUEL_MULTIPLIER <= resources[ship.playerId]?.fuel ?: 0,
                                    cursor.state == CursorState.JUMP
                            )
                    )
                }
            }
        }
)
