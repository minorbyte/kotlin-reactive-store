package org.kstore.demo.stars.gameplay.view.console.component.colony.buildingspanel

import org.kstore.demo.stars.common.orNull
import org.kstore.demo.stars.gameplay.view.active.colony.*
import org.springframework.stereotype.Service
import react.kstore.BasicStore
import react.kstore.optional.present
import java.util.*

class SelectNextColonyBuildQueueItemAction
class SelectPrevColonyBuildQueueItemAction
class DeselectColonyBuildQueueItemAction

@Service
class UIScreenColonyViewBuildQueueBlockSelectionStore(
        selectedColonyBuildQueueViewStore: SelectedColonyBuildQueueViewStore,
        selectedColonyAvailableToBuildViewStore: SelectedColonyAvailableToBuildViewStore
) : BasicStore<Optional<Int>>(
        initialState = Optional.empty(),
        reactions = {
            on(SelectNextColonyBuildQueueItemAction::class) {
                update { state, _ ->
                    present(
                            Optional.empty(),
                            selectedColonyBuildQueueViewStore.state,
                            selectedColonyAvailableToBuildViewStore.state
                    ) { buildQueue, availableToBuild ->
                        val index = state.orNull() ?: -1
                        val size = buildQueue.size + availableToBuild.size
                        Optional.of(if (index >= size - 1) 0 else index + 1)
                    }
                }
            }
            on(SelectPrevColonyBuildQueueItemAction::class) {
                update { state, _ ->
                    present(
                            Optional.empty(),
                            selectedColonyBuildQueueViewStore.state,
                            selectedColonyAvailableToBuildViewStore.state
                    ) { buildQueue, availableToBuild ->
                        val index = state.orNull() ?: 1
                        val size = buildQueue.size + availableToBuild.size
                        Optional.of(if (index <= 0) size - 1 else index - 1)
                    }
                }
            }
            on(DeselectColonyBuildQueueItemAction::class) update { _, _ ->
                Optional.empty()
            }
        }
)

