package org.kstore.demo.stars.gameplay.model.turn.map

import org.kstore.demo.stars.common.*
import org.kstore.demo.stars.gameplay.model.player.PlayerId
import org.kstore.demo.stars.gameplay.model.player.colony.Colony
import org.kstore.demo.stars.gameplay.model.player.ship.*
import org.kstore.demo.stars.gameplay.model.starsystem.*
import org.kstore.demo.stars.gameplay.model.turn.PlayerTurn
import org.kstore.demo.stars.rule.colony.COLONY_RADAR_RADIUS
import org.kstore.demo.stars.rule.tech.*
import react.kstore.*
import react.kstore.action.*
import react.kstore.dependency.dependsOn
import react.kstore.reaction.*
import react.kstore.reaction.Validation.Companion.condition


val initialState = PlayerTurnMap()


class PlayerTurnMapStore(
        dispatcher: Dispatcher = CommonDispatcher,
        playerTurnStore: Subscribable<PlayerTurn>,
        starSystemStore: Subscribable<StarSystem>,
        shipStore: Subscribable<Map<Pair<PlayerId, ShipId>, Ship>>,
        colonyStore: Subscribable<List<Colony>>,
        playerTechStore: Subscribable<Map<PlayerId, List<TechId>>>
) : BasicStore<PlayerTurnMap>(
        dispatcher = dispatcher,
        initialState = initialState
) {

    init {
        dependsOn {
            stores(
                    starSystemStore,
                    shipStore.reduce { it.values },
                    playerTurnStore,
                    colonyStore,
                    playerTechStore
            ) {
                rewrite { starSystem, ships, player, colonies, playerTechs ->
                    val techs = playerTechs.getOrDefault(player.playerId, emptyList())

                    PlayerTurnMap(
                            tiles = mutableMapOf<Position, Tile>()
                                    .append { printStar(starSystem.star) }
                                    .append { printPlanets(starSystem.planets) }
                                    .append { printShips(filterVisibleShips(ships, colonies, player.playerId, techs)) }
                                    .append { printColonies(filterVisibleColonies(ships, colonies, player.playerId, techs)) },
                            size = starSystem.size
                    )
                }
            }
        }
        on(ShipCreateAction::class) validate { state, action ->
            validateImpassableTile(action.ship.position, state)
            validateBorders(action.ship.position, state)
        }
        on(MovingAction::class) validate { state, action ->
            validateImpassableTile(action.position, state)
        }
        on(PositionAction::class) validate { state, action ->
            validateBorders(action.position, state)
        }
    }

    private fun MutableMap<Position, Tile>.printStar(star: Star) {
        inRadiusDo(star.position, star.radius, { x, y ->
            this[Position(x, y)] = StarTile(star)
        })
    }

    private fun MutableMap<Position, Tile>.printPlanets(planets: Collection<Planet>) {
        planets.forEach { planet ->
            this[planet.position] = PlanetTile(planet)
        }
    }

    private fun MutableMap<Position, Tile>.printShips(ships: List<Ship>) {
        ships.forEach { ship ->
            this.putOrReplace(ship.position, SpaceTile(), {
                when (it) {
                    is SpaceTile -> it.copy(ships = it.ships.plus(ship))
                    is PlanetTile -> it.copy(ships = it.ships.plus(ship))
                    is ColonyTile -> it.copy(ships = it.ships.plus(ship))
                    else -> it
                }
            })
        }
    }

    private fun MutableMap<Position, Tile>.printColonies(colonies: Collection<Colony>) {
        colonies.forEach { colony ->
            this.putOrReplace(colony.position, {
                if (it is PlanetTile) ColonyTile(it.planet, colony, it.ships)
                else it
            })
        }
    }

    private fun filterVisibleShips(ships: Collection<Ship>, colonies: Collection<Colony>, playerId: PlayerId, techs: List<TechId>): List<Ship> {
        val playerShips = ships.filter { it.playerId == playerId }
        val playerColonies = colonies.filter { it.playerId == playerId }
        val colonyRadarRadius = COLONY_RADAR_RADIUS * (techs.firstOrNull { it == RADAR_TECH_ID }?.let { TechTree[it]!! }?.multiplier
                ?: 1.0).toInt()

        return ships.filter { ship ->
            if (ship.playerId == playerId) true
            else playerShips.any { ship.position.isInRadiusOf(it.position, it.shipType.radarRadius) } ||
                    playerColonies.any { ship.position.isInRadiusOf(it.position, colonyRadarRadius) }
        }
    }

    private fun filterVisibleColonies(ships: Collection<Ship>, colonies: Collection<Colony>, playerId: PlayerId, techs: List<TechId>): List<Colony> {
        val playerShips = ships.filter { it.playerId == playerId }
        val playerColonies = colonies.filter { it.playerId == playerId }
        val colonyRadarRadius = COLONY_RADAR_RADIUS * (techs.firstOrNull { it == RADAR_TECH_ID }?.let { TechTree[it]!! }?.multiplier
                ?: 1.0).toInt()

        return colonies.filter { colony ->
            if (colony.playerId == playerId) true
            else playerShips.any { colony.position.isInRadiusOf(it.position, it.shipType.radarRadius) } ||
                    playerColonies.any { colony.position.isInRadiusOf(it.position, colonyRadarRadius) }
        }
    }

    private fun validateImpassableTile(p1: Position, p2: PlayerTurnMap): Validation {
        return condition(
                p2.tiles[p1] !is StarTile,
                "Can`t pass"
        )
    }

    private fun validateBorders(p1: Position, p2: PlayerTurnMap): Validation {
        return condition(
                p2.size.contain(p1),
                "Out of map"
        )
    }

}
