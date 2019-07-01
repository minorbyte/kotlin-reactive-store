package org.kstore.demo.stars.gameplay.model.player.resources.income

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.*
import org.kstore.*
import org.kstore.demo.stars.gameplay.model.*
import org.kstore.demo.stars.gameplay.model.player.colony.*
import org.kstore.demo.stars.rule.blueprint.*
import org.kstore.store.TestSubscribable

@StarsTest
internal class PlayerIncomeStoreTest : GameDescriptionEnvironmentTest() {

    private lateinit var tested: PlayerIncomeStore
    private lateinit var colonies: TestSubscribable<List<Colony>>
    private lateinit var colonyBuildings: TestSubscribable<Map<ColonyId, List<ColonyBuilding>>>

    @BeforeEach
    fun before() {
        colonies = TestSubscribable(listOf())
        colonyBuildings = TestSubscribable(mapOf())
        tested = PlayerIncomeStore(dispatcher, gameDescription, colonies, colonyBuildings)
    }

    @Test
    fun `initial store state is taken from gameDescription`() {
        Assertions.assertThat(tested.state).isEqualTo(
                gameDescription.players.associateBy({
                    it.id
                }, {
                    Income()
                }))
    }

    @Test
    fun `lists all colonies even they have no buildings`(
            @RandomBean playerColonies: List<Colony>
    ) {
        colonies.setState(playerColonies.map { it.copy(playerId = gameDescription.currentPlayerId) })

        assertThat(tested.state[gameDescription.currentPlayerId]!!.fuel).isZero()
        assertThat(tested.state[gameDescription.currentPlayerId]!!.resources).isZero()
        assertThat(tested.state[gameDescription.currentPlayerId]!!.researchPoints).isZero()
    }

    @Test
    fun `sum all resource income`(
            @RandomBean playerColonies: List<Colony>,
            @RandomBean playerColonyBuildings: List<ColonyBuilding>
    ) {
        val mines = playerColonyBuildings.map {
            it.copy(
                    colonyId = playerColonies.randomItem().id,
                    blueprint = it.blueprint.copy(blueprintId = MINE_ID)
            )
        }

        colonies.setState(playerColonies.map { it.copy(playerId = gameDescription.currentPlayerId) })
        colonyBuildings.setState(mines.groupBy { it.colonyId })

        await().untilAsserted {
            assertThat(tested.state[gameDescription.currentPlayerId]!!.resources).isEqualTo(mines.fold(0) { acc, mine ->
                acc + playerColonies.first { it.id == mine.colonyId }.planetResourceMultiplier.multiplier * MINE_INCOME / 10
            })
            assertThat(tested.state[gameDescription.currentPlayerId]!!.fuel).isZero()
            assertThat(tested.state[gameDescription.currentPlayerId]!!.researchPoints).isZero()
        }
    }

    @Test
    fun `sum all fuel income`(
            @RandomBean playerColonies: List<Colony>,
            @RandomBean playerColonyBuildings: List<ColonyBuilding>
    ) {
        val refineries = playerColonyBuildings.map {
            it.copy(
                    colonyId = playerColonies.randomItem().id,
                    blueprint = it.blueprint.copy(blueprintId = REFINERY_ID)
            )
        }

        colonies.setState(playerColonies.map { it.copy(playerId = gameDescription.currentPlayerId) })
        colonyBuildings.setState(refineries.groupBy { it.colonyId })

        await().untilAsserted {
            assertThat(tested.state[gameDescription.currentPlayerId]!!.resources).isZero()
            assertThat(tested.state[gameDescription.currentPlayerId]!!.fuel).isEqualTo(refineries.size * REFINERY_INCOME)
            assertThat(tested.state[gameDescription.currentPlayerId]!!.researchPoints).isZero()
        }
    }

    @Test
    fun `sum all RP income`(
            @RandomBean playerColonies: List<Colony>,
            @RandomBean playerColonyBuildings: List<ColonyBuilding>
    ) {
        val laboratories = playerColonyBuildings.map {
            it.copy(
                    colonyId = playerColonies.randomItem().id,
                    blueprint = it.blueprint.copy(blueprintId = LABORATORY_ID)
            )
        }

        colonies.setState(playerColonies.map { it.copy(playerId = gameDescription.currentPlayerId) })
        colonyBuildings.setState(laboratories.groupBy { it.colonyId })

        await().untilAsserted {
            assertThat(tested.state[gameDescription.currentPlayerId]!!.resources).isZero()
            assertThat(tested.state[gameDescription.currentPlayerId]!!.fuel).isZero()
            assertThat(tested.state[gameDescription.currentPlayerId]!!.researchPoints).isEqualTo(laboratories.size * LABORATORY_INCOME)
        }

    }
}


