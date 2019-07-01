package org.kstore.demo.stars.gameplay.view.console.component.tech

import org.kstore.demo.stars.gameplay.model.player.tech.PlayerTechStore
import org.kstore.demo.stars.gameplay.model.turn.PlayerTurnStore
import org.kstore.demo.stars.rule.tech.*
import org.springframework.stereotype.Service
import react.kstore.BasicStore

@Service
class ActivePlayerAvailableTechStore(
        playerTurnStore: PlayerTurnStore,
        playerTechStore: PlayerTechStore
) : BasicStore<List<Tech>>(
        initialState = listOf(),
        dependsOn = {
            stores(
                    playerTurnStore,
                    playerTechStore
            ) rewrite { playerTurn, techs ->
                TechTree.values
                        .filter {
                            !techs[playerTurn.playerId]!!.contains(it.techId) && (!it.prerequisite.isPresent || techs[playerTurn.playerId]!!.contains(it.prerequisite.get()))
                        }
            }
        }

)

