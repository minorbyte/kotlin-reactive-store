package org.kstore.demo.stars.gameplay.model.player.resources

import org.kstore.demo.stars.GameDescription
import org.kstore.demo.stars.gameplay.model.player.PlayerId
import react.kstore.BasicStore
import react.kstore.action.Dispatcher
import java.lang.Integer.max

class PlayerResourcesStore(
        dispatcher: Dispatcher,
        gameDescription: GameDescription
) : BasicStore<Map<PlayerId, PlayerResources>>(
        dispatcher = dispatcher,
        initialState = gameDescription.players.associateBy({
            it.id
        }, {
            PlayerResources(
                    resources = it.resources,
                    fuel = it.fuel
            )
        }),
        reactions = {
            on(PlayerIncomeAction::class) update { state, action ->
                state.plus(Pair(
                        first = action.playerId,
                        second = state[action.playerId]?.let {
                            it.copy(
                                    resources = it.resources + action.resources,
                                    fuel = it.fuel + action.fuel
                            )
                        } ?: PlayerResources(action.resources, action.fuel)
                ))
            }
            on(PlayerResourcePaymentAction::class) update { state, action ->
                state.plus(Pair(
                        first = action.playerId,
                        second = state[action.playerId]!!.let {
                            it.copy(
                                    resources = max(it.resources - action.resources, 0)
                            )
                        }
                ))
            }
            on(PlayerFuelPaymentAction::class) update { state, action ->
                state.plus(Pair(
                        first = action.playerId,
                        second = state[action.playerId]!!.let {
                            it.copy(
                                    fuel = max(it.fuel - action.fuel, 0)
                            )
                        }
                ))
            }
        }
)
