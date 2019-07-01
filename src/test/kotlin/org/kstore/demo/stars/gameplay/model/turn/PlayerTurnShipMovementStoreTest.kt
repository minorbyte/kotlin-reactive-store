package org.kstore.demo.stars.gameplay.model.turn

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.kstore.*
import org.kstore.demo.stars.*
import org.kstore.demo.stars.common.Position
import org.kstore.demo.stars.gameplay.model.*
import org.kstore.demo.stars.gameplay.model.player.PlayerId
import org.kstore.demo.stars.gameplay.model.player.colony.*
import org.kstore.demo.stars.gameplay.model.player.ship.*
import org.kstore.demo.stars.gameplay.model.starsystem.Planet
import org.kstore.demo.stars.rule.PlayerTurnStageType
import org.kstore.demo.stars.rule.PlayerTurnStageType.MOVEMENT
import org.kstore.store.TestStore

@StarsTest
internal class PlayerTurnShipMovementStoreTest: GameDescriptionEnvironmentTest() {

    private lateinit var shipsStore: TestStore<List<Ship>>

    @BeforeEach
    fun before() {
        shipsStore = TestStore(emptyList())
    }

    @Test
    fun `initial state is taken from gameDescription`() {
        assertThat(PlayerTurnShipMovementStore(gameDescription, shipsStore, dispatcher).state).isEqualTo(
                gameDescription.currentPlayerTurnShipMovements.associateBy({ it.first }, { it.second })
        )
    }

    @Test
    fun `when player movement turn stage starts is filled from ships store`(
            @RandomBean shipTemplates: List<Ship>
    ) {
        val tested = PlayerTurnShipMovementStore(gameDescription, shipsStore, dispatcher)
        val player = gameDescription.players.randomItem()

        shipsStore.setState(shipTemplates.map { it.copy(playerId = player.id) })

        dispatcher.action(StartPlayerTurnStageAction(MOVEMENT))

        awaitThat {
            assertThat(tested.state).isEqualTo(shipTemplates.associateBy({ it.id }, { it.shipType.moves }))
        }

    }

    @Test
    fun `when player turn stage other than movement starts is empty`(
            @RandomBean shipTemplates: List<Ship>
    ) {
        val tested = PlayerTurnShipMovementStore(gameDescription, shipsStore, dispatcher)
        val player = gameDescription.players.randomItem()

        shipsStore.setState(shipTemplates.map { it.copy(playerId = player.id) })

        dispatcher.action(StartPlayerTurnStageAction(RANDOM.except(MOVEMENT) { nextObject(PlayerTurnStageType::class) }))

        awaitThat {
            assertThat(tested.state).isEmpty()
        }
    }

    @Test
    fun `when ship is moved it's available movements decreased`(
            @RandomBean position: Position
    ) {
        val moves = 1 + RANDOM.nextInt(10)
        val player = gameDescription.currentPlayer()
        val ship = player.ships.randomItem()
        gameDescription = gameDescription.copy(
                currentPlayerTurnShipMovements = gameDescription.currentPlayerTurnShipMovements.plus(ship.id to moves)
        )

        val tested = PlayerTurnShipMovementStore(gameDescription, shipsStore, dispatcher)

        dispatcher.action(ShipMoveAction(player.id, ship.id, position))

        awaitThat {
            assertThat(tested.state[ship.id]).isEqualTo(moves - 1)
        }
    }

    @Test
    fun `forbids to move ships without available moves`(
            @RandomBean position: Position
    ) {
        val player = gameDescription.currentPlayer()
        val ship = player.ships.randomItem()
        gameDescription = gameDescription.copy(
                currentPlayerTurnShipMovements = gameDescription.currentPlayerTurnShipMovements.plus(ship.id to 0)
        )

        val tested = PlayerTurnShipMovementStore(gameDescription, shipsStore, dispatcher)

        dispatcher.action(ShipMoveAction(player.id, ship.id, position))

        awaitThat {
            assertThat(tested.state[ship.id]).isEqualTo(0)
        }
    }

    @Test
    fun `when ship do ShipFinalMovement it have no more turns`(
            @RandomBean position: Position
    ) {
        val moves = 1 + RANDOM.nextInt(10)
        val player = gameDescription.currentPlayer()
        val ship = player.ships.randomItem()
        gameDescription = gameDescription.copy(
                currentPlayerTurnShipMovements = gameDescription.currentPlayerTurnShipMovements.plus(ship.id to moves)
        )

        val tested = PlayerTurnShipMovementStore(gameDescription, shipsStore, dispatcher)

        dispatcher.action(TestShipFinalMovement(player.id, ship.id))

        awaitThat {
            assertThat(tested.state[ship.id]).isEqualTo(0)
        }
    }

    @Test
    fun `forbids to do ShipFinalMovement without available moves`(
            @RandomBean position: Position
    ) {
        val player = gameDescription.currentPlayer()
        val ship = player.ships.randomItem()
        gameDescription = gameDescription.copy(
                currentPlayerTurnShipMovements = gameDescription.currentPlayerTurnShipMovements.plus(ship.id to 0)
        )

        val tested = PlayerTurnShipMovementStore(gameDescription, shipsStore, dispatcher)

        dispatcher.action(TestShipFinalMovement(player.id, ship.id))

        awaitThat {
            assertThat(tested.state[ship.id]).isEqualTo(0)
        }
    }

    @Test
    fun `when ship jumps it have no more turns`(
            @RandomBean position: Position
    ) {
        val moves = 1 + RANDOM.nextInt(10)
        val player = gameDescription.currentPlayer()
        val ship = player.ships.randomItem()
        gameDescription = gameDescription.copy(
                currentPlayerTurnShipMovements = gameDescription.currentPlayerTurnShipMovements.plus(ship.id to moves)
        )

        val tested = PlayerTurnShipMovementStore(gameDescription, shipsStore, dispatcher)

        dispatcher.action(ShipJumpAction(player.id, ship.id, position))

        awaitThat {
            assertThat(tested.state[ship.id]).isEqualTo(0)
        }
    }

    @Test
    fun `forbids to jump ships without available moves`(
            @RandomBean position: Position
    ) {
        val player = gameDescription.currentPlayer()
        val ship = player.ships.randomItem()
        gameDescription = gameDescription.copy(
                currentPlayerTurnShipMovements = gameDescription.currentPlayerTurnShipMovements.plus(ship.id to 0)
        )

        val tested = PlayerTurnShipMovementStore(gameDescription, shipsStore, dispatcher)

        dispatcher.action(ShipJumpAction(player.id, ship.id, position))

        awaitThat {
            assertThat(tested.state[ship.id]).isEqualTo(0)
        }
    }

    @Test
    fun `when ship builds colony it moves ends`(
            @RandomBean colonyId: ColonyId,
            @RandomBean planet: Planet,
            @RandomBean name: String,
            @RandomBean position: Position
    ) {
        val moves = 1 + RANDOM.nextInt(10)
        val player = gameDescription.currentPlayer()
        val ship = player.ships.randomItem()
        gameDescription = gameDescription.copy(
                currentPlayerTurnShipMovements = gameDescription.currentPlayerTurnShipMovements.plus(ship.id to moves)
        )

        val tested = PlayerTurnShipMovementStore(gameDescription, shipsStore, dispatcher)

        dispatcher.action(BuildColonyAction(colonyId, planet, player.id, ship.id, name))

        awaitThat {
            assertThat(tested.state[ship.id]).isEqualTo(0)
        }
    }

    @Test
    fun `forbids ship to build colony without available moves`(
            @RandomBean colonyId: ColonyId,
            @RandomBean planet: Planet,
            @RandomBean name: String,
            @RandomBean position: Position
    ) {
        val moves = 1 + RANDOM.nextInt(10)
        val player = gameDescription.currentPlayer()
        val ship = player.ships.randomItem()
        gameDescription = gameDescription.copy(
                currentPlayerTurnShipMovements = gameDescription.currentPlayerTurnShipMovements.plus(ship.id to moves)
        )

        val tested = PlayerTurnShipMovementStore(gameDescription, shipsStore, dispatcher)

        dispatcher.action(BuildColonyAction(colonyId, planet, player.id, ship.id, name))

        awaitThat {
            assertThat(tested.state[ship.id]).isEqualTo(0)
        }
    }

    @Test
    fun `when ship repairs it moves ends`(
            @RandomBean colonyId: ColonyId,
            @RandomBean planet: Planet,
            @RandomBean name: String,
            @RandomBean position: Position
    ) {
        val moves = 1 + RANDOM.nextInt(10)
        val player = gameDescription.currentPlayer()
        val ship = player.ships.randomItem()
        gameDescription = gameDescription.copy(
                currentPlayerTurnShipMovements = gameDescription.currentPlayerTurnShipMovements.plus(ship.id to moves)
        )

        val tested = PlayerTurnShipMovementStore(gameDescription, shipsStore, dispatcher)

        dispatcher.action(BuildColonyAction(colonyId, planet, player.id, ship.id, name))

        awaitThat {
            assertThat(tested.state[ship.id]).isEqualTo(0)
        }
    }

    @Test
    fun `forbids ship to repair without available moves`(
            @RandomBean colonyId: ColonyId,
            @RandomBean planet: Planet,
            @RandomBean name: String,
            @RandomBean position: Position
    ) {
        val moves = 1 + RANDOM.nextInt(10)
        val player = gameDescription.currentPlayer()
        val ship = player.ships.randomItem()
        gameDescription = gameDescription.copy(
                currentPlayerTurnShipMovements = gameDescription.currentPlayerTurnShipMovements.plus(ship.id to moves)
        )

        val tested = PlayerTurnShipMovementStore(gameDescription, shipsStore, dispatcher)

        dispatcher.action(BuildColonyAction(colonyId, planet, player.id, ship.id, name))

        awaitThat {
            assertThat(tested.state[ship.id]).isEqualTo(0)
        }
    }

    class TestShipFinalMovement(
            override val playerId: PlayerId,
            override val shipId: ShipId
    ) : ShipFinalMovement
}
