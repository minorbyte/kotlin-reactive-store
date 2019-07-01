package org.kstore.demo.stars.gameplay.view.active.colony

import org.kstore.demo.stars.gameplay.model.player.colony.ColonyIncomeStore
import org.springframework.stereotype.Service
import react.kstore.BasicStore
import react.kstore.optional.present
import java.util.*

@Service
class SelectedColonyIncomeViewStore(
        selectedColonyIdViewStore: SelectedColonyIdViewStore,
        colonyIncomeStore: ColonyIncomeStore
) : BasicStore<Optional<IncomeView>>(
        initialState = Optional.empty(),
        dependsOn = {
            stores(
                    selectedColonyIdViewStore,
                    colonyIncomeStore
            ) {
                rewrite { maybeColonyId, incomeByColony ->
                    present(Optional.empty(), maybeColonyId) { colonyId ->
                        val colonyIncome = incomeByColony[colonyId] ?: return@rewrite Optional.empty()

                        Optional.of(
                                IncomeView(
                                        resources = colonyIncome.resources,
                                        fuel = colonyIncome.fuel,
                                        researchPoints = colonyIncome.researchPoints
                                )
                        )
                    }
                }
            }
        }
)
