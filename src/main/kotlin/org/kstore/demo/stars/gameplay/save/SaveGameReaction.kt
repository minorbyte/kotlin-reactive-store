package org.kstore.demo.stars.gameplay.save

import com.fasterxml.jackson.databind.ObjectMapper
import org.kstore.demo.stars.*
import org.kstore.demo.stars.common.orNull
import org.kstore.demo.stars.gameplay.model.player.colony.*
import org.kstore.demo.stars.gameplay.model.player.resources.PlayerResourcesStore
import org.kstore.demo.stars.gameplay.model.player.ship.PlayerShipsStore
import org.kstore.demo.stars.gameplay.model.player.tech.*
import org.kstore.demo.stars.gameplay.model.starsystem.StarSystemStore
import org.kstore.demo.stars.gameplay.model.turn.PlayerTurnShipMovementStore
import org.kstore.demo.stars.gameplay.model.turn.stage.PlayerTurnStageStore
import org.kstore.demo.stars.rule.PlayerTurnStageType
import org.springframework.stereotype.Component
import react.kstore.action.CommonDispatcher
import java.io.FileOutputStream


@Component
class SaveGameReaction(
        gameDescription: GameDescription,
        playerResourcesStore: PlayerResourcesStore,
        colonyStore: ColonyStore,
        colonyBuildingsStore: ColonyBuildingsStore,
        colonyBuildQueueStore: ColonyBuildQueueStore,
        playerTechStore: PlayerTechStore,
        playerResearchingTechStore: PlayerResearchingTechStore,
        playerShipsStore: PlayerShipsStore,
        playerTurnStageStore: PlayerTurnStageStore,
        playerTurnShipMovementStore: PlayerTurnShipMovementStore,
        starSystemStore: StarSystemStore,
        mapper: ObjectMapper
) {

    init {
        CommonDispatcher.subscribe(SaveGameAction::class) {
            val resourcesByPlayer = playerResourcesStore.state
            val colonies = colonyStore.state
            val colonyBuildings = colonyBuildingsStore.state
            val colonyBuildQueue = colonyBuildQueueStore.state
            val playerTechs = playerTechStore.state
            val playerResearchingTech = playerResearchingTechStore.state
            val playerShips = playerShipsStore.state
            val playerTurnStage = playerTurnStageStore.state
            val playerTurnShipMovement = playerTurnShipMovementStore.state
            val starSystem = starSystemStore.state

            val gameDescription = GameDescriptionImpl(
                    name = gameDescription.name,
                    players = gameDescription.players.map { player ->
                        player.copy(
                                resources = resourcesByPlayer[player.id]!!.resources,
                                fuel = resourcesByPlayer[player.id]!!.fuel,
                                colonies = colonies.filter { it.playerId == player.id }.map { colony ->
                                    ColonyDescription(
                                            id = colony.id,
                                            name = colony.name,
                                            position = colony.position,
                                            buildings = colonyBuildings[colony.id]!!.map {
                                                Pair(it.blueprint.blueprintId, it.id)
                                            },
                                            buildQueue = colonyBuildQueue[colony.id]!!.items.map {
                                                Pair(it.blueprint.blueprintId, it.turnsLeft)
                                            }
                                    )
                                },
                                techs = playerTechs[player.id]!!,
                                researchedTech = playerResearchingTech[player.id]?.orNull().let { if (it == null) null else Pair(it.techId, it.spentSP) },
                                ships = playerShips.values.filter { it.playerId == player.id }.map { ship ->
                                    ShipDescription(
                                            id = ship.id,
                                            position = ship.position,
                                            hp = ship.hp,
                                            shipType = ship.shipType
                                    )
                                }
                        )
                    },
                    currentPlayerId = playerTurnStage.playerId,
                    currentPlayerTurnStage = playerTurnStage.turnStage as PlayerTurnStageType,
                    currentTurn = playerTurnStage.turnNumber,
                    currentPlayerTurnShipMovements = playerTurnShipMovement.map { Pair(it.key, it.value) },
                    starSystem = StarSystemDescription(
                            name = starSystem.name,
                            size = starSystem.size,
                            star = StarDescription(
                                    id = starSystem.star.id,
                                    title = starSystem.star.title,
                                    radius = starSystem.star.radius,
                                    position = starSystem.star.position
                            ),
                            planets = starSystem.planets.map { planet ->
                                PlanetDescription(
                                        id = planet.id,
                                        title = planet.title,
                                        position = planet.position,
                                        resourceMultiplier = planet.resourceMultiplier,
                                        planetType = planet.planetType
                                )
                            }
                    )
            )

            val bytes = mapper.writeValueAsBytes(gameDescription)

            FileOutputStream("game${it.index}.sav").use { fos ->
                fos.write(bytes)
            }
        }
    }
}
