package org.kstore.demo.stars.gameplay.view.console.component.tech

import org.kstore.demo.stars.gameplay.model.player.tech.PlayerTechStore
import org.kstore.demo.stars.gameplay.model.turn.PlayerTurnStore
import org.kstore.demo.stars.rule.tech.*
import org.springframework.stereotype.Service
import react.kstore.BasicStore

@Service
class ActivePlayerVisibleResearchedTechStore(
        playerTurnStore: PlayerTurnStore,
        playerTechStore: PlayerTechStore
) : BasicStore<List<Tech>>(
        initialState = listOf(),
        dependsOn = {
            stores(
                    playerTurnStore,
                    playerTechStore
            ) rewrite { playerTurn, techs ->
                val researchedTechs = techs[playerTurn.playerId]!!.map { TechTree[it]!! }

                researchedTechs
                        .filter { tech ->
                            researchedTechs.none { it.hides.isPresent && (it.hides.get() == tech.techId) }
                        }
            }
        }

)

