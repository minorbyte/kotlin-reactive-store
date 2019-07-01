package org.kstore.demo.stars.gameplay.model.player.ship

import org.assertj.core.api.Assertions.assertThat
import org.awaitility.Awaitility.await
import org.awaitility.Duration.ONE_HUNDRED_MILLISECONDS
import org.junit.jupiter.api.*
import org.kstore.RandomBean
import org.kstore.action.EmittedActions
import org.kstore.demo.stars.gameplay.model.StarsTest
import org.kstore.demo.stars.gameplay.model.player.PlayerId
import org.kstore.demo.stars.gameplay.model.player.resources.PlayerResources
import org.kstore.store.TestStore
import react.kstore.action.*

@StarsTest
internal class ShipFuelConsumptionValidatorTest {

    private lateinit var tested: ShipFuelConsumptionValidator
    private lateinit var playerResourcesStore: TestStore<Map<PlayerId, PlayerResources>>
    private lateinit var shipsStore: TestStore<Map<Pair<PlayerId, ShipId>, Ship>>
    private lateinit var dispatcher: Dispatcher

    @BeforeEach
    fun before() {
        playerResourcesStore = TestStore(mapOf())
        shipsStore = TestStore(mapOf())
        dispatcher = DispatcherImpl()

        tested = ShipFuelConsumptionValidator(playerResourcesStore, shipsStore, dispatcher)
    }

    @RepeatedTest(5)
    @Test
    fun `validator must accept ShipMoveAction while fuel is enough`(
            @RandomBean action: ShipMoveAction,
            @RandomBean shipTemplate: Ship,
            @RandomBean defenderTemplate: Ship
    ) {
        val emittedActions = EmittedActions(dispatcher)

        val ship = shipTemplate.copy(playerId = action.playerId, id = action.shipId)
        shipsStore.setState(mapOf(action.playerId to action.shipId to ship))
        playerResourcesStore.setState(mapOf(action.playerId to PlayerResources(fuel = ship.shipType.fuelConsumption)))

        dispatcher.action(action)

        await().untilAsserted {
            assertThat(emittedActions.lastOrNull(ShipMoveAction::class)).isSameAs(action)
        }
    }

    @RepeatedTest(5)
    @Test
    fun `validator must decline ShipMoveAction while fuel is not enough`(
            @RandomBean action: ShipMoveAction,
            @RandomBean shipTemplate: Ship,
            @RandomBean defenderTemplate: Ship
    ) {
        val emittedActions = EmittedActions(dispatcher)

        val ship = shipTemplate.copy(playerId = action.playerId, id = action.shipId)
        shipsStore.setState(mapOf(action.playerId to action.shipId to ship))
        playerResourcesStore.setState(mapOf(action.playerId to PlayerResources(fuel = -1)))

        dispatcher.action(action)

        await().pollDelay(ONE_HUNDRED_MILLISECONDS).untilAsserted {
            assertThat(emittedActions.lastOrNull(ShipMoveAction::class)).isNull()
        }
    }

    @RepeatedTest(5)
    @Test
    fun `validator must accept ShipJumpAction while fuel is enough`(
            @RandomBean action: ShipJumpAction,
            @RandomBean shipTemplate: Ship,
            @RandomBean defenderTemplate: Ship
    ) {
        val emittedActions = EmittedActions(dispatcher)

        val ship = shipTemplate.copy(playerId = action.playerId, id = action.shipId)
        shipsStore.setState(mapOf(action.playerId to action.shipId to ship))
        playerResourcesStore.setState(mapOf(action.playerId to PlayerResources(fuel = ship.shipType.fuelConsumption * JUMP_FUEL_MULTIPLIER)))

        dispatcher.action(action)

        await().untilAsserted {
            assertThat(emittedActions.lastOrNull(ShipJumpAction::class)).isSameAs(action)
        }
    }

    @RepeatedTest(5)
    @Test
    fun `validator must decline ShipJumpAction while fuel is not enough`(
            @RandomBean action: ShipJumpAction,
            @RandomBean shipTemplate: Ship,
            @RandomBean defenderTemplate: Ship
    ) {
        val emittedActions = EmittedActions(dispatcher)

        val ship = shipTemplate.copy(playerId = action.playerId, id = action.shipId)
        shipsStore.setState(mapOf(action.playerId to action.shipId to ship))
        playerResourcesStore.setState(mapOf(action.playerId to PlayerResources(fuel = -1)))

        dispatcher.action(action)

        await().pollDelay(ONE_HUNDRED_MILLISECONDS).untilAsserted {
            assertThat(emittedActions.lastOrNull(ShipJumpAction::class)).isNull()
        }
    }
}
