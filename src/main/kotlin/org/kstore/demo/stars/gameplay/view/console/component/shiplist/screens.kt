package org.kstore.demo.stars.gameplay.view.console.component.shiplist

import org.kstore.demo.stars.common.*
import org.kstore.demo.stars.gameplay.model.player.ship.*
import java.util.*


fun printShipsScreen(
        ships: List<Ship>,
        selectedShip: Optional<Ship>,
        movements: Map<ShipId, String>
) =
        mutableListOf(" ## | TYPE                 | HP      | ATTACK  | DEF | MOV | RAD | MAV | COMP ")
                .append(
                        ships
                                .mapIndexed { index, ship ->
                                    val result = " ${(index + 1).toString().rightField(2)} |" +
                                            " ${ship.shipType.name.leftField(20)} |" +
                                            " ${ship.hp.toString().rightField(3)}/${ship.shipType.maxHp.toString().rightField(3)} |" +
                                            " ${ship.shipType.minAttackPower.toString().rightField(3)}-${ship.shipType.maxAttackPower.toString().rightField(3)} |" +
                                            " ${ship.shipType.defense.toString().rightField(3)} |" +
                                            " ${movements.getOrDefault(ship.id, "?").rightField(1)}/${ship.shipType.moves.toString().rightField(1)} |" +
                                            " ${ship.shipType.radarRadius.toString().rightField(3)} |" +
                                            " ${ship.shipType.baseManeuverability.toString().rightField(3)} |" +
                                            " ${ship.shipType.baseComputerLevel.toString().rightField(4)} "

                                    if (selectedShip.isPresent && ship.id == selectedShip.get().id) "\u001B[47;30m$result\u001B[0m"
                                    else result
                                }
                ).toList()
