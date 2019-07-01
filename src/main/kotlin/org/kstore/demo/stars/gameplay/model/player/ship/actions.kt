package org.kstore.demo.stars.gameplay.model.player.ship

import org.kstore.demo.stars.common.Position
import org.kstore.demo.stars.gameplay.model.player.PlayerId
import org.kstore.demo.stars.gameplay.model.turn.PlayerAction
import org.kstore.demo.stars.rule.ship.ShipType
import org.kstore.demo.stars.rule.tech.TechId

interface MovingAction: PositionAction {
    override val position: Position
}

interface ShipFinalMovement {
    val playerId: PlayerId
    val shipId: ShipId
}

interface PositionAction {
    val position: Position
}

data class ShipCreateAction(
        val ship: Ship,
        override val playerId: PlayerId
) : PlayerAction

data class ShipMoveAction(
        override val playerId: PlayerId,
        val shipId: ShipId,
        override val position: Position
) : PlayerAction, PositionAction, MovingAction

data class ShipJumpAction(
        override val playerId: PlayerId,
        override val shipId: ShipId,
        override val position: Position
) : PlayerAction, PositionAction, MovingAction, ShipFinalMovement

data class ShipMovedAction(
        override val playerId: PlayerId,
        val shipId: ShipId,
        override val position: Position
) : PlayerAction, PositionAction, MovingAction

data class ShipDestroyedAction(
        val playerId: PlayerId,
        val shipId: ShipId
)

data class BattleStartAction(
        val attacker: Ship,
        val defender: Ship,
        val attackerTechs: List<TechId>,
        val defenderTechs: List<TechId>
)

data class BattleFinishAction(
        val attacker: Ship,
        val defender: Ship,
        val defenderDamage: Int,
        val attackerDamage: Int
)

data class ShipRepairAction(
        override val playerId: PlayerId,
        override val shipId: ShipId,
        val amount: Int = 0
): ShipFinalMovement, PlayerAction

data class CarrierUploadAction(
        val playerId: PlayerId,
        val uploadedShipId: ShipId,
        val carrierShipId: ShipId
)

data class CarrierDeployAction(
        val playerId: PlayerId,
        val carrierShipId: ShipId,
        val shipType: ShipType
)
