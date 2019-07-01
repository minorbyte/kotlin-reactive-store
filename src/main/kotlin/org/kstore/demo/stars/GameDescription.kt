package org.kstore.demo.stars

import org.kstore.demo.stars.common.*
import org.kstore.demo.stars.rule.*
import org.kstore.demo.stars.rule.blueprint.BlueprintId
import org.kstore.demo.stars.rule.ship.ShipType
import org.kstore.demo.stars.rule.starsystem.*
import org.kstore.demo.stars.rule.tech.TechId


interface GameDescription {

    val name: String
    val currentTurn: Int
    val currentPlayerId: PlayerDescriptionId
    val currentPlayerTurnStage: TurnStage
    val currentPlayerTurnShipMovements: List<Pair<ShipDescriptionId, Int>>
    val players: List<PlayerDescription>
    val starSystem: StarSystemDescription
}


data class GameDescriptionImpl (
        override val name: String,
        override val currentTurn: Int,
        override val currentPlayerId: PlayerDescriptionId,
        override val currentPlayerTurnStage: PlayerTurnStageType,
        override val currentPlayerTurnShipMovements: List<Pair<ShipDescriptionId, Int>>,
        override val players: List<PlayerDescription>,
        override val starSystem: StarSystemDescription
) : GameDescription

typealias PlayerDescriptionId = String
typealias ColonyDescriptionId = String
typealias ShipDescriptionId = String

data class PlayerDescription(
        val id: PlayerDescriptionId,
        val moveOrder: Int,
        val ai: Boolean,
        val name: String = "Player",
        val resources: Int,
        val fuel: Int,
        val colonies: List<ColonyDescription>,
        val ships: List<ShipDescription>,
        val techs: List<TechId>,
        val researchedTech: Pair<TechId, Int>?
)

data class ColonyDescription(
        val id: ColonyDescriptionId,
        val position: Position,
        val name: String,
        val buildQueue: List<Pair<BlueprintId, Int>>,
        val buildings: List<Pair<BlueprintId, String>>
)

data class StarSystemDescription(
        val name: String,
        val size: Area,
        val star: StarDescription,
        val planets: List<PlanetDescription>
)

data class PlanetDescription (
        val id: String,
        val title: String,
        val position: Position,
        val planetType: PlanetType = PlanetType.STANDARD,
        val resourceMultiplier: ResourceMultiplier = ResourceMultiplier.NORMAL
)

data class StarDescription(
        val id: String,
        val title: String,
        val radius: Int = 2,
        val position: Position
)
data class ShipDescription(
        val id: ShipDescriptionId,
        val position: Position,
        val shipType: ShipType = ShipType.CAPITAL,
        val hp: Int = shipType.maxHp
)
