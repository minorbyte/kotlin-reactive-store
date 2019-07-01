package org.kstore.demo.stars.gameplay.model.turn.map

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset.strictOffset
import org.junit.jupiter.api.*
import org.kstore.RandomBean
import org.kstore.demo.stars.*
import org.kstore.demo.stars.common.*
import org.kstore.demo.stars.gameplay.model.*
import org.kstore.demo.stars.gameplay.model.player.PlayerId
import org.kstore.demo.stars.gameplay.model.player.colony.Colony
import org.kstore.demo.stars.gameplay.model.player.ship.*
import org.kstore.demo.stars.gameplay.model.starsystem.*
import org.kstore.demo.stars.gameplay.model.turn.PlayerTurn
import org.kstore.demo.stars.rule.colony.COLONY_RADAR_RADIUS
import org.kstore.demo.stars.rule.tech.*
import org.kstore.store.TestSubscribable

@StarsTest
internal class PlayerTurnMapStoreTest : DispatcherEnvironmentTest() {

    private lateinit var playerId: PlayerId
    private lateinit var star: Star
    private lateinit var starSystemSize: Area
    private lateinit var starSystem: StarSystem

    private lateinit var playerTurnStore: TestSubscribable<PlayerTurn>
    private lateinit var starSystemStore: TestSubscribable<StarSystem>
    private lateinit var shipStore: TestSubscribable<Map<Pair<PlayerId, ShipId>, Ship>>
    private lateinit var colonyStore: TestSubscribable<List<Colony>>
    private lateinit var playerTechStore: TestSubscribable<Map<PlayerId, List<TechId>>>

    private lateinit var tested: PlayerTurnMapStore

    @BeforeEach
    fun before() {
        playerId = RANDOM.nextObject(String::class)
        star = Star(RANDOM.nextString(), RANDOM.nextString(), 2, Position(5, 5))
        starSystemSize = Area(10, 10)
        starSystem = StarSystem(RANDOM.nextString(), starSystemSize, star)

        playerTurnStore = TestSubscribable(PlayerTurn(1, playerId))
        starSystemStore = TestSubscribable(starSystem)
        shipStore = TestSubscribable(mapOf())
        colonyStore = TestSubscribable(listOf())
        playerTechStore = TestSubscribable(mapOf())

        tested = PlayerTurnMapStore(dispatcher, playerTurnStore, starSystemStore, shipStore, colonyStore, playerTechStore)
    }

    @Test
    fun `contain star`() {
        assertThat(tested.state.size).isEqualTo(starSystemSize)
        assertThat(tested.state.tiles)
                .hasSize(5)
                .allSatisfy { position, tile ->
                    assertThat(position.x).isCloseTo(5, strictOffset(2))
                    assertThat(position.y).isCloseTo(5, strictOffset(2))

                    assertThat(tile is StarTile).isTrue()
                    assertThat((tile as StarTile).star).isEqualTo(star)
                }
    }

    @Test
    fun `map must contain player ship`(
            @RandomBean shipTemplate: Ship
    ) {
        val ship = shipTemplate.copy(playerId = playerId)

        shipStore.setState(mapOf(playerId to ship.id to ship))

        val notAStar = tested.state.tiles.filter { it.value !is StarTile }

        assertThat(notAStar.entries).hasSize(1)
        assertThat(notAStar.keys.first()).isEqualTo(ship.position)
        assertThat(notAStar.values.first().ships[0]).isEqualTo(ship)
    }


    @Test
    fun `map must contain player colony`(
            @RandomBean colonyTemplate: Colony
    ) {
        val colony = colonyTemplate.copy(playerId = playerId)

        starSystemStore.setState(starSystemStore.state.copy(
                planets = listOf(RANDOM.except<Planet> { !it.habitable }.copy(position = colony.position))
        ))
        colonyStore.setState(listOf(colony))

        val notAStar = tested.state.tiles.filter { it.value !is StarTile }

        assertThat(notAStar.entries).hasSize(1)
        assertThat(notAStar.keys).allMatch { it == colony.position }
        assertThat(notAStar.values).allMatch { it is ColonyTile && it.colony == colony }
    }

    @Test
    fun `map must contain planet`(
            @RandomBean planet: Planet
    ) {
        starSystemStore.setState(starSystem.copy(planets = listOf(planet)))

        val notAStar = tested.state.tiles.filter { it.value !is StarTile }

        assertThat(notAStar.entries).hasSize(1)
        assertThat(notAStar.keys).allMatch { it == planet.position }
        assertThat(notAStar.values).allMatch { it is PlanetTile && it.planet == planet }
    }

    @Test
    fun `ship is visible in colony radar range`(
            @RandomBean enemy: PlayerId,
            @RandomBean colonyTemplate: Colony,
            @RandomBean shipTemplate: Ship
    ) {
        val playerColony = colonyTemplate.copy(playerId = playerId)
        val enemyShip = shipTemplate.copy(playerId = enemy, position = colonyTemplate.position.randomOffset(
                min = 1,
                max = COLONY_RADAR_RADIUS - 1
        ))

        starSystemStore.setState(starSystemStore.state.copy(
                planets = listOf(RANDOM.except<Planet> { !it.habitable }.copy(position = playerColony.position))
        ))
        colonyStore.setState(listOf(playerColony))
        shipStore.setState(mapOf(enemyShip.playerId to enemyShip.id to enemyShip))

        awaitThat {
            val notAStar = tested.state.tiles.filter { it.value !is StarTile }

            assertThat(notAStar.entries)
                    .hasSize(2)
                    .anyMatch {
                        val tile = it.value
                        tile is ColonyTile
                                && tile.colony.position == playerColony.position
                                && it.key == playerColony.position
                    }
                    .anyMatch { tile ->
                        tile.value.ships.any {
                            it.position == enemyShip.position
                                    && tile.key == enemyShip.position
                                    && it.playerId == enemy
                        }
                    }
        }
    }

    @Test
    fun `ship is visible in extended by tech colony radar range`(
            @RandomBean enemy: PlayerId,
            @RandomBean colonyTemplate: Colony,
            @RandomBean shipTemplate: Ship
    ) {
        val playerColony = colonyTemplate.copy(playerId = playerId)
        val enemyShip = shipTemplate.copy(playerId = enemy, position = colonyTemplate.position.randomOffset(
                min = 1,
                max = (COLONY_RADAR_RADIUS * TechTree[RADAR_TECH_ID]!!.multiplier).toInt() - 1
        ))

        starSystemStore.setState(starSystemStore.state.copy(
                planets = listOf(RANDOM.except<Planet> { !it.habitable }.copy(position = playerColony.position))
        ))
        colonyStore.setState(listOf(playerColony))
        shipStore.setState(mapOf(enemyShip.playerId to enemyShip.id to enemyShip))
        playerTechStore.setState(mapOf(playerId to listOf(RADAR_TECH_ID)))

        awaitThat {
            val notAStar = tested.state.tiles.filter { it.value !is StarTile }

            assertThat(notAStar.entries)
                    .hasSize(2)
                    .anyMatch {
                        val tile = it.value
                        tile is ColonyTile
                                && tile.colony.position == playerColony.position
                                && it.key == playerColony.position
                    }
                    .anyMatch { tile ->
                        tile.value.ships.any {
                            it.position == enemyShip.position
                                    && tile.key == enemyShip.position
                                    && it.playerId == enemy
                        }
                    }
        }
    }

    @Test
    fun `ship is invisible beyond colony radar range`(
            @RandomBean enemy: PlayerId,
            @RandomBean colonyTemplate: Colony,
            @RandomBean shipTemplate: Ship
    ) {
        val playerColony = colonyTemplate.copy(playerId = playerId)
        val enemyShip = shipTemplate.copy(playerId = enemy, position = colonyTemplate.position.randomOffset(
                min = COLONY_RADAR_RADIUS + 1,
                max = 5
        ))

        starSystemStore.setState(starSystemStore.state.copy(
                planets = listOf(RANDOM.except<Planet> { !it.habitable }.copy(position = playerColony.position))
        ))
        colonyStore.setState(listOf(playerColony))

        val notAStar = tested.state.tiles.filter { it.value !is StarTile }

        awaitThat {
            assertThat(notAStar.entries)
                    .hasSize(1)
                    .anyMatch {
                        (it.value as ColonyTile).colony.position == playerColony.position && it.key == playerColony.position
                    }
        }
    }

    @Test
    fun `ship is visible in ship radar range`(
            @RandomBean enemy: PlayerId,
            @RandomBean colonyTemplate: Colony,
            @RandomBean shipTemplate: Ship
    ) {
        val playerShip = shipTemplate.copy(playerId = playerId)
        val enemyShip = shipTemplate.copy(
                playerId = enemy,
                position = shipTemplate.position.randomOffset(max = playerShip.shipType.radarRadius)
        )

        shipStore.setState(mapOf(
                playerId to playerShip.id to playerShip,
                enemy to enemyShip.id to enemyShip
        ))

        val notAStar = tested.state.tiles.filter { it.value !is StarTile }

        assertThat(notAStar.size).isBetween(1, 2)

        assertThat(notAStar.entries)
                .anyMatch { tile ->
                    tile.value.ships.any {
                        it.position == enemyShip.position
                                && tile.key == enemyShip.position
                                && it.playerId == enemy
                    }
                }
                .anyMatch { tile ->
                    tile.value.ships.any {
                        it.position == playerShip.position
                                && tile.key == playerShip.position
                                && it.playerId == playerId
                    }
                }
    }

    @Test
    fun `ship is invisible beyond ship radar range`(
            @RandomBean enemy: PlayerId,
            @RandomBean colonyTemplate: Colony,
            @RandomBean shipTemplate: Ship
    ) {
        val playerShip = shipTemplate.copy(playerId = playerId)
        val enemyShip = shipTemplate.copy(
                playerId = enemy,
                position = playerShip.position.randomOffset(playerShip.shipType.radarRadius + 1, 5)
        )

        shipStore.setState(mapOf(
                playerId to playerShip.id to playerShip,
                enemy to enemyShip.id to enemyShip
        ))

        val notAStar = tested.state.tiles.filter { it.value !is StarTile }

        assertThat(notAStar.entries)
                .hasSize(1)
                .anyMatch { tile ->
                    tile.value.ships.any {
                        it.position == playerShip.position
                                && tile.key == playerShip.position
                                && it.playerId == playerId
                    }
                }
    }

    @Test
    fun `colony is visible in colony radar range`(
            @RandomBean enemy: PlayerId,
            @RandomBean colonyTemplate: Colony
    ) {
        val playerColony = colonyTemplate.copy(playerId = playerId)
        val enemyColony = colonyTemplate.copy(playerId = enemy, position = colonyTemplate.position.randomOffset(
                min = 1,
                max = COLONY_RADAR_RADIUS - 1
        ))

        starSystemStore.setState(starSystemStore.state.copy(
                planets = listOf(RANDOM.except<Planet> { !it.habitable }.copy(position = enemyColony.position))
        ))
        colonyStore.setState(listOf(playerColony, enemyColony))

        awaitThat {
            val notAStar = tested.state.tiles.filter { it.value !is StarTile }

            assertThat(notAStar.entries)
                    .hasSize(2)
                    .anyMatch { (it.value as ColonyTile).colony.position == playerColony.position && it.key == playerColony.position }
                    .anyMatch { (it.value as ColonyTile).colony.position == enemyColony.position && it.key == enemyColony.position }
        }
    }

    @Test
    fun `colony is visible in extended by tech colony radar range`(
            @RandomBean enemy: PlayerId,
            @RandomBean colonyTemplate: Colony
    ) {
        val playerColony = colonyTemplate.copy(playerId = playerId)
        val enemyColony = colonyTemplate.copy(playerId = enemy, position = colonyTemplate.position.randomOffset(
                min = 1,
                max = (COLONY_RADAR_RADIUS * TechTree[RADAR_TECH_ID]!!.multiplier).toInt() - 1
        ))

        starSystemStore.setState(starSystemStore.state.copy(
                planets = listOf(
                        RANDOM.except<Planet> { !it.habitable }.copy(position = playerColony.position),
                        RANDOM.except<Planet> { !it.habitable }.copy(position = enemyColony.position)
                )
        ))
        colonyStore.setState(listOf(playerColony, enemyColony))
        playerTechStore.setState(mapOf(playerId to listOf(RADAR_TECH_ID)))

        awaitThat {
            val notAStar = tested.state.tiles.filter { it.value !is StarTile }

            assertThat(notAStar.entries)
                    .hasSize(2)
                    .anyMatch { (it.value as ColonyTile).colony.position == playerColony.position && it.key == playerColony.position }
                    .anyMatch { (it.value as ColonyTile).colony.position == enemyColony.position && it.key == enemyColony.position }
        }
    }

    @Test
    fun `colony is invisible beyond colony radar range`(
            @RandomBean enemy: PlayerId,
            @RandomBean colonyTemplate: Colony
    ) {
        val playerColony = colonyTemplate.copy(playerId = playerId)
        val enemyColony = colonyTemplate.copy(
                playerId = enemy,
                position = colonyTemplate.position.randomOffset(COLONY_RADAR_RADIUS + 1, 5)
        )

        starSystemStore.setState(starSystemStore.state.copy(
                planets = listOf(
                        RANDOM.except<Planet> { !it.habitable }.copy(position = playerColony.position),
                        RANDOM.except<Planet> { !it.habitable }.copy(position = enemyColony.position)
                )
        ))
        colonyStore.setState(listOf(playerColony, enemyColony))

        val notAStar = tested.state.tiles.filter { it.value !is StarTile }

        awaitThat {
            assertThat(notAStar.entries)
                    .hasSize(1)
                    .anyMatch { (it.value as ColonyTile).colony.position == playerColony.position && it.key == playerColony.position }
        }
    }

    @Test
    fun `colony is visible in ship radar range`(
            @RandomBean enemy: PlayerId,
            @RandomBean colonyTemplate: Colony,
            @RandomBean shipTemplate: Ship
    ) {
        val playerShip = shipTemplate.copy(playerId = playerId)
        val enemyColony = colonyTemplate.copy(playerId = enemy, position = shipTemplate.position.randomOffset(max = playerShip.shipType.radarRadius))

        starSystemStore.setState(starSystemStore.state.copy(
                planets = listOf(RANDOM.except<Planet> { !it.habitable }.copy(position = enemyColony.position))
        ))
        colonyStore.setState(listOf(enemyColony))
        shipStore.setState(mapOf(playerId to playerShip.id to playerShip))

        val notAStar = tested.state.tiles.filter { it.value !is StarTile }

        assertThat(notAStar.size).isBetween(1, 2)

        assertThat(notAStar.entries)
                .anyMatch { it.value.ships.isNotEmpty() && it.value.ships[0].position == playerShip.position && it.key == playerShip.position }
                .anyMatch { entry ->
                    entry.value.let {
                        it is ColonyTile && it.colony.position == enemyColony.position && entry.key == enemyColony.position
                    }
                }
    }

    @Test
    fun `colony is invisible beyond ship radar range`(
            @RandomBean enemy: PlayerId,
            @RandomBean colonyTemplate: Colony,
            @RandomBean shipTemplate: Ship
    ) {
        val playerShip = shipTemplate.copy(playerId = playerId)
        val enemyColony = colonyTemplate.copy(
                playerId = enemy,
                position = playerShip.position.randomOffset(playerShip.shipType.radarRadius + 1, 5)
        )

        colonyStore.setState(listOf(enemyColony))
        shipStore.setState(mapOf(playerId to playerShip.id to playerShip))

        val colonyTiles = tested.state.tiles.filter { it.value !is StarTile }

        assertThat(colonyTiles.entries)
                .hasSize(1)
                .anyMatch { it.value.ships.isNotEmpty() && it.value.ships[0].position == playerShip.position && it.key == playerShip.position }
    }


    @Test
    fun `ship creating outside of map is forbidden`(
            @RandomBean shipTemplate: Ship
    ) {
        val ship = shipTemplate.copy(position = Position(starSystemSize.width + RANDOM.nextInt(), starSystemSize.height + RANDOM.nextInt()))

        dispatcher.action(ShipCreateAction(ship, ship.playerId))

        delayAwaitThat {
            assertThat(emittedActions.lastOrNull(ShipCreateAction::class)).isNull()
        }
    }

    @Test
    fun `ship moving outside of map is forbidden`(
            @RandomBean shipId: ShipId,
            @RandomBean playerId: PlayerId
    ) {
        dispatcher.action(ShipMoveAction(playerId, shipId, Position(starSystemSize.width + RANDOM.nextInt(), starSystemSize.height + RANDOM.nextInt())))

        delayAwaitThat {
            assertThat(emittedActions.lastOrNull(ShipMoveAction::class)).isNull()
        }
    }

    @Test
    fun `ship jumping outside of map is forbidden`(
            @RandomBean shipId: ShipId,
            @RandomBean playerId: PlayerId
    ) {
        dispatcher.action(ShipJumpAction(playerId, shipId, Position(starSystemSize.width + RANDOM.nextInt(), starSystemSize.height + RANDOM.nextInt())))

        delayAwaitThat {
            assertThat(emittedActions.lastOrNull(ShipJumpAction::class)).isNull()
        }
    }

    @Test
    fun `ship moving to impassable tile is forbidden`(
            @RandomBean shipId: ShipId,
            @RandomBean playerId: PlayerId
    ) {
        dispatcher.action(ShipMoveAction(playerId, shipId, Position(
                x = starSystem.star.position.x + RANDOM.nextInt(starSystem.star.radius),
                y = starSystem.star.position.y + RANDOM.nextInt(starSystem.star.radius)
        )))

        delayAwaitThat {
            assertThat(emittedActions.lastOrNull(ShipMoveAction::class)).isNull()
        }
    }

    @Test
    fun `ship jump to impassable tile is forbidden`(
            @RandomBean shipId: ShipId,
            @RandomBean playerId: PlayerId
    ) {
        dispatcher.action(ShipJumpAction(playerId, shipId, Position(
                x = starSystem.star.position.x + RANDOM.nextInt(starSystem.star.radius),
                y = starSystem.star.position.y + RANDOM.nextInt(starSystem.star.radius)
        )))

        delayAwaitThat {
            assertThat(emittedActions.lastOrNull(ShipJumpAction::class)).isNull()
        }
    }

    private fun Position.randomOffset(min: Int = 0, max: Int): Position =
            when (RANDOM.nextInt(4)) {
                0 -> this.copy(x = this.x - min - RANDOM.nextInt(max))
                1 -> this.copy(x = this.x + min + RANDOM.nextInt(max))
                2 -> this.copy(y = this.y - min - RANDOM.nextInt(max))
                3 -> this.copy(y = this.y + min + RANDOM.nextInt(max))
                else -> this
            }

}
