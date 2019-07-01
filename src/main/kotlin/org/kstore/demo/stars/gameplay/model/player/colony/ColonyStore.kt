package org.kstore.demo.stars.gameplay.model.player.colony

import org.kstore.demo.stars.GameDescription
import org.kstore.demo.stars.gameplay.model.player.colony.ColonyPlanetType.*
import org.kstore.demo.stars.rule.starsystem.PlanetType
import react.kstore.BasicStore
import react.kstore.action.Dispatcher
import react.kstore.reaction.Validation.Companion.condition

fun translatePlanetSize(planetType: PlanetType) = when (planetType) {
    PlanetType.MOON -> MOON
    PlanetType.SMALL -> SMALL
    PlanetType.STANDARD -> STANDARD
    else -> throw IllegalStateException("Colonies can`t be at inhabitable planets")
}

class ColonyStore(
        dispatcher: Dispatcher,
        gameDescription: GameDescription
) : BasicStore<List<Colony>>(
        dispatcher = dispatcher,
        initialState = gameDescription.players.flatMap { player ->
            player.colonies.map { colony ->
                val planet = gameDescription.starSystem.planets.first { colony.position == it.position }

                Colony(
                        id = colony.id,
                        position = colony.position,
                        name = colony.name,
                        playerId = player.id,
                        planetSize = translatePlanetSize(planet.planetType),
                        planetMaxBuildings = planet.planetType.maxBuildings,
                        planetResourceMultiplier = planet.resourceMultiplier
                )
            }
        },
        reactions = {
            on(BuildColonyAction::class) {
                update { state, action ->
                    state.plus(Colony(
                            id = action.id,
                            planetResourceMultiplier = action.habitable.resourceMultiplier,
                            planetMaxBuildings = action.habitable.maxBuildings,
                            planetSize = translatePlanetSize(action.habitable.planetType),
                            playerId = action.playerId,
                            position = action.habitable.position,
                            name = action.name
                    ))
                }
                validate { state, action ->
                    condition(
                            state.none { it.position == action.habitable.position},
                            "Planet already has colony"
                    )
                }
            }
            on(DestroyColonyAction::class) {
                update { state, action ->
                    state.filterNot { it.id == action.id }
                }
                validate { state, action ->
                    condition(
                            state.none { it.id == action.id && it.playerId == action.byPlayerId},
                            "Can`t bomb own colony"
                    )
                }
            }
        }
)
