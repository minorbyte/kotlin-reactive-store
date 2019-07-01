package org.kstore.demo.stars.gameplay.model.player.resources

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.kstore.demo.stars.*
import org.kstore.demo.stars.gameplay.model.*

@StarsTest
internal class PlayerResourcesStoreTest : GameDescriptionEnvironmentTest() {

    private lateinit var tested: PlayerResourcesStore

    @BeforeEach
    fun before() {
        gameDescription = gameDescription.copy(
                players = gameDescription.players.map {
                    it.copy(
                            fuel = RANDOM.nextInt(100500),
                            resources = RANDOM.nextInt(100500)
                    )
                }
        )

        tested = PlayerResourcesStore(dispatcher, gameDescription)
    }

    @Test
    fun `initial store state is taken from gameDescription`() {
        assertThat(tested.state).isEqualTo(
                gameDescription.players.associateBy({
                    it.id
                }, {
                    PlayerResources(
                            resources = it.resources,
                            fuel = it.fuel
                    )
                })
        )
    }

    @Test
    fun `resources are increased after income`() {
        val action = PlayerIncomeAction(
                playerId = gameDescription.currentPlayerId,
                resources = RANDOM.nextInt(100), fuel = RANDOM.nextInt(100)
        )
        dispatcher.action(action)

        awaitThat {
            assertThat(tested.state[gameDescription.currentPlayerId]!!.resources).isEqualTo(gameDescription.currentPlayer().resources + action.resources)
            assertThat(tested.state[gameDescription.currentPlayerId]!!.fuel).isEqualTo(gameDescription.currentPlayer().fuel + action.fuel)
        }
    }

    @Test
    fun `resources are decreased after resource payment`() {
        val action = PlayerResourcePaymentAction(
                playerId = gameDescription.currentPlayerId,
                resources = RANDOM.nextInt(100)
        )
        dispatcher.action(action)

        awaitThat {
            assertThat(tested.state[gameDescription.currentPlayerId]!!.resources).isEqualTo(gameDescription.currentPlayer().resources - action.resources)
            assertThat(tested.state[gameDescription.currentPlayerId]!!.fuel).isEqualTo(gameDescription.currentPlayer().fuel)
        }
    }

    @Test
    fun `resources are decreased after fuel payment`() {
        val action = PlayerFuelPaymentAction(
                playerId = gameDescription.currentPlayerId,
                fuel = RANDOM.nextInt(100)
        )
        dispatcher.action(action)

        awaitThat {
            assertThat(tested.state[gameDescription.currentPlayerId]!!.resources).isEqualTo(gameDescription.currentPlayer().resources)
            assertThat(tested.state[gameDescription.currentPlayerId]!!.fuel).isEqualTo(gameDescription.currentPlayer().fuel - action.fuel)
        }
    }

    @Test
    fun `resources can't be decreased below 0`() {
        val action = PlayerResourcePaymentAction(
                playerId = gameDescription.currentPlayerId,
                resources = gameDescription.currentPlayer().resources + RANDOM.nextInt(100)
        )
        dispatcher.action(action)

        awaitThat {
            assertThat(tested.state[gameDescription.currentPlayerId]!!.resources).isZero()
            assertThat(tested.state[gameDescription.currentPlayerId]!!.fuel).isEqualTo(gameDescription.currentPlayer().fuel)
        }
    }

    @Test
    fun `fuel can't be decreased below 0`() {
        val action = PlayerFuelPaymentAction(
                playerId = gameDescription.currentPlayerId,
                fuel = gameDescription.currentPlayer().fuel + RANDOM.nextInt(100)
        )
        dispatcher.action(action)

        awaitThat {
            assertThat(tested.state[gameDescription.currentPlayerId]!!.resources).isEqualTo(gameDescription.currentPlayer().resources)
            assertThat(tested.state[gameDescription.currentPlayerId]!!.fuel).isZero()
        }
    }

}
