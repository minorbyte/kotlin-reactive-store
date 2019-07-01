package org.kstore.demo.stars.gameplay.view.console.component.colony.buildingspanel

import org.kstore.demo.stars.common.orNull
import org.kstore.demo.stars.gameplay.view.active.colony.SelectedColonyBuildingsViewStore
import org.springframework.stereotype.Service
import react.kstore.BasicStore
import react.kstore.optional.present
import java.util.*

class SelectNextColonyBuildingAction
class SelectPrevColonyBuildingAction
class DeselectColonyBuildingAction
class BuildSelectedColonyItemAction
class RemoveSelectedColonyItemAction


@Service
class UIScreenColonyViewBuildingsBlockSelectionStore(
        selectedColonyBuildingsViewStore: SelectedColonyBuildingsViewStore
) : BasicStore<Optional<Int>>(
        initialState = Optional.empty(),
        reactions = {
            on(SelectNextColonyBuildingAction::class) {
                update { state, _ ->
                    present(Optional.empty(), selectedColonyBuildingsViewStore.state) { colonyBuildings ->
                        val index = state.orNull() ?: -1
                        Optional.of(if (index >= colonyBuildings.buildings.size - 1) 0 else index + 1)
                    }
                }
            }
            on(SelectPrevColonyBuildingAction::class) {
                update { state, _ ->
                    present(Optional.empty(), selectedColonyBuildingsViewStore.state) { colonyBuildings ->
                        val index = state.orElse(1)
                        Optional.of(if (index <= 0) colonyBuildings.buildings.size - 1 else index - 1)
                    }
                }
            }
            on(DeselectColonyBuildingAction::class) update { _, _ ->
                Optional.empty()
            }
        }
)

