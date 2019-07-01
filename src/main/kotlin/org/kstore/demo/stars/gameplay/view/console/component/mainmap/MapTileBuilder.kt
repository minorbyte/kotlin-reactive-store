package org.kstore.demo.stars.gameplay.view.console.component.mainmap

import org.kstore.demo.stars.common.ConsoleColor
import org.kstore.demo.stars.common.ConsoleColor.*
import org.kstore.demo.stars.gameplay.model.player.PlayerId
import org.kstore.demo.stars.gameplay.model.turn.map.*
import org.kstore.demo.stars.rule.starsystem.PlanetType

internal object MapTileBuilder : ((Tile, PlayerId) -> TileChar) {

    override fun invoke(tile: Tile, currentPlayerId: PlayerId): TileChar = TileChar(renderTile(tile), renderColor(tile, currentPlayerId)
            , WHITE, false)

    private fun renderColor(tile: Tile, currentPlayerId: PlayerId): ConsoleColor =
            when {
                tile.ships.any { it.playerId != currentPlayerId } -> RED
                tile is ColonyTile && tile.colony.playerId != currentPlayerId -> RED
                tile.ships.any { it.playerId == currentPlayerId } -> GREEN
                tile is ColonyTile && tile.colony.playerId == currentPlayerId -> GREEN
                else -> WHITE
            }

    private fun renderTile(tile: Tile): Char {
        if (tile.ships.isNotEmpty()) {
            return '>'
        }

        return when (tile) {
            is StarTile -> '*'
            is PlanetTile -> when (tile.planet.planetType) {
                PlanetType.GAS_GIANT_RINGED -> '@'
                PlanetType.GAS_GIANT -> 'G'
                PlanetType.STANDARD -> 'O'
                PlanetType.SMALL -> 'o'
                PlanetType.MOON -> 'm'
            }
            else -> ' '
        }
    }

}
