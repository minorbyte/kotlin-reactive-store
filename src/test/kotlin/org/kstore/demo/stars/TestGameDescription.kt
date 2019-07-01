package org.kstore.demo.stars

import org.kstore.demo.stars.common.*
import org.kstore.demo.stars.rule.PlayerTurnStageType
import org.kstore.demo.stars.rule.blueprint.*
import org.kstore.demo.stars.rule.ship.ShipType
import org.kstore.demo.stars.rule.tech.TechTree
import org.kstore.randomItem


class TestGameDescription(
        name: String? = null,
        currentTurn: Int? = null,
        currentPlayerTurnStage: PlayerTurnStageType? = null,
        starSystem: StarSystemDescription? = null,
        players: List<PlayerDescription>? = null,
        currentPlayerId: PlayerDescriptionId? = null,
        currentPlayerTurnShipMovements: List<Pair<ShipDescriptionId, Int>>? = null
) : GameDescription {

    private val area = Area(RANDOM.nextInt(Int.MAX_VALUE), RANDOM.nextInt(Int.MAX_VALUE))

    override var name: String = name ?: RANDOM.nextString()
    override var currentTurn: Int = 0
    override lateinit var currentPlayerTurnStage: PlayerTurnStageType
    override lateinit var starSystem: StarSystemDescription
    override lateinit var players: List<PlayerDescription>
    override lateinit var currentPlayerId: PlayerDescriptionId
    override lateinit var currentPlayerTurnShipMovements: List<Pair<ShipDescriptionId, Int>>

    init {
        this.currentTurn = currentTurn ?: RANDOM.nextInt(Int.MAX_VALUE)

        this.currentPlayerTurnStage = currentPlayerTurnStage ?: RANDOM.nextObject()

        this.starSystem = starSystem ?: StarSystemDescription(
                name = RANDOM.nextString(),
                size = area,
                star = StarDescription(
                        id = RANDOM.nextString(),
                        position = randomPositionInArea(area),
                        title = RANDOM.nextString(),
                        radius = RANDOM.nextInt(5)
                ),
                planets = (0..RANDOM.nextInt(2, 10)).fold(listOf()) { planets, _ ->
                    planets.plus(
                            RANDOM.except({ value ->
                                planets.any { it.position == value.position } || (planets.isEmpty() && !value.planetType.habitable)
                            }, {
                                RANDOM.nextObject(PlanetDescription::class)
                            })
                    )
                }
        )

        this.players = players ?: (0..RANDOM.nextInt(2, 5)).fold(listOf()) { players, _ ->
            players.plus(
                    PlayerDescription(
                            name = RANDOM.nextString(),
                            id = RANDOM.nextString(),
                            ai = RANDOM.nextBoolean(),
                            moveOrder = RANDOM.except({ order -> players.any { it.moveOrder == order } }, { RANDOM.nextInt() }),
                            fuel = RANDOM.nextInt(Int.MAX_VALUE / 2),
                            resources = RANDOM.nextInt(Int.MAX_VALUE / 2),
                            techs = (0..RANDOM.nextInt(3)).fold(listOf()) { acc, _ ->
                                acc.plus(TechTree.values.filter { it.prerequisite.empty }.randomItem().techId)
                            },
                            colonies = emptyList(),
                            researchedTech = null,
                            ships = (0..RANDOM.nextInt(1, 5)).map {
                                val shipType = RANDOM.nextObject(ShipType::class)
                                ShipDescription(
                                        id = RANDOM.nextString(),
                                        position = randomPositionInArea(area),
                                        shipType = shipType,
                                        hp = RANDOM.nextInt(shipType.maxHp)
                                )
                            }
                    ).let { player ->
                        player.copy(
                                researchedTech = {
                                    val randomTech = RANDOM.except({
                                        player.techs.contains(it.techId)
                                    }, {
                                        RANDOM.next(TechTree.values.toList().randomItem())
                                    })

                                    if (RANDOM.nextBoolean()) null else Pair(
                                            first = randomTech.techId,
                                            second = RANDOM.nextInt(randomTech.cost - 1)
                                    )
                                }(),
                                colonies = (0..RANDOM.nextInt(1, 3)).fold(listOf()) { colonies, _ ->
                                    val planet = this.starSystem.planets.randomItem { !it.planetType.habitable }

                                    colonies.plus(
                                            ColonyDescription(
                                                    id = RANDOM.nextString(),
                                                    name = RANDOM.nextString(),
                                                    position = planet.position,
                                                    buildQueue = (0..RANDOM.nextInt(3)).fold(listOf()) { queue, _ ->
                                                        queue.plus(Blueprints.values.toList().randomItem().blueprintId to 2)
                                                    },
                                                    buildings = (0..RANDOM.nextInt(3)).fold(listOf()) { buildings, _ ->
                                                        buildings.plus(Blueprints.values
                                                                .filter { it is BuildingBlueprint }
                                                                .map { it as BuildingBlueprint }
                                                                .randomItem()
                                                                .blueprintId to RANDOM.nextString())
                                                    }
                                            )
                                    )
                                }
                        )

                    }
            )
        }
        this.currentPlayerId = currentPlayerId ?: this.players.randomItem().id

        this.currentPlayerTurnShipMovements = currentPlayerTurnShipMovements ?: currentPlayer().ships.map {
            Pair(it.id, RANDOM.nextInt(it.shipType.moves + 1))
        }
    }

    fun currentPlayer() = players.first { it.id == currentPlayerId }

    fun copy(
            name: String = this.name,
            currentTurn: Int = this.currentTurn,
            currentPlayerTurnStage: PlayerTurnStageType = this.currentPlayerTurnStage,
            starSystem: StarSystemDescription = this.starSystem,
            players: List<PlayerDescription> = this.players,
            currentPlayerId: PlayerDescriptionId = this.currentPlayerId,
            currentPlayerTurnShipMovements: List<Pair<ShipDescriptionId, Int>> = this.currentPlayerTurnShipMovements
    ) = TestGameDescription(name, currentTurn, currentPlayerTurnStage, starSystem, players, currentPlayerId, currentPlayerTurnShipMovements)

    inline fun mapAllPlayers(map: (PlayerDescription) -> PlayerDescription) = copy(
            players = players.map(map)
    )

    inline fun mapAllColonies(map: (PlayerDescription, ColonyDescription) -> ColonyDescription) = copy(
            players = players.map { player ->
                player.copy(
                        colonies = player.colonies.map { map(player, it) }
                )
            }
    )
}

fun randomPositionInArea(area: Area) = Position(RANDOM.nextInt(area.width), RANDOM.nextInt(area.height))
