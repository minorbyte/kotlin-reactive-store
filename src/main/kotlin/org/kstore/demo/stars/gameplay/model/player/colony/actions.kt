package org.kstore.demo.stars.gameplay.model.player.colony

import org.kstore.demo.stars.common.Position
import org.kstore.demo.stars.gameplay.model.player.PlayerId
import org.kstore.demo.stars.gameplay.model.player.ship.*
import org.kstore.demo.stars.gameplay.model.starsystem.Planet
import org.kstore.demo.stars.gameplay.model.turn.PlayerAction
import org.kstore.demo.stars.rule.blueprint.*


data class BuildColonyAction(
        val id: ColonyId,
        val habitable: Planet,
        override val playerId: PlayerId,
        override val shipId: ShipId,
        val name: String
) : PositionAction, ShipFinalMovement {
    override val position: Position
        get() = habitable.position
}

data class BombColonyAction(
        val id: ColonyId,
        override val shipId: ShipId,
        override val playerId: PlayerId,
        val power: Int
) : ShipFinalMovement, PlayerAction

data class DestroyColonyAction(
        val id: ColonyId,
        val byPlayerId: PlayerId
)

data class AddToColonyBuildQueueAction(
        val colonyId: ColonyId,
        val blueprint: Blueprint
)

data class RemoveFromColonyBuildQueueAction(
        val colonyId: ColonyId,
        val index: Int
)

data class DecreaseTurnsLeftBuildQueueAction(
        val colonyId: ColonyId
)

data class BuildBuildingAtColonyAction(
        val colonyId: ColonyId,
        val blueprint: BuildingBlueprint
)

data class DemolishBuildingAtColonyAction(
        val colonyId: ColonyId,
        val id: BuildingId
)

data class DemolishedBuildingAtColonyAction(
        val colonyId: ColonyId,
        val building: ColonyBuilding
)
