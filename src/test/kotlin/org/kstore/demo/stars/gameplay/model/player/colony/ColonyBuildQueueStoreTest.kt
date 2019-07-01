package org.kstore.demo.stars.gameplay.model.player.colony

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.kstore.*
import org.kstore.demo.stars.*
import org.kstore.demo.stars.gameplay.model.*
import org.kstore.demo.stars.gameplay.model.player.colony.ColonyPlanetType.STANDARD
import org.kstore.demo.stars.rule.blueprint.*
import org.kstore.store.TestStore

@StarsTest
internal class ColonyBuildQueueStoreTest : GameDescriptionEnvironmentTest() {

    lateinit var colonyStore: TestStore<List<Colony>>
    lateinit var colonyBuildingsStore: TestStore<Map<ColonyId, List<ColonyBuilding>>>

    @BeforeEach
    fun before() {
        colonyBuildingsStore = TestStore(mapOf())
        colonyStore = TestStore(listOf())
    }

    @Test
    fun `initial store state is taken from gameDescription`() {
        assertThat(ColonyBuildQueueStore(dispatcher, gameDescription, colonyStore, colonyBuildingsStore).state).isEqualTo(
                gameDescription.players
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
                        })
        )
    }

    @Test
    fun `build colony added to state`(@RandomBean action: BuildColonyAction) {
        val tested = ColonyBuildQueueStore(dispatcher, gameDescription, colonyStore, colonyBuildingsStore)

        dispatcher.action(action)

        awaitThat {
            assertThat(tested.state).containsKey(action.id)
            assertThat(tested.state[action.id]).isEqualTo(ColonyBuildQueue())
        }
    }

    @Test
    fun `decrease turns left on DecreaseTurnsLeftBuildQueueAction`(@RandomBean action: DecreaseTurnsLeftBuildQueueAction) {
        gameDescription = gameDescription.mapAllColonies { _, colony ->
            colony.copy(
                    buildQueue = listOf(MINE_ID to 2)
            )
        }
        colonyStore.setState(gameDescription.players.flatMap { player ->
            player.colonies.map { colony ->
                val planet = gameDescription.starSystem.planets.first { colony.position == it.position }

                Colony(
                        id = colony.id,
                        position = colony.position,
                        name = colony.name,
                        playerId = player.id,
                        planetSize = STANDARD,
                        planetMaxBuildings = 10,
                        planetResourceMultiplier = planet.resourceMultiplier
                )
            }
        })

        val tested = ColonyBuildQueueStore(dispatcher, gameDescription, colonyStore, colonyBuildingsStore)
        val colonyId = gameDescription.currentPlayer().colonies.randomItem().id

        dispatcher.action(action.copy(colonyId = colonyId))

        awaitThat {
            assertThat(tested.state[colonyId]!!.items.first().turnsLeft).isEqualTo(1)
            assertThat(tested.state.minus(colonyId).values).allMatch { it.items.first().turnsLeft == 2 }
        }
    }

    @Test
    fun `complete building on DecreaseTurnsLeftBuildQueueAction`(@RandomBean action: DecreaseTurnsLeftBuildQueueAction) {
        gameDescription = gameDescription.mapAllColonies { _, colony ->
            colony.copy(
                    buildQueue = listOf(MINE_ID to 1)
            )
        }
        colonyStore.setState(gameDescription.players.flatMap { player ->
            player.colonies.map { colony ->
                val planet = gameDescription.starSystem.planets.first { colony.position == it.position }

                Colony(
                        id = colony.id,
                        position = colony.position,
                        name = colony.name,
                        playerId = player.id,
                        planetSize = STANDARD,
                        planetMaxBuildings = planet.planetType.maxBuildings,
                        planetResourceMultiplier = planet.resourceMultiplier
                )
            }
        })

        val tested = ColonyBuildQueueStore(dispatcher, gameDescription, colonyStore, colonyBuildingsStore)
        val colonyId = gameDescription.currentPlayer().colonies.randomItem().id

        dispatcher.action(action.copy(colonyId = colonyId))

        delayAwaitThat {
            assertThat(emittedActions.last(BuildBuildingAtColonyAction::class)).isEqualTo(BuildBuildingAtColonyAction(colonyId, Blueprints[MINE_ID] as BuildingBlueprint))
            assertThat(tested.state[colonyId]!!.items).isEmpty()
            assertThat(tested.state.minus(colonyId).values).allMatch { it.items.first().turnsLeft == 1 }
        }
    }

    @Test
    fun `add to colony build queue on AddToColonyBuildQueueAction`() {
        val colonyId = gameDescription.currentPlayer().colonies.randomItem().id

        val tested = ColonyBuildQueueStore(dispatcher, gameDescription, colonyStore, colonyBuildingsStore)
        val queueSize = tested.state[colonyId]!!.items.size

        val blueprint = Blueprints[MINE_ID]!!
        dispatcher.action(AddToColonyBuildQueueAction(colonyId = colonyId, blueprint = blueprint))

        delayAwaitThat {
            assertThat(tested.state[colonyId]!!.items).hasSize(queueSize + 1)
            assertThat(tested.state[colonyId]!!.items.last()).matches { it.blueprint == blueprint && it.turnsLeft == blueprint.turnsTotal }
        }
    }

    @Test
    fun `can't add to full colony build queue on AddToColonyBuildQueueAction`() {
        gameDescription = gameDescription.mapAllColonies { _, colony ->
            colony.copy(
                    buildQueue = listOf(
                            REFINERY_ID to 1,
                            REFINERY_ID to 1,
                            REFINERY_ID to 1,
                            REFINERY_ID to 1,
                            REFINERY_ID to 1
                    )
            )
        }

        val colonyId = gameDescription.currentPlayer().colonies.randomItem().id

        val tested = ColonyBuildQueueStore(dispatcher, gameDescription, colonyStore, colonyBuildingsStore)
        val queueSize = tested.state[colonyId]!!.items.size

        val blueprint = Blueprints[MINE_ID]!!
        dispatcher.action(AddToColonyBuildQueueAction(colonyId = colonyId, blueprint = blueprint))

        delayAwaitThat {
            assertThat(emittedActions.lastOrNull(AddToColonyBuildQueueAction::class)).isNull()
            assertThat(tested.state[colonyId]!!.items)
                    .hasSize(queueSize)
                    .noneMatch { it.blueprint == blueprint }
        }
    }

    @Test
    fun `remove from colony build queue on RemoveFromColonyBuildQueueAction`() {
        gameDescription = gameDescription.mapAllColonies { _, colony ->
            colony.copy(
                    buildQueue = listOf(
                            REFINERY_ID to 1,
                            REFINERY_ID to 2,
                            REFINERY_ID to 3,
                            REFINERY_ID to 4,
                            REFINERY_ID to 5
                    )
            )
        }

        val colonyId = gameDescription.currentPlayer().colonies.randomItem().id
        val action = RemoveFromColonyBuildQueueAction(colonyId = colonyId, index = RANDOM.nextInt(5))

        val tested = ColonyBuildQueueStore(dispatcher, gameDescription, colonyStore, colonyBuildingsStore)

        dispatcher.action(action)

        delayAwaitThat {
            assertThat(tested.state[colonyId]!!.items)
                    .hasSize(4)
                    .noneMatch { it.turnsLeft == action.index + 1 }
        }
    }


    @Test
    fun `queue is validated on DemolishedBuildingAtColonyAction`() {
        gameDescription = gameDescription.mapAllColonies { _, colony ->
            colony.copy(
                    buildQueue = listOf(
                            SHIP_YARD_LEVEL3_ID to 1,
                            "FRIGATE" to 1,
                            "CORVETTE" to 1,
                            MINE_ID to 1
                    )
            )
        }

        val blueprint = Blueprints[SHIP_YARD_LEVEL2_ID]!! as BuildingBlueprint
        val colonyId = gameDescription.currentPlayer().colonies.randomItem().id
        val action = DemolishedBuildingAtColonyAction(colonyId = colonyId, building = ColonyBuilding(colonyId = colonyId, blueprint = blueprint))

        colonyBuildingsStore.setState(mapOf(
                colonyId to listOf(
                        ColonyBuilding(colonyId = colonyId, blueprint = Blueprints[SHIP_YARD_LEVEL1_ID] as BuildingBlueprint)
                )
        ))

        val tested = ColonyBuildQueueStore(dispatcher, gameDescription, colonyStore, colonyBuildingsStore)

        dispatcher.action(action)

        delayAwaitThat {
            assertThat(tested.state[colonyId]!!.items)
                    .hasSize(2)
                    .noneMatch { it.blueprint == Blueprints[SHIP_YARD_LEVEL3_ID]!! }
                    .noneMatch { it.blueprint == Blueprints["FRIGATE"]!! }
        }
    }
}
