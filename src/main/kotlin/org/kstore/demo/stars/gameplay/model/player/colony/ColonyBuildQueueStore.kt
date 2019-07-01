package org.kstore.demo.stars.gameplay.model.player.colony

import org.kstore.demo.stars.GameDescription
import org.kstore.demo.stars.gameplay.model.player.resources.*
import org.kstore.demo.stars.gameplay.model.player.ship.*
import org.kstore.demo.stars.rule.blueprint.*
import org.kstore.demo.stars.rule.colony.MAX_BUILD_QUEUE_SIZE
import react.kstore.*
import react.kstore.action.Dispatcher
import react.kstore.optional.present
import react.kstore.reaction.Validation.Companion.condition
import java.lang.Math.floor
import java.util.*


class ColonyBuildQueueStore(
        dispatcher: Dispatcher,
        gameDescription: GameDescription,
        colonyStore: Store<List<Colony>>,
        colonyBuildingsStore: Store<Map<ColonyId, List<ColonyBuilding>>>
) : BasicStore<Map<ColonyId, ColonyBuildQueue>>(
        dispatcher = dispatcher,
        initialState = gameDescription.players
                .flatMap { player ->
                    player.colonies.map { colony ->
                        Pair(colony.id, ColonyBuildQueue(
                                items = colony.buildQueue.map {
                                    ColonyBuildQueueItem(
                                            id = colony.id,
                                            blueprint = Blueprints[it.first]!!,
                                            turnsLeft = it.second
                                    )
                                }
                        ))
                    }
                }
                .associateBy({
                    it.first
                }, {
                    it.second
                }),
        reactions = {
            on(BuildColonyAction::class) {
                update { state, action ->
                    state.plus(Pair(
                            action.id,
                            ColonyBuildQueue()
                    ))
                }
            }
            on(DecreaseTurnsLeftBuildQueueAction::class) update { state, action ->
                val colony = colonyStore.state.first { it.id == action.colonyId }
                val buildQueue = state.getOrDefault(action.colonyId, ColonyBuildQueue())
                state.plus(Pair(action.colonyId, buildQueue.copy(
                        items = buildQueue
                                .items
                                .mapIndexed { index, it ->
                                    if (index == 0) it.copy(turnsLeft = it.turnsLeft - 1) else it
                                }
                                .filter {
                                    if (it.turnsLeft == 0) {
                                        val blueprint = it.blueprint
                                        when (blueprint) {
                                            is BuildingBlueprint -> dispatcher.asyncAction(BuildBuildingAtColonyAction(action.colonyId, blueprint))
                                            is ShipBlueprint -> dispatcher.asyncAction(ShipCreateAction(
                                                    playerId = colony.playerId,
                                                    ship = Ship(
                                                            playerId = colony.playerId,
                                                            position = colony.position,
                                                            id = UUID.randomUUID().toString(),
                                                            shipType = blueprint.shipType
                                                    )))
                                        }
                                        false
                                    } else true
                                }
                )))
            }
            on(AddToColonyBuildQueueAction::class) {
                update { state, action ->
                    val buildQueue = state.getOrDefault(action.colonyId, ColonyBuildQueue())
                    val colony = colonyStore.state.first { it.id == action.colonyId }

                    dispatcher.action(PlayerResourcePaymentAction(colony.playerId, action.blueprint.cost))

                    state.plus(Pair(action.colonyId, buildQueue.copy(
                            items = buildQueue.items.plus(
                                    ColonyBuildQueueItem(
                                            action.colonyId,
                                            action.blueprint,
                                            action.blueprint.turnsTotal
                                    )
                            )
                    )))
                }
                validate { state, action ->
                    condition(
                            state[action.colonyId]!!.items.size < MAX_BUILD_QUEUE_SIZE,
                            "Build queue is full"
                    )
                }
            }
            on(RemoveFromColonyBuildQueueAction::class) update { state, action ->
                val buildQueue = state.getOrDefault(action.colonyId, ColonyBuildQueue())
                val colony = colonyStore.state.first { it.id == action.colonyId }
                val item = buildQueue.items[action.index]

                val remainedResources = floor((item.turnsLeft.toDouble() / item.blueprint.turnsTotal.toDouble()) * item.blueprint.cost).toInt()
                dispatcher.action(PlayerIncomeAction(colony.playerId, resources = remainedResources, fuel = 0))

                state.plus(Pair(action.colonyId, buildQueue.copy(
                        items = buildQueue.items.filterIndexed { index, _ -> index != action.index }
                )))
            }
            on(DemolishedBuildingAtColonyAction::class) update { state, action ->
                val buildings = colonyBuildingsStore.state.getOrDefault(action.colonyId, emptyList())
                val queue = state.getOrDefault(action.colonyId, ColonyBuildQueue())

                state.plus(Pair(action.colonyId, queue.copy(
                        items = queue.items.filter { item ->
                            present(true, item.blueprint.prerequisite) { prerequisiteId ->
                                return@filter buildings.any { it.blueprint.blueprintId == prerequisiteId }
                            }
                        }
                )))
            }
        }
)
