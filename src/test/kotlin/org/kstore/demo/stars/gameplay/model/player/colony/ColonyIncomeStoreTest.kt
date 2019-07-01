package org.kstore.demo.stars.gameplay.model.player.colony

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.kstore.RandomBean
import org.kstore.demo.stars.*
import org.kstore.demo.stars.gameplay.model.*
import org.kstore.demo.stars.gameplay.model.player.PlayerId
import org.kstore.demo.stars.gameplay.model.player.resources.income.Income
import org.kstore.demo.stars.rule.blueprint.*
import org.kstore.demo.stars.rule.tech.TechId
import org.kstore.store.TestSubscribable

@StarsTest
internal class ColonyIncomeStoreTest : GameDescriptionEnvironmentTest() {

    lateinit var colonyBuildingsStore: TestSubscribable<Map<ColonyId, List<ColonyBuilding>>>
    lateinit var colonyStore: TestSubscribable<List<Colony>>
    lateinit var playerTechStore: TestSubscribable<Map<PlayerId, List<TechId>>>

    @BeforeEach
    fun before() {
        colonyBuildingsStore = TestSubscribable(mapOf())
        colonyStore = TestSubscribable(listOf())
        playerTechStore = TestSubscribable(mapOf())
    }

    @Test
    fun `initial store state is taken from gameDescription`() {
        assertThat(ColonyIncomeStore(dispatcher, colonyBuildingsStore, colonyStore, playerTechStore).state).isEmpty()
    }

    @Test
    fun `takes counts resource buildings from buildings store`(@RandomBean playerId: PlayerId) {
        val thisColony = RANDOM.nextObject(Colony::class).copy(playerId = playerId)
        val otherColony = RANDOM.nextObject(Colony::class)

        colonyStore.setState(listOf(thisColony, otherColony))

        val tested = ColonyIncomeStore(dispatcher, colonyBuildingsStore, colonyStore, playerTechStore)

        awaitThat {
            assertThat(tested.state).isEmpty()
        }

        colonyBuildingsStore.setState(mapOf(
                thisColony.id to listOf(ColonyBuilding(colonyId = thisColony.id, blueprint = Blueprints[MINE_ID] as BuildingBlueprint)),
                otherColony.id to listOf(ColonyBuilding(colonyId = thisColony.id, blueprint = Blueprints[MINE_ID] as BuildingBlueprint))
        ))

        awaitThat {
            assertThat(tested.state).hasSize(2)
            assertThat(tested.state[thisColony.id]).isEqualTo(Income(
                    resources = MINE_INCOME,
                    fuel = 0,
                    researchPoints = 0
            ))
        }
    }

    @Test
    fun `takes counts fuel buildings from buildings store`(@RandomBean playerId: PlayerId) {
        val thisColony = RANDOM.nextObject(Colony::class).copy(playerId = playerId)
        val otherColony = RANDOM.nextObject(Colony::class)

        colonyStore.setState(listOf(thisColony, otherColony))

        val tested = ColonyIncomeStore(dispatcher, colonyBuildingsStore, colonyStore, playerTechStore)

        awaitThat {
            assertThat(tested.state).isEmpty()
        }

        colonyBuildingsStore.setState(mapOf(
                thisColony.id to listOf(ColonyBuilding(colonyId = thisColony.id, blueprint = Blueprints[REFINERY_ID] as BuildingBlueprint)),
                otherColony.id to listOf(ColonyBuilding(colonyId = thisColony.id, blueprint = Blueprints[REFINERY_ID] as BuildingBlueprint))
        ))

        awaitThat {
            assertThat(tested.state).hasSize(2)
            assertThat(tested.state[thisColony.id]).isEqualTo(Income(
                    resources = 0,
                    fuel = REFINERY_INCOME,
                    researchPoints = 0
            ))
        }
    }

    @Test
    fun `takes counts RP buildings from buildings store`(@RandomBean playerId: PlayerId) {
        val thisColony = RANDOM.nextObject(Colony::class).copy(playerId = playerId)
        val otherColony = RANDOM.nextObject(Colony::class)

        colonyStore.setState(listOf(thisColony, otherColony))

        val tested = ColonyIncomeStore(dispatcher, colonyBuildingsStore, colonyStore, playerTechStore)

        awaitThat {
            assertThat(tested.state).isEmpty()
        }

        colonyBuildingsStore.setState(mapOf(
                thisColony.id to listOf(ColonyBuilding(colonyId = thisColony.id, blueprint = Blueprints[LABORATORY_ID] as BuildingBlueprint)),
                otherColony.id to listOf(ColonyBuilding(colonyId = thisColony.id, blueprint = Blueprints[LABORATORY_ID] as BuildingBlueprint))
        ))

        awaitThat {
            assertThat(tested.state).hasSize(2)
            assertThat(tested.state[thisColony.id]).isEqualTo(Income(
                    resources = 0,
                    fuel = 0,
                    researchPoints = LABORATORY_INCOME
            ))
        }
    }

    @Test
    fun `tech affects resource income`(@RandomBean playerId: PlayerId) {
        val thisColony = RANDOM.nextObject(Colony::class).copy(playerId = playerId)
        val otherColony = RANDOM.nextObject(Colony::class)

        colonyStore.setState(listOf(thisColony, otherColony))
        playerTechStore.setState(mapOf(
                thisColony.playerId to listOf("M1"),
                otherColony.playerId to listOf("M1")
        ))

        val tested = ColonyIncomeStore(dispatcher, colonyBuildingsStore, colonyStore, playerTechStore)

        awaitThat {
            assertThat(tested.state).isEmpty()
        }

        colonyBuildingsStore.setState(mapOf(
                thisColony.id to listOf(ColonyBuilding(colonyId = thisColony.id, blueprint = Blueprints[MINE_ID] as BuildingBlueprint)),
                otherColony.id to listOf(ColonyBuilding(colonyId = thisColony.id, blueprint = Blueprints[MINE_ID] as BuildingBlueprint))
        ))

        awaitThat {
            assertThat(tested.state).hasSize(2)
            assertThat(tested.state[thisColony.id]).isEqualTo(Income(
                    resources = (MINE_INCOME * 1.1).toInt(),
                    fuel = 0,
                    researchPoints = 0
            ))
        }
    }

    @Test
    fun `tech affects fuel income`(@RandomBean playerId: PlayerId) {
        val thisColony = RANDOM.nextObject(Colony::class).copy(playerId = playerId)
        val otherColony = RANDOM.nextObject(Colony::class)

        colonyStore.setState(listOf(thisColony, otherColony))
        playerTechStore.setState(mapOf(
                thisColony.playerId to listOf("R1"),
                otherColony.playerId to listOf("R1")
        ))

        val tested = ColonyIncomeStore(dispatcher, colonyBuildingsStore, colonyStore, playerTechStore)

        awaitThat {
            assertThat(tested.state).isEmpty()
        }

        colonyBuildingsStore.setState(mapOf(
                thisColony.id to listOf(ColonyBuilding(colonyId = thisColony.id, blueprint = Blueprints[REFINERY_ID] as BuildingBlueprint)),
                otherColony.id to listOf(ColonyBuilding(colonyId = thisColony.id, blueprint = Blueprints[REFINERY_ID] as BuildingBlueprint))
        ))

        awaitThat {
            assertThat(tested.state).hasSize(2)
            assertThat(tested.state[thisColony.id]).isEqualTo(Income(
                    resources = 0,
                    fuel = (REFINERY_INCOME * 1.1).toInt(),
                    researchPoints = 0
            ))
        }
    }
}
