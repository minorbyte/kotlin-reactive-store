package org.kstore.demo.stars.gameplay.view.screen.colony

import org.kstore.demo.stars.gameplay.view.console.component.colony.buildings.UIScreenColonyBuildingsViewStore
import org.kstore.demo.stars.gameplay.view.console.component.colony.buildqueue.UIScreenColonyBuildQueueViewStore
import org.kstore.demo.stars.gameplay.view.console.component.colony.summary.UIScreenColonySummaryViewStore
import org.kstore.demo.stars.gameplay.view.screen.BasicScreenStore
import org.springframework.stereotype.Service
import react.kstore.dependency.dependsOn

@Service
class ColonyScreenStore(
        uiScreenColonyBuildingsViewStore: UIScreenColonyBuildingsViewStore,
        uiScreenColonyBuildQueueViewStore: UIScreenColonyBuildQueueViewStore,
        uiScreenColonySummaryViewStore: UIScreenColonySummaryViewStore
) : BasicScreenStore(
) {
    init {
        dependsOn {
            stores(
                    uiScreenColonyBuildingsViewStore,
                    uiScreenColonyBuildQueueViewStore,
                    uiScreenColonySummaryViewStore
            ) rewrite { buildings, queue, summary ->
                printColonyScreen(
                        buildings, queue, summary
                )
            }
        }
    }
}
