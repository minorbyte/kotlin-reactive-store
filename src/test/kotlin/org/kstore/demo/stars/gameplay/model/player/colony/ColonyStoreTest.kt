package org.kstore.demo.stars.gameplay.model.player.colony

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kstore.*
import org.kstore.demo.stars.*
import org.kstore.demo.stars.gameplay.model.*
import org.kstore.demo.stars.gameplay.model.starsystem.Planet
import org.kstore.demo.stars.rule.starsystem.PlanetType.*

@StarsTest
internal class ColonyStoreTest : GameDescriptionEnvironmentTest() {


    @Test
    fun `initial store state is taken from gameDescription`() {
        val tested = ColonyStore(dispatcher, gameDescription)
        assertThat(tested.state).containsExactlyInAnyOrderElementsOf(
                gameDescription.players.flatMap { player ->
                    player.colonies.map { colony ->
                        val planet = gameDescription.starSystem.planets.first { colony.position == it.position }

                        Colony(
                                id = colony.id,
                                position = colony.position,
                                name = colony.name,
                                playerId = player.id,
                                planetSize = translatePlanetSize(planet.planetType),
                                planetMaxBuildings = planet.planetType.maxBuildings,
                                planetResourceMultiplier = planet.resourceMultiplier
                        )
                    }
                }
        )
    }

    @Test
    fun `build colony added to state`(@RandomBean actionTemplate: BuildColonyAction) {
        val action = actionTemplate.copy(
                habitable = actionTemplate.habitable.copy(
                        planetType = RANDOM.next(MOON, SMALL, STANDARD)
                )
        )

        gameDescription = gameDescription.copy(
                players = gameDescription.players.map {
                    it.copy(colonies = listOf())
                }
        )

        val tested = ColonyStore(dispatcher, gameDescription)

        dispatcher.action(action.copy(playerId = gameDescription.currentPlayerId))

        awaitThat {
            assertThat(tested.state).contains(Colony(
                    playerId = gameDescription.currentPlayerId,
                    position = action.position,
                    name = action.name,
                    id = action.id,
                    planetResourceMultiplier = action.habitable.resourceMultiplier,
                    planetMaxBuildings = action.habitable.maxBuildings,
                    planetSize = translatePlanetSize(action.habitable.planetType)
            ))
        }
    }

    @Test
    fun `can't build two colony at one planet`(@RandomBean action: BuildColonyAction) {
        val colony = gameDescription.currentPlayer().colonies.first()
        val habitable = gameDescription.starSystem.planets.first { it.position == colony.position }.let {
            Planet(
                    id = it.id,
                    position = it.position,
                    title = it.title,
                    planetType = it.planetType,
                    resourceMultiplier = it.resourceMultiplier
            )
        }

        val tested = ColonyStore(dispatcher, gameDescription)

        dispatcher.action(action.copy(playerId = gameDescription.currentPlayerId, habitable = habitable))

        awaitThat {
            assertThat(emittedActions.lastOrNull(BuildColonyAction::class)).isNull()
            assertThat(tested.state).noneMatch { it.id == action.id }
        }
    }

    @Test
    fun `destroy enemy colony`() {
        val playerWithColony = gameDescription.players.randomItemButNot(gameDescription.currentPlayer())
        val colony = playerWithColony.colonies.randomItem()

        val tested = ColonyStore(dispatcher, gameDescription)

        dispatcher.action(DestroyColonyAction(byPlayerId = gameDescription.currentPlayerId, id = colony.id))

        awaitThat {
            assertThat(tested.state).noneMatch { it.id == colony.id }
        }
    }

    @Test
    fun `can't destroy own colony`() {
        val colony = gameDescription.currentPlayer().colonies.randomItem()

        val tested = ColonyStore(dispatcher, gameDescription)

        dispatcher.action(DestroyColonyAction(byPlayerId = gameDescription.currentPlayerId, id = colony.id))

        awaitThat {
            assertThat(tested.state).anyMatch { it.id == colony.id }
        }
    }
}
