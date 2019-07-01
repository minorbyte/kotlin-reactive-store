package org.kstore.demo.stars.gameplay.model.player.colony

import org.kstore.demo.stars.gameplay.model.player.PlayerId
import org.kstore.demo.stars.gameplay.model.player.resources.income.Income
import org.kstore.demo.stars.rule.blueprint.*
import org.kstore.demo.stars.rule.tech.TechAffects.*
import org.kstore.demo.stars.rule.tech.TechId
import org.kstore.demo.stars.rule.tech.TechTree.maxMultiplier
import react.kstore.*
import react.kstore.action.Dispatcher

class ColonyIncomeStore(
        dispatcher: Dispatcher,
        colonyBuildingsStore: Subscribable<Map<ColonyId, List<ColonyBuilding>>>,
        colonyStore: Subscribable<List<Colony>>,
        playerTechStore: Subscribable<Map<PlayerId, List<TechId>>>
) : BasicStore<Map<ColonyId, Income>>(
        dispatcher = dispatcher,
        initialState = mapOf(),
        dependsOn = {
            stores(
                    colonyBuildingsStore,
                    colonyStore,
                    playerTechStore
            ) {
                rewrite { buildingByColonies, colonies, techsByPlayer ->
                    buildingByColonies.mapValues { buildingByColony ->
                        val producingBuildings = buildingByColony
                                .value
                                .groupBy { it.blueprint.name }
                                .filter {
                                    when (it.key) {
                                        MINE_ID -> true
                                        REFINERY_ID -> true
                                        LABORATORY_ID -> true
                                        else -> false
                                    }
                                }
                                .mapValues { it.value.size }

                        val colony = colonies.first { it.id == buildingByColony.key }
                        val techs = techsByPlayer.getOrDefault(colony.playerId, listOf())

                        Income(
                                resources = (producingBuildings.getOrDefault(MINE_ID, 0) * MINE_INCOME * maxMultiplier(MINE, techs)).toInt(),
                                fuel = (producingBuildings.getOrDefault(REFINERY_ID, 0) * REFINERY_INCOME * maxMultiplier(REFINERY, techs)).toInt(),
                                researchPoints = producingBuildings.getOrDefault(LABORATORY_ID, 0) * LABORATORY_INCOME
                        )
                    }
                }
            }
        }
)
