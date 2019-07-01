package org.kstore.demo.stars.gameplay.view.console.component.description

import org.kstore.demo.stars.gameplay.model.player.*
import org.kstore.demo.stars.gameplay.model.player.colony.ColonyIncomeStore
import org.kstore.demo.stars.gameplay.model.player.resources.income.Income
import org.kstore.demo.stars.gameplay.model.turn.PlayerTurn
import org.kstore.demo.stars.gameplay.model.turn.map.*
import org.kstore.demo.stars.gameplay.view.active.colony.IncomeView
import org.kstore.demo.stars.gameplay.view.active.tile.Cursor
import org.kstore.demo.stars.gameplay.view.active.tile.CursorState.*
import org.kstore.demo.stars.gameplay.view.console.component.colony.summary.*
import org.kstore.demo.stars.common.isIn
import org.kstore.demo.stars.rule.ship.ShipType.*
import org.kstore.demo.stars.rule.starsystem.PlanetType
import org.springframework.stereotype.Service
import react.kstore.*
import react.kstore.dependency.dependsOn


@Service
class UITileDescriptionStore(
        playerStore: PlayerStore,
        activeTileStore: Subscribable<Tile>,
        playerTurnStore: Subscribable<PlayerTurn>,
        cursorStore: Subscribable<Cursor>,
        shipDescriptionStore: UIScreenShipDescriptionViewStore,
        colonyIncomeStore: ColonyIncomeStore
) : BasicStore<List<String>>(
        emptyList()
) {

    init {
        dependsOn {
            stores(
                    cursorStore,
                    activeTileStore,
                    playerTurnStore,
                    shipDescriptionStore
            ) {
                rewrite { cursor, tile, playerTurn, maybeShipDescription ->
                    val shipDescription = when {
                        cursor.state.isIn(JUMP, MOVE) && maybeShipDescription.isPresent -> {
                            maybeShipDescription.get()
                        }
                        tile.ships.isEmpty() -> printEmptyShips()
                        else -> {
                            printShips(
                                    PrintablePlayerFleet(
                                            capitals = tile.ships.count { it.shipType == CAPITAL },
                                            frigates = tile.ships.count { it.shipType == FRIGATE },
                                            corvettes = tile.ships.count { it.shipType == CORVETTE },
                                            fighters = tile.ships.count { it.shipType == FIGHTER },
                                            scouts = tile.ships.count { it.shipType == SCOUT },
                                            total = tile.ships.size,
                                            power = tile.ships.fold(0, { acc, ship -> ship.shipType.minAttackPower + acc }),
                                            playerName = playerStore.state[tile.ships.first().playerId]!!.name
                                    )
                            )
                        }
                    }

                    when (tile) {
                        is SpaceTile -> printEmptyDescription().plus(shipDescription)
                        is StarTile -> printStarDescription(starName = tile.star.id)
                        is PlanetTile -> printPlanet(tile).plus(shipDescription)
                        is ColonyTile -> printColony(tile, playerTurn.playerId, colonyIncomeStore.state.getOrDefault(tile.colony.id, Income())!!).plus(shipDescription)
                    }
                }
            }
        }
    }

    private fun printPlanet(tile: PlanetTile) =
            when (tile.planet.planetType) {
                PlanetType.SMALL -> printSmallPlanetDescription(
                        planetName = tile.planet.title,
                        planetMinerals = printMinerals(tile.planet.resourceMultiplier)
                )
                PlanetType.STANDARD -> printPlanetDescription(
                        planetName = tile.planet.title,
                        planetMinerals = printMinerals(tile.planet.resourceMultiplier)
                )
                PlanetType.GAS_GIANT -> printGasGiantDescription(
                        planetName = tile.planet.title
                )
                PlanetType.GAS_GIANT_RINGED -> printRingedGasGiantDescription(
                        planetName = tile.planet.title
                )
                PlanetType.MOON -> printMoonDescription(
                        planetName = tile.planet.title,
                        planetMinerals = printMinerals(tile.planet.resourceMultiplier)
                )
            }

    private fun printColony(tile: ColonyTile, playerId: PlayerId, income: Income): List<String> {
        if (playerId == tile.colony.playerId) {
            return printShortSummaryComponent(
                    planetName = tile.planet.title,
                    planetSize = tile.planet.planetType.name,
                    planetMinerals = printMinerals(tile.planet.resourceMultiplier),
                    colonyPlayer = tile.colony.playerId,
                    colonyIncome = income.let {
                        IncomeView(
                                resources = it.resources,
                                fuel = it.fuel,
                                researchPoints = it.researchPoints
                        )
                    }
            )
        } else {
            return printEnemyShortSummaryComponent(
                    planetName = tile.planet.title,
                    planetSize = tile.planet.planetType.name,
                    planetMinerals = printMinerals(tile.planet.resourceMultiplier),
                    colonyPlayer = tile.colony.playerId
            )
        }
    }
}
