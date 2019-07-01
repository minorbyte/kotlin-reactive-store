package org.kstore.demo.stars.gameplay.view.console.component.description

import org.kstore.demo.stars.common.*

fun printShip(
        playerName: String,
        shipTypeName: String,
        shipHP: String,
        shipMaxHP: String,
        shipMinAttack: String,
        shipMaxAttack: String,
        shipDefense: String,
        shipComputer: String,
        shipManeuver: String,
        shipRadar: String,
        shipMovesLeft: String,
        shipFighters: String,
        shipMaxFighters: String,
        shipCorvettes: String,
        shipMaxCorvettes: String,
        allowedToBuildColony: Boolean,
        hasHangar: Boolean,
        canRepair: Boolean,
        canBomb: Boolean,
        canJump: Boolean,
        jumping: Boolean
) = listOf(
        "--------------------------------",
        " ${playerName.leftField(30)} ",
        " ${shipTypeName.leftField(30)} ",
        " HP:                    ${shipHP.rightField(3)}/${shipMaxHP.leftField(3)} ",
        " ATTACK:  ${shipMinAttack.rightField(3)}-${shipMaxAttack.leftField(3)} DEFENSE:  ${shipDefense.rightField(3)} ",
        " COMPUTER:    ${shipComputer.rightField(2)} MANEUVER    ${shipManeuver.rightField(2)} ",
        " RADAR        ${shipRadar.rightField(2)}                ",
        "                  MOVES LEFT ${shipMovesLeft.rightField(2)} ",
        "                                ",
        "                                "
).plus(
        if (hasHangar) listOf(
                " HANGAR             LOAD/UNLOAD ",
                " FIGHTERS         ${shipFighters.rightField(2)}/${shipMaxFighters.leftField(2)} <U>/<H> ",
                " CORVETTES        ${shipCorvettes.rightField(2)}/${shipMaxCorvettes.leftField(2)} <Y>/<G> ",
                "--------------------------------"
        )
        else listOf(
                "                                ",
                "                                ",
                "                                ",
                "--------------------------------"
        )
).plus(
        if (jumping) {
            listOf(
                    " JUMPING. SELECT DESTINATION    ",
                    " <TAB> TO CANCEL                ",
                    " <J> TO CONFIRM JUMP DESTINATION"
            )
        } else {
            listOf(
                    when {
                        allowedToBuildColony -> " <B> TO BUILD COLONY            "
                        canBomb -> " <V> TO BOMB ENEMY COLONY       "
                        else -> "                                "
                    },
                    if (canRepair) " <R> TO REPAIR SHIP             "
                    else "                                ",
                    if (canJump) " <J> TO JUMP                    "
                    else "                                "
            )
        }
)


fun printShips(fleet: PrintablePlayerFleet) =
        listOf(
                "--------------------------------",
                " ${(fleet.playerName + " FLEET").toUpperCase().leftField(30)} ",
                "                                ",
                " CAPITAL SHIPS              ${fleet.capitals.toString().rightField(3)} ",
                " FRIGATES                   ${fleet.frigates.toString().rightField(3)} ",
                " CORVETTES                  ${fleet.corvettes.toString().rightField(3)} ",
                " FIGHTERS                   ${fleet.fighters.toString().rightField(3)} ",
                " SCOUTS                     ${fleet.scouts.toString().rightField(3)} ",
                "                                ",
                " TOTAL SHIPS                ${fleet.total.toString().rightField(3)} ",
                " POWER                    ${fleet.power.toString().rightField(5)} ",
                "                                ",
                "                                ",
                "                                ",
                "--------------------------------",
                " PRESS <E> TO SELECT NEXT SHIP  ",
                "       <P> TO VIEW SHIP LIST    "
        )

