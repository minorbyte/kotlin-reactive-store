package org.kstore.demo.stars.gameplay.view.active.colony

import org.kstore.demo.stars.gameplay.model.player.colony.*
import org.kstore.demo.stars.rule.blueprint.*
import org.springframework.stereotype.Service
import react.kstore.BasicStore
import react.kstore.optional.present
import java.util.*

@Service
class SelectedColonyAvailableToBuildViewStore(
        selectedColonyIdViewStore: SelectedColonyIdViewStore,
        colonyBuildingsStore: ColonyBuildingsStore,
        colonyBuildQueueStore: ColonyBuildQueueStore
) : BasicStore<Optional<List<AvailableToBuildView>>>(
        initialState = Optional.empty(),
        dependsOn = {
            stores(
                    selectedColonyIdViewStore,
                    colonyBuildingsStore,
                    colonyBuildQueueStore
            ) {
                rewrite { maybeColonyId, buildingsByColony, buildQueueByColony ->
                    present(Optional.empty(), maybeColonyId) { colonyId ->
                        val buildings = buildingsByColony.getOrDefault(colonyId, listOf())
                        val buildQueue = buildQueueByColony.getOrDefault(colonyId, ColonyBuildQueue())

                        Optional.of(
                                Blueprints.values
                                        .filter { blueprint ->
                                            present(Unit, blueprint.prerequisite) { prerequisite ->
                                                if (buildings.none { it.blueprint.blueprintId == prerequisite }) {
                                                    return@filter false
                                                }
                                            }

                                            true
                                        }
                                        .filter { blueprint ->
                                            if (blueprint is BuildingBlueprint && blueprint.single) {
                                                if (buildings.any { it.blueprint.blueprintId == blueprint.blueprintId }) {
                                                    return@filter false
                                                }

                                                if (buildQueue.items.any { it.blueprint.blueprintId == blueprint.blueprintId }) {
                                                    return@filter false
                                                }
                                            }
                                            true
                                        }
                                        .map {
                                            AvailableToBuildView(
                                                    id = it.blueprintId,
                                                    displayName = it.name,
                                                    cost = it.cost,
                                                    turns = it.turnsTotal
                                            )
                                        }
                        )
                    }
                }
            }
        }
)
