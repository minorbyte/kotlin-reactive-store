package org.kstore.demo.stars.gameplay.view.console.component.panel

import org.kstore.demo.stars.gameplay.model.player.resources.*
import org.kstore.demo.stars.gameplay.model.player.resources.income.*
import org.kstore.demo.stars.gameplay.model.turn.PlayerTurnStore
import org.springframework.stereotype.Service
import react.kstore.BasicStore

const val NA = "N/A"

@Service
class ScreenUpperPanelComponentStore(
        playerTurnStore: PlayerTurnStore,
        playerResourcesStore: PlayerResourcesStore,
        playerIncomeStore: PlayerIncomeStore
) : BasicStore<String>(
        initialState = printTopPanel(NA, NA, NA, NA, NA, NA),
        dependsOn = {
            stores(
                    playerTurnStore,
                    playerIncomeStore,
                    playerResourcesStore
            ) {
                rewrite { playerTurn, playersIncome, playersResources ->
                    val income = playersIncome.getOrDefault(playerTurn.playerId, Income())
                    val resources = playersResources.getOrDefault(playerTurn.playerId, PlayerResources())

                    printTopPanel(
                            playerTurn.playerName.toUpperCase(),
                            resources.fuel.print(),
                            "+" + income.fuel.print(),
                            resources.resources.toString(),
                            "+" + income.resources.toString(),
                            income.researchPoints.toString()
                    )
                }
            }
        }
)
