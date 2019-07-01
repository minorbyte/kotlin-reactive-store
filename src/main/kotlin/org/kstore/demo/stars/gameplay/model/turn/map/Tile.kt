package org.kstore.demo.stars.gameplay.model.turn.map

import org.kstore.demo.stars.gameplay.model.player.colony.Colony
import org.kstore.demo.stars.gameplay.model.player.ship.Ship
import org.kstore.demo.stars.gameplay.model.starsystem.*

sealed class Tile(
        open val ships: List<Ship> = listOf()
)

data class SpaceTile(
        override val ships: List<Ship> = listOf()
) : Tile()

data class StarTile(
        val star: Star
) : Tile()

data class PlanetTile(
        val planet: Planet,
        override val ships: List<Ship> = listOf()
) : Tile()

data class ColonyTile(
        val planet: Planet,
        val colony: Colony,
        override val ships: List<Ship> = listOf()
) : Tile()
