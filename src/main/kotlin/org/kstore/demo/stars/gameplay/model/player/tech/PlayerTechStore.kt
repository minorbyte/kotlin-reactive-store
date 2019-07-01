package org.kstore.demo.stars.gameplay.model.player.tech

import org.kstore.demo.stars.GameDescription
import org.kstore.demo.stars.gameplay.model.player.PlayerId
import org.kstore.demo.stars.rule.tech.TechId
import react.kstore.BasicStore
import react.kstore.action.Dispatcher
import react.kstore.reaction.Validation

class PlayerTechStore(
        dispatcher: Dispatcher,
        gameDescription: GameDescription
) : BasicStore<Map<PlayerId, List<TechId>>>(
        dispatcher = dispatcher,
        initialState = gameDescription.players.associateBy({ it.id }, { it.techs }),
        reactions = {
            on(ResearchedTechAction::class) {
                update { state, action ->
                    state.plus(
                            action.playerId to state.getOrDefault(action.playerId, listOf()).plus(action.techId)
                    )
                }
            }
            on(ResearchTechAction::class) {
                validate { state, action ->
                    Validation.condition(
                            !state[action.playerId]!!.contains(action.techId),
                            "Player already has this tech"
                    )
                }
            }
        }
)

