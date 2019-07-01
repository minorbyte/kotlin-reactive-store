package org.kstore.demo.stars.gameplay.model.player.colony

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kstore.*
import org.kstore.demo.stars.*
import org.kstore.demo.stars.gameplay.model.*
import org.kstore.demo.stars.gameplay.model.player.ship.ShipId
import org.kstore.demo.stars.rule.blueprint.*

@StarsTest
internal class ColonyBuildingsStoreTest : GameDescriptionEnvironmentTest() {
    @Test
    fun `initial store state is taken from gameDescription`() {
        assertThat(ColonyBuildingsStore(dispatcher, gameDescription).state).isEqualTo(
                gameDescription.players
                        .flatMap { player ->
                            player.colonies.flatMap { colony ->
                                colony.buildings.map {
                                    ColonyBuilding(
                                            id = it.second,
                                            colonyId = colony.id,
                                            blueprint = Blueprints[it.first] as BuildingBlueprint
                                    )
                                }
                            }
                        }
                        .groupBy {
                            it.colonyId
                        }
        )
    }

    @Test
    fun `build colony added to state`(@RandomBean action: BuildColonyAction) {
        val tested = ColonyBuildingsStore(dispatcher, gameDescription)

        dispatcher.action(action)

        awaitThat {
            assertThat(tested.state).containsKey(action.id)
            assertThat(tested.state[action.id]).isEmpty()
        }
    }

    @Test
    fun `build building at colony`(@RandomBean action: BuildBuildingAtColonyAction) {
        val tested = ColonyBuildingsStore(dispatcher, gameDescription)

        dispatcher.action(action)

        awaitThat {
            assertThat(tested.state).containsKey(action.colonyId)
            assertThat(tested.state[action.colonyId])
                    .hasSize(1)
                    .anyMatch { it.blueprint == action.blueprint }
        }
    }

    @Test
    fun `demolish building at colony`() {
        val mineId = RANDOM.nextString()
        gameDescription = gameDescription.mapAllColonies { _, colony ->
            colony.copy(
                    buildings = listOf(
                            MINE_ID to mineId,
                            REFINERY_ID to RANDOM.nextString()
                    )
            )
        }
        val colonyId = gameDescription.currentPlayer().colonies.randomItem().id

        val tested = ColonyBuildingsStore(dispatcher, gameDescription)

        dispatcher.action(DemolishBuildingAtColonyAction(colonyId = colonyId, id = mineId))

        awaitThat {
            assertThat(emittedActions.last(DemolishedBuildingAtColonyAction::class)).matches { it.colonyId == colonyId && it.building.id == mineId }

            assertThat(tested.state).containsKey(colonyId)
            assertThat(tested.state[colonyId])
                    .hasSize(1)
                    .noneMatch { it.id == mineId }
        }
    }

    @Test
    fun `bomb colony with power less than building count`(@RandomBean shipId: ShipId) {
        gameDescription = gameDescription.mapAllColonies { _, colony ->
            colony.copy(
                    buildings = listOf(
                            MINE_ID to RANDOM.nextString(),
                            REFINERY_ID to RANDOM.nextString(),
                            LABORATORY_ID to RANDOM.nextString()
                    )
            )
        }
        val colonyId = gameDescription.players.randomItemButNot(gameDescription.currentPlayer()).colonies.randomItem().id

        val tested = ColonyBuildingsStore(dispatcher, gameDescription)

        dispatcher.action(BombColonyAction(id = colonyId, playerId = gameDescription.currentPlayerId, shipId = shipId, power = 2))

        awaitThat {
            assertThat(tested.state).containsKey(colonyId)
            assertThat(tested.state[colonyId]).hasSize(1)
        }
    }

    @Test
    fun `bombardment first destroys buildings without any dependencies`(@RandomBean shipId: ShipId) {
        gameDescription = gameDescription.mapAllColonies { _, colony ->
            colony.copy(
                    buildings = listOf(
                            SHIP_YARD_LEVEL1_ID to RANDOM.nextString(),
                            SHIP_YARD_LEVEL2_ID to RANDOM.nextString(),
                            SHIP_YARD_LEVEL3_ID to RANDOM.nextString()
                    )
            )
        }
        val colonyId = gameDescription.players.randomItemButNot(gameDescription.currentPlayer()).colonies.randomItem().id

        val tested = ColonyBuildingsStore(dispatcher, gameDescription)

        dispatcher.action(BombColonyAction(id = colonyId, playerId = gameDescription.currentPlayerId, shipId = shipId, power = 2))

        awaitThat {
            assertThat(tested.state).containsKey(colonyId)
            assertThat(tested.state[colonyId])
                    .hasSize(1)
                    .allMatch { it.blueprint.blueprintId == SHIP_YARD_LEVEL1_ID }
        }
    }

    @Test
    fun `bombardment destroys colony`(@RandomBean shipId: ShipId) {
        gameDescription = gameDescription.mapAllColonies { _, colony ->
            colony.copy(
                    buildings = listOf(
                            SHIP_YARD_LEVEL1_ID to RANDOM.nextString(),
                            SHIP_YARD_LEVEL2_ID to RANDOM.nextString(),
                            SHIP_YARD_LEVEL3_ID to RANDOM.nextString()
                    )
            )
        }
        val colonyId = gameDescription.players.randomItemButNot(gameDescription.currentPlayer()).colonies.randomItem().id

        val tested = ColonyBuildingsStore(dispatcher, gameDescription)

        dispatcher.action(BombColonyAction(id = colonyId, playerId = gameDescription.currentPlayerId, shipId = shipId, power = RANDOM.nextInt(3, Int.MAX_VALUE)))

        awaitThat {
            assertThat(emittedActions.last(DestroyColonyAction::class)).matches {
                it.byPlayerId == gameDescription.currentPlayerId && it.id == colonyId
            }

            assertThat(tested.state).containsKey(colonyId)
            assertThat(tested.state[colonyId]).isEmpty()
        }
    }
}
