package org.kstore.demo.stars.gameplay.model.player.resources.income

import org.kstore.demo.stars.GameDescription
import org.kstore.demo.stars.gameplay.model.player.PlayerId
import org.kstore.demo.stars.gameplay.model.player.colony.*
import org.kstore.demo.stars.rule.blueprint.*
import react.kstore.*
import react.kstore.action.Dispatcher
import react.kstore.dependency.dependsOn

class PlayerIncomeStore(
        dispatcher: Dispatcher,
        gameDescription: GameDescription,
        colonyStore: Subscribable<List<Colony>>,
        colonyBuildingsStore: Subscribable<Map<ColonyId, List<ColonyBuilding>>>
) : BasicStore<Map<PlayerId, Income>>(
        dispatcher = dispatcher,
        initialState = gameDescription.players.associateBy({
            it.id
        }, {
            Income()
        })
) {

    init {
        dependsOn {
            stores(
                    colonyStore,
                    colonyBuildingsStore
            ) {
                merge { colonies, colonyBuildings, state ->
                    state.plus(
                            colonies.groupingBy { it.playerId }
                                    .fold(MutableIncome()) { acc, colony ->
                                        colonyBuildings.getOrDefault(colony.id, emptyList()).fold(acc) { acc, building ->
                                            when (building.blueprint.blueprintId) {
                                                MINE_ID -> acc.copy(
                                                        resources = acc.resources + colony.planetResourceMultiplier.multiplier * MINE_INCOME / 10
                                                )
                                                REFINERY_ID -> acc.copy(fuel = acc.fuel + REFINERY_INCOME)
                                                LABORATORY_ID -> acc.copy(researchPoints = acc.researchPoints + LABORATORY_INCOME)
                                                else -> acc
                                            }
                                        }
                                    }
                                    .mapValues {
                                        Income(
                                                resources = it.value.resources,
                                                fuel = it.value.fuel,
                                                researchPoints = it.value.researchPoints
                                        )
                                    }
                    )
                }
            }
        }
    }

    private data class MutableIncome(
            var resources: Int = 0,
            var fuel: Int = 0,
            var researchPoints: Int = 0
    )

}
