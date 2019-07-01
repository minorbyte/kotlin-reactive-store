package org.kstore.demo.stars.gameplay.view.active.colony

import org.kstore.demo.stars.gameplay.model.player.colony.*
import org.kstore.demo.stars.rule.blueprint.Blueprints
import org.springframework.stereotype.Service
import react.kstore.BasicStore
import react.kstore.action.asyncAction
import react.kstore.optional.present
import java.util.*

@Service
class SelectedColonyBuildQueueViewStore(
        selectedColonyIdViewStore: SelectedColonyIdViewStore,
        colonyBuildQueueStore: ColonyBuildQueueStore
) : BasicStore<Optional<List<BuildQueueView>>>(
        initialState = Optional.empty(),
        reactions = {
            on(AddToBuildQueueAction::class) {
                react { _, action ->
                    present(Unit, selectedColonyIdViewStore.state) { colonyId ->
                        asyncAction(AddToColonyBuildQueueAction(
                                colonyId = colonyId,
                                blueprint = Blueprints[action.blueprintId]!!
                        ))
                    }
                }
                validate(
                        { _, action -> "Invalid building id ${action.blueprintId}" },
                        { _, action -> Blueprints.containsKey(action.blueprintId) }
                )
            }
            on(RemoveFromBuildQueueAction::class) {
                react { _, action ->
                    present(Unit, selectedColonyIdViewStore.state) { colonyId ->
                        asyncAction(RemoveFromColonyBuildQueueAction(
                                colonyId = colonyId,
                                index = action.index
                        ))
                    }
                }
                validate("Index is less than 0", { _, action -> action.index >= 0 })
            }
        },
        dependsOn = {
            stores(
                    selectedColonyIdViewStore,
                    colonyBuildQueueStore
            ) {
                rewrite { maybeColonyId, buildQueueByColony ->
                    present(Optional.empty(), maybeColonyId) { colonyId ->
                        Optional.of(buildQueueByColony.getOrDefault(colonyId, ColonyBuildQueue()).items.map {
                            BuildQueueView(
                                    displayName = it.blueprint.name,
                                    turnsLeft = it.turnsLeft
                            )
                        })
                    }
                }
            }
        }
)
