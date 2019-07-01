package org.kstore.demo.stars.gameplay.view.console.component.colony.buildqueue

import org.kstore.demo.stars.gameplay.view.active.colony.*
import org.kstore.demo.stars.gameplay.view.console.component.colony.buildingspanel.*
import org.kstore.demo.stars.rule.colony.MAX_BUILD_QUEUE_SIZE
import org.springframework.stereotype.Service
import react.kstore.BasicStore
import react.kstore.action.asyncAction
import react.kstore.optional.present
import java.util.*

@Service
class UIScreenColonyBuildQueueViewStore(
        selectedColonyBuildQueueViewStore: SelectedColonyBuildQueueViewStore,
        selectedColonyAvailableToBuildViewStore: SelectedColonyAvailableToBuildViewStore,
        uiScreenColonyViewBuildQueueBlockSelectionStore: UIScreenColonyViewBuildQueueBlockSelectionStore
) : BasicStore<List<String>>(
        initialState = emptyBuildQueue(),
        reactions = {
            on(BuildSelectedColonyItemAction::class) react { state, _ ->
                present(
                        Unit,
                        selectedColonyBuildQueueViewStore.state,
                        selectedColonyAvailableToBuildViewStore.state,
                        uiScreenColonyViewBuildQueueBlockSelectionStore.state
                ) { queue, available, selection ->
                    if (queue.size < MAX_BUILD_QUEUE_SIZE) {
                        asyncAction(AddToBuildQueueAction(available[selection - queue.size].id))
                        asyncAction(SelectNextColonyBuildQueueItemAction())
                    }
                }
            }

            on(RemoveSelectedColonyItemAction::class) react { state, _ ->
                present(
                        Unit,
                        selectedColonyBuildQueueViewStore.state,
                        uiScreenColonyViewBuildQueueBlockSelectionStore.state
                ) { queue, selection ->
                    if (queue.size > selection) {
                        asyncAction(RemoveFromBuildQueueAction(selection))

                        if (queue.size > 1) {
                            asyncAction(SelectPrevColonyBuildQueueItemAction())
                        }
                    }
                }
            }
        },
        dependsOn = {
            stores(
                    selectedColonyBuildQueueViewStore,
                    selectedColonyAvailableToBuildViewStore,
                    uiScreenColonyViewBuildQueueBlockSelectionStore
            ) rewrite { maybeBuildings, maybeAvailableToBuild, selection ->
                present(emptyBuildQueue(), maybeBuildings, maybeAvailableToBuild) { queue, availableToBuild ->
                    printBuildQueueComponent(queue, availableToBuild, translateBuildQueueSelection(selection, queue, 5))
                }
            }
        }
)

fun translateBuildQueueSelection(buildQueueSelection: Optional<Int>, buildQueue: List<BuildQueueView>, padding: Int): Optional<Int> {
    return if (buildQueueSelection.isPresent && buildQueueSelection.get() >= buildQueue.size)
        Optional.of(buildQueueSelection.get() + padding - buildQueue.size)
    else buildQueueSelection
}
