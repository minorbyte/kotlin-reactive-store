package org.kstore.demo.stars.gameplay.view.console.component.colony.buildings

import org.kstore.demo.stars.gameplay.view.active.colony.*
import org.kstore.demo.stars.gameplay.view.console.component.colony.buildingspanel.*
import org.springframework.stereotype.Service
import react.kstore.BasicStore
import react.kstore.action.asyncAction
import react.kstore.optional.present

@Service
class UIScreenColonyBuildingsViewStore(
        selectedColonyBuildingsViewStore: SelectedColonyBuildingsViewStore,
        uiScreenColonyViewBuildingsBlockSelectionStore: UIScreenColonyViewBuildingsBlockSelectionStore
) : BasicStore<List<String>>(
        initialState = emptyBuildings(),
        dependsOn = {
            stores(
                    selectedColonyBuildingsViewStore,
                    uiScreenColonyViewBuildingsBlockSelectionStore
            ) rewrite { maybeBuildings, selection ->
                present(emptyBuildings(), maybeBuildings) { buildings ->
                    printBuildingsComponent(buildings, selection)
                }
            }
        },
        reactions = {
            on(RemoveSelectedColonyItemAction::class) react { state, _ ->
                present(
                        Unit,
                        selectedColonyBuildingsViewStore.state,
                        uiScreenColonyViewBuildingsBlockSelectionStore.state
                ) { buildings, selection ->
                    asyncAction(DemolishBuildingAction(buildings.buildings[selection].id))
                }
            }
        }
)

