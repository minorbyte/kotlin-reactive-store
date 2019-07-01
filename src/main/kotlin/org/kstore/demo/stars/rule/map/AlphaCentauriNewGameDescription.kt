package org.kstore.demo.stars.rule.map

import org.kstore.demo.stars.*
import org.kstore.demo.stars.common.*
import org.kstore.demo.stars.rule.PlayerTurnStageType
import org.kstore.demo.stars.rule.ship.ShipType
import org.kstore.demo.stars.rule.starsystem.*
import java.util.*


const val COLONIAL_PLAYER_NAME = "Colonial Forces"
const val CYLON_PLAYER_NAME = "Cylon"

const val INITIAL_RESOURCES = 1000
const val INITIAL_FUEL = 50

val alphaCentauriNewGameDescriptionShips = listOf(
        ShipDescription(UUID.randomUUID().toString(), Position(7, 9), ShipType.CAPITAL, 50),
        ShipDescription(UUID.randomUUID().toString(), Position(5, 9), ShipType.CAPITAL),
        ShipDescription(UUID.randomUUID().toString(), Position(6, 9), ShipType.SCOUT),
        ShipDescription(UUID.randomUUID().toString(), Position(9, 6), ShipType.SCOUT),
        ShipDescription(UUID.randomUUID().toString(), Position(8, 5), ShipType.FRIGATE),
        ShipDescription(UUID.randomUUID().toString(), Position(7, 9), ShipType.FIGHTER),
        ShipDescription(UUID.randomUUID().toString(), Position(7, 9), ShipType.FIGHTER),
        ShipDescription(UUID.randomUUID().toString(), Position(7, 9), ShipType.FIGHTER),
        ShipDescription(UUID.randomUUID().toString(), Position(7, 9), ShipType.FIGHTER)
)

val alphaCentauriNewGameDescription = GameDescriptionImpl(
        name = "Alpha Centauri",
        players = listOf(
                PlayerDescription(
                        id = "PLAYER:$COLONIAL_PLAYER_NAME",
                        moveOrder = 0,
                        ai = false,
                        name = COLONIAL_PLAYER_NAME,
                        resources = INITIAL_RESOURCES,
                        fuel = INITIAL_FUEL,
                        colonies = listOf(),
                        ships = alphaCentauriNewGameDescriptionShips,
                        techs = emptyList(),
                        researchedTech = null
                ),
                PlayerDescription(
                        id = "PLAYER:$CYLON_PLAYER_NAME",
                        moveOrder = 1,
                        ai = true,
                        name = CYLON_PLAYER_NAME,
                        resources = INITIAL_RESOURCES,
                        fuel = INITIAL_FUEL,
                        colonies = listOf(),
                        ships = listOf(
                                ShipDescription(UUID.randomUUID().toString(), Position(2, 3), ShipType.CAPITAL)
                        ),
                        techs = emptyList(),
                        researchedTech = null
                )
        ),
        currentPlayerId = "PLAYER:$COLONIAL_PLAYER_NAME",
        currentPlayerTurnStage = PlayerTurnStageType.MOVEMENT,
        currentTurn = 1,
        currentPlayerTurnShipMovements = alphaCentauriNewGameDescriptionShips.map { Pair(it.id, it.shipType.moves) },
        starSystem = StarSystemDescription(
                name = "Alpha Centauri",
                size = Area(15, 28),
                star = StarDescription(
                        id = "Alpha Centauri",
                        title = "Alpha Centauri",
                        radius = 2,
                        position = Position(5, 5)
                ),
                planets = listOf(
                        PlanetDescription(
                                id = "Alpha Centauri I",
                                title = "Alpha Centauri I",
                                position = Position(2, 2),
                                resourceMultiplier = ResourceMultiplier.NORMAL,
                                planetType = PlanetType.STANDARD
                        ),
                        PlanetDescription(
                                id = "Alpha Centauri IA",
                                title = "Alpha Centauri IA",
                                position = Position(1, 2),
                                resourceMultiplier = ResourceMultiplier.NORMAL,
                                planetType = PlanetType.MOON
                        ),
                        PlanetDescription(
                                id = "Alpha Centauri II",
                                title = "Alpha Centauri II",
                                position = Position(9, 9),
                                resourceMultiplier = ResourceMultiplier.NORMAL,
                                planetType = PlanetType.SMALL
                        ),
                        PlanetDescription(
                                id = "Alpha Centauri IIA",
                                title = "Alpha Centauri IIA",
                                position = Position(10, 9),
                                resourceMultiplier = ResourceMultiplier.NORMAL,
                                planetType = PlanetType.MOON
                        ),
                        PlanetDescription(
                                id = "Alpha Centauri III",
                                title = "Alpha Centauri III",
                                position = Position(2, 9),
                                resourceMultiplier = ResourceMultiplier.NORMAL,
                                planetType = PlanetType.GAS_GIANT
                        ),
                        PlanetDescription(
                                id = "Alpha Centauri IV",
                                title = "Alpha Centauri IV",
                                position = Position(9, 2),
                                resourceMultiplier = ResourceMultiplier.NORMAL,
                                planetType = PlanetType.GAS_GIANT_RINGED
                        )
                )
        )
)
