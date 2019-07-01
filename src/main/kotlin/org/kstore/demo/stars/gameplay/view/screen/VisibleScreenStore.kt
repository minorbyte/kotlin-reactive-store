package org.kstore.demo.stars.gameplay.view.screen

import org.kstore.demo.stars.UIRenderService
import org.kstore.demo.stars.gameplay.view.console.component.panel.*
import org.kstore.demo.stars.gameplay.view.screen.Screen.*
import org.kstore.demo.stars.gameplay.view.screen.colony.ColonyScreenStore
import org.kstore.demo.stars.gameplay.view.screen.info.InfoScreenStore
import org.kstore.demo.stars.gameplay.view.screen.mainmap.MainMapScreenStore
import org.kstore.demo.stars.gameplay.view.screen.menu.MenuScreenStore
import org.kstore.demo.stars.gameplay.view.screen.planets.PlanetsScreenStore
import org.kstore.demo.stars.gameplay.view.screen.ships.ShipsScreenStore
import org.kstore.demo.stars.gameplay.view.screen.tech.TechScreenStore
import org.springframework.stereotype.Service
import react.kstore.BasicStore
import react.kstore.dependency.dependsOn
import java.lang.String.join


@Service
class VisibleScreenStore(
        uiRenderService: UIRenderService,
        activeScreenStore: ActiveScreenStore,
        uiScreenUpperPanelStore: ScreenUpperPanelComponentStore,
        uiScreenLowerPanelStore: ScreenLowerPanelComponentStore,
        mainMapScreenStore: MainMapScreenStore,
        colonyScreenStore: ColonyScreenStore,
        menuScreenStore: MenuScreenStore,
        planetsScreenStore: PlanetsScreenStore,
        shipsScreenStore: ShipsScreenStore,
        techScreenStore: TechScreenStore,
        infoScreenStore: InfoScreenStore
) : BasicStore<String>(
        ""
) {

    init {
        dependsOn {
            stores(
                    activeScreenStore,
                    uiScreenUpperPanelStore,
                    uiScreenLowerPanelStore,
                    mainMapScreenStore,
                    colonyScreenStore,
                    infoScreenStore,
                    menuScreenStore,
                    planetsScreenStore,
                    shipsScreenStore,
                    techScreenStore
            ) {
                rewrite { screen,
                          uiUpperPanel,
                          uiLowerPanel,
                          mainMapContent,
                          colonyContent,
                          infoContent,
                          menuContent,
                          planetsContent,
                          shipsContent,
                          techContent ->

                    printScreen(
                            uiUpperPanel,
                            printContent(
                                    when (screen) {
                                        MAIN_MAP -> mainMapContent
                                        COLONY_VIEW -> colonyContent
                                        MENU -> menuContent
                                        KEY_INFO -> infoContent
                                        PLANETS -> planetsContent
                                        TECH -> techContent
                                        SHIPS -> shipsContent
                                    }
                            ),
                            uiLowerPanel
                    )
                }
            }
        }

        this.subscribe {
            uiRenderService.updateScreen(it)
        }
    }

    private fun printScreen(upperPanel: String, content: String, lowerPanel: String): String {
        return join("\n", upperPanel, content, lowerPanel)
    }

    private fun printContent(lines: List<String>) =
            lines.take(27).joinToString("\n") { "|$it|" }
}
