package org.kstore.demo.stars.gameplay.model.player.tech

import org.kstore.demo.stars.GameDescription
import org.kstore.demo.stars.gameplay.model.player.PlayerId
import org.kstore.demo.stars.gameplay.model.player.resources.DoPlayerResearchAction
import org.kstore.demo.stars.rule.tech.*
import react.kstore.BasicStore
import react.kstore.action.Dispatcher
import java.util.*

class PlayerResearchingTechStore(
        dispatcher: Dispatcher,
        gameDescription: GameDescription
) : BasicStore<Map<PlayerId, Optional<ResearchingTech>>>(
        dispatcher = dispatcher,
        initialState = gameDescription.players.associateBy({ it.id }, {
            it.researchedTech?.let { Optional.of(ResearchingTech(it.first, it.second)) } ?: Optional.empty()
        }),
        reactions = {
            on(ResearchTechAction::class) {
                update { state, action ->
                    state.plus(
                            action.playerId to Optional.of(ResearchingTech(action.techId, 0))
                    )
                }
            }
            on(CleanResearchedTechAction::class) {
                update { state, action ->

                    state.plus(
                            action.playerId to Optional.empty()
                    )
                }
            }
            on(DoPlayerResearchAction::class) {
                update { state, action ->
                    val optionalTech = state[action.playerId]!!
                    if (optionalTech.isPresent) {
                        val tech = TechTree[optionalTech.get().techId]!!
                        val currentSP = optionalTech.get().spentSP

                        if (tech.cost > currentSP + action.researchPoints) {
                            state.plus(action.playerId to Optional.of(ResearchingTech(tech.techId, currentSP + action.researchPoints)))
                        } else {
                            dispatcher.action(ResearchedTechAction(action.playerId, tech.techId))
                            state.plus(action.playerId to Optional.empty())
                        }
                    } else state
                }
            }
        }
)

data class ResearchingTech(
        val techId: TechId,
        val spentSP: Int
)
