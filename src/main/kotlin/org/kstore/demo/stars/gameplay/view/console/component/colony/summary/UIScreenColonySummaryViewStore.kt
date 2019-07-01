package org.kstore.demo.stars.gameplay.view.console.component.colony.summary

import org.kstore.demo.stars.gameplay.view.active.colony.*
import org.kstore.demo.stars.gameplay.view.console.component.description.printMinerals
import org.springframework.stereotype.Service
import react.kstore.BasicStore
import react.kstore.optional.present

@Service
class UIScreenColonySummaryViewStore(
        selectedColonySummaryViewStore: SelectedColonySummaryViewStore,
        selectedColonyPlanetViewStore: SelectedColonyPlanetViewStore,
        selectedColonyIncomeViewStore: SelectedColonyIncomeViewStore
) : BasicStore<List<String>>(
        initialState = emptySummary(),
        dependsOn = {
            stores(
                    selectedColonySummaryViewStore,
                    selectedColonyPlanetViewStore,
                    selectedColonyIncomeViewStore
            ) rewrite { maybeSummary, maybePlanet, maybeIncome ->
                present(emptySummary(), maybeSummary, maybePlanet, maybeIncome) { summary, planet, income ->
                    printSummaryComponent(
                            planetName = planet.planetName,
                            planetSize = planet.planetSize,
                            planetMinerals = printMinerals(planet.resourceMultiplier),
                            income = income
                    )
                }
            }
        }
)

