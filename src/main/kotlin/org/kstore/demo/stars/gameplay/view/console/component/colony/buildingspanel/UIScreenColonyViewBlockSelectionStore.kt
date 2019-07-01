package org.kstore.demo.stars.gameplay.view.console.component.colony.buildingspanel

import org.kstore.demo.stars.gameplay.view.console.component.colony.buildingspanel.Selection.*
import org.springframework.stereotype.Service
import react.kstore.BasicStore
import react.kstore.action.*

class SelectNextColonyBlockItem
class SelectPrevColonyBlockItem

class SelectNextColonyBlock
class SelectColonyBuildings
class SelectColonyBuildQueue

@Service
class UIScreenColonyViewBlockSelectionStore : BasicStore<Selection>(
        initialState = BUILDINGS,
        reactions = {
            on(SelectNextColonyBlock::class) update { state, action ->
                when (state) {
                    BUILDINGS -> {
                        asyncAction(DeselectColonyBuildingAction())
                        asyncAction(SelectNextColonyBuildQueueItemAction())
                        BUILD_QUEUE
                    }
                    BUILD_QUEUE -> {
                        asyncAction(SelectNextColonyBuildingAction())
                        asyncAction(DeselectColonyBuildQueueItemAction())
                        BUILDINGS
                    }
                }
            }
            on(SelectColonyBuildings::class) update { state, action ->
                asyncAction(SelectNextColonyBuildingAction())
                asyncAction(DeselectColonyBuildQueueItemAction())
                BUILDINGS
            }
            on(SelectColonyBuildQueue::class) update { _, _ ->
                asyncAction(DeselectColonyBuildingAction())
                asyncAction(SelectNextColonyBuildQueueItemAction())
                BUILD_QUEUE
            }
            on(SelectNextColonyBlockItem::class) react { state, _ ->
                when (state) {
                    BUILDINGS -> action(SelectNextColonyBuildingAction())
                    BUILD_QUEUE -> action(SelectNextColonyBuildQueueItemAction())
                }
            }
            on(SelectPrevColonyBlockItem::class) react { state, _ ->
                when (state) {
                    BUILDINGS -> action(SelectPrevColonyBuildingAction())
                    BUILD_QUEUE -> action(SelectPrevColonyBuildQueueItemAction())
                }
            }
        }
)
