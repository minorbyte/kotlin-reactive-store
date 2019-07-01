package org.kstore.demo.stars.gameplay.model.turn

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.kstore.RandomBean
import org.kstore.demo.stars.awaitThat
import org.kstore.demo.stars.gameplay.model.*
import org.kstore.demo.stars.gameplay.model.player.PlayerId
import org.kstore.demo.stars.gameplay.model.player.ship.*
import org.kstore.store.TestSubscribable

@StarsTest
internal class PlayerTurnShipStoreTest : DispatcherEnvironmentTest() {

    private lateinit var tested: PlayerTurnShipStore

    private lateinit var playerTurnSubscribable: TestSubscribable<PlayerTurn>
    private lateinit var shipsSubscribable: TestSubscribable<Map<Pair<PlayerId, ShipId>, Ship>>

    @RandomBean
    private lateinit var ships: List<Ship>

    private lateinit var players: List<PlayerId>

    private lateinit var playerTurn: PlayerTurn

    @BeforeEach
    fun before() {
        players = ships.map { it.playerId }.distinct()
        shipsSubscribable = TestSubscribable(ships.associateBy { Pair(it.playerId, it.id) })
        playerTurn = PlayerTurn(1, players.first())
        playerTurnSubscribable = TestSubscribable(playerTurn)

        tested = PlayerTurnShipStore(playerTurnSubscribable, shipsSubscribable, dispatcher)
    }

    @Test
    fun `initial store state is taken from other stores`() {
        awaitThat {
            assertThat(tested.state).containsExactlyInAnyOrderElementsOf(ships.filter { it.playerId == playerTurn.playerId })
        }
    }

    @Test
    fun `ships store updates when player turn changes`() {
        playerTurnSubscribable.setState(PlayerTurn(1, players[1]))

        awaitThat {
            assertThat(tested.state).containsExactlyInAnyOrderElementsOf(ships.filter { it.playerId == players[1] })
        }
    }

    @Test
    fun `ships store updates when player ships changes`(@RandomBean newShipTemplate: Ship) {
        val newShip = newShipTemplate.copy(playerId = playerTurn.playerId)

        shipsSubscribable.setState(ships.plus(newShip).associateBy { Pair(it.playerId, it.id) })

        awaitThat {
            assertThat(tested.state).contains(newShip)
        }
    }

}
