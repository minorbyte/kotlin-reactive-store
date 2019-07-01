package org.kstore.demo.stars.gameplay.view.active.colony

import org.kstore.demo.stars.gameplay.model.player.colony.*
import org.springframework.stereotype.Service
import react.kstore.BasicStore
import react.kstore.action.asyncAction
import react.kstore.optional.present
import java.util.*

@Service
class SelectedColonyBuildingsViewStore(
        selectedColonyIdViewStore: SelectedColonyIdViewStore,
        colonyBuildingsStore: ColonyBuildingsStore,
        colonyStore: ColonyStore
) : BasicStore<Optional<BuildingsView>>(
        initialState = Optional.empty(),
        reactions = {
            on(DemolishBuildingAction::class) {
                react { _, action ->
                    present(Unit, selectedColonyIdViewStore.state) { colonyId ->
                        asyncAction(DemolishBuildingAtColonyAction(
                                colonyId = colonyId,
                                id = action.buildingId
                        ))
                    }
                }
            }
        },
        dependsOn = {
            stores(
                    selectedColonyIdViewStore,
                    colonyBuildingsStore,
                    colonyStore
            ) {
                rewrite { maybeColonyId, buildingsByColony, colonies ->
                    present(Optional.empty(), maybeColonyId) { colonyId ->
                        val buildingsInColony = buildingsByColony.getOrDefault(colonyId, emptyList())

                        Optional.of(
                                BuildingsView(
                                        buildings = buildingsInColony
                                                .groupBy { it.blueprint.blueprintId }
                                                .filter { it.value.isNotEmpty() }
                                                .mapValues {
                                                    BuildingView(
                                                            id = it.value[0].id,
                                                            displayName = it.value[0].blueprint.name,
                                                            count = it.value.size
                                                    )
                                                }
                                                .values
                                                .toList(),
                                        totalCount = buildingsInColony.size,
                                        maxCount = colonies.firstOrNull() { it.id == colonyId }?.planetMaxBuildings ?: 0
                                )
                        )
                    }
                }
            }
        }
)

