package org.kstore.demo.stars.gameplay.model.player.ship

import org.kstore.demo.stars.common.Position
import org.kstore.demo.stars.gameplay.model.player.PlayerId
import org.kstore.demo.stars.rule.ship.ShipType

typealias ShipId = String

data class Ship(
        val id: ShipId,
        val playerId: PlayerId,
        val position: Position,
        val shipType: ShipType = ShipType.CAPITAL,
        val hp: Int = shipType.maxHp,
        val corvettes: Int = 0,
        val fighters: Int = 0
)

