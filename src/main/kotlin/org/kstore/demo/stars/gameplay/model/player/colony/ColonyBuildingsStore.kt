package org.kstore.demo.stars.gameplay.model.player.colony

import org.kstore.demo.stars.GameDescription
import org.kstore.demo.stars.rule.blueprint.*
import react.kstore.BasicStore
import react.kstore.action.Dispatcher
import react.kstore.optional.present
import java.util.*


private val random = Random()

class ColonyBuildingsStore(
        dispatcher: Dispatcher,
        gameDescription: GameDescription
) : BasicStore<Map<ColonyId, List<ColonyBuilding>>>(
        dispatcher = dispatcher,
        initialState = gameDescription.players
                .flatMap { player ->
                    player.colonies.flatMap { colony ->
                        colony.buildings.map {
                            ColonyBuilding(
                                    id = it.second,
                                    colonyId = colony.id,
                                    blueprint = Blueprints[it.first] as BuildingBlueprint
                            )
                        }
                    }
                }
                .groupBy {
                    it.colonyId
                },
        reactions = {
            on(BuildColonyAction::class) {
                update { state, action ->
                    state.plus(Pair(
                            action.id,
                            listOf()
                    ))
                }
            }
            on(BuildBuildingAtColonyAction::class) {
                update { state, action ->
                    state.plus(Pair(
                            action.colonyId,
                            state.getOrDefault(action.colonyId, listOf())
                                    .plus(ColonyBuilding(
                                            colonyId = action.colonyId,
                                            blueprint = action.blueprint
                                    ))
                    ))
                }
            }
            on(DemolishBuildingAtColonyAction::class) update { state, action ->
                val building = state.getOrDefault(action.colonyId, listOf()).firstOrNull { it.id == action.id } ?: return@update state
                dispatcher.asyncAction(DemolishedBuildingAtColonyAction(action.colonyId, building))

                state.plus(Pair(
                        action.colonyId,
                        state.getOrDefault(action.colonyId, listOf())
                                .filterNot { it.id == action.id }
                ))
            }
            on(BombColonyAction::class) update { state, action ->
                var buildingsToDestroy = state.getOrDefault(action.id, listOf())
                (0 until action.power).forEach {
                    if (buildingsToDestroy.isEmpty()) {
                        dispatcher.asyncAction(DestroyColonyAction(action.id, action.playerId))
                        return@update state.plus(action.id to listOf())
                    }

                    val buildingsAllowedToDestroy = buildingsToDestroy.filter { building ->
                        buildingsToDestroy.none {
                            present(false, it.blueprint.prerequisite) {
                                it == building.blueprint.blueprintId
                            }
                        }
                    }
                    val removed = buildingsAllowedToDestroy[random.nextInt(buildingsAllowedToDestroy.size)]

                    buildingsToDestroy = buildingsToDestroy.minus(removed)
                }

                state.plus(action.id to buildingsToDestroy)
            }
        }
)
