package org.kstore.demo.stars.gameplay.model.turn

import org.kstore.demo.stars.GameDescription
import org.kstore.demo.stars.gameplay.model.player.PlayerStore
import org.kstore.demo.stars.gameplay.model.player.colony.ColonyStore
import org.kstore.demo.stars.gameplay.model.player.resources.income.PlayerIncomeStore
import org.kstore.demo.stars.gameplay.model.player.ship.PlayerShipsStore
import org.kstore.demo.stars.gameplay.model.player.tech.PlayerTechStore
import org.kstore.demo.stars.gameplay.model.starsystem.StarSystemStore
import org.kstore.demo.stars.gameplay.model.turn.map.PlayerTurnMapStore
import org.kstore.demo.stars.gameplay.model.turn.stage.*
import org.springframework.context.annotation.*
import react.kstore.action.CommonDispatcher


@Configuration
class TurnConfig {

    @Bean(destroyMethod = "stop")
    fun turnStore(gameDescription: GameDescription): TurnStore = TurnStore(gameDescription)

    @Bean(destroyMethod = "stop")
    fun playerTurnStore(gameDescription: GameDescription, turnStore: TurnStore) =
            PlayerTurnStore(gameDescription, turnStore)

    @Bean(destroyMethod = "stop")
    fun playerTurnStageStore(gameDescription: GameDescription, playerTurnStore: PlayerTurnStore) =
            PlayerTurnStageStore(gameDescription, playerTurnStore)

    @Bean
    fun playerTurnStageProcessor(
            playersTurnTurnStore: PlayerTurnStore,
            colonyStore: ColonyStore,
            playerIncomeStore: PlayerIncomeStore
    ) =
            PlayerTurnStageProcessor(CommonDispatcher, playersTurnTurnStore, colonyStore, playerIncomeStore)

    @Bean(destroyMethod = "stop")
    fun playerTurnShipMovementStore(gameDescription: GameDescription, playerShipStore: PlayerTurnShipStore) =
            PlayerTurnShipMovementStore(gameDescription, playerShipStore)

    @Bean(destroyMethod = "stop")
    fun playerTurnShipStore(playerTurnStore: PlayerTurnStore, shipStore: PlayerShipsStore) = PlayerTurnShipStore(playerTurnStore, shipStore)

    @Bean(destroyMethod = "stop")
    fun playerTurnMapStore(
            playerTurnStore: PlayerTurnStore,
            starSystemStore: StarSystemStore,
            shipStore: PlayerShipsStore,
            colonyStore: ColonyStore,
            playerTechStore: PlayerTechStore
    ) = PlayerTurnMapStore(CommonDispatcher, playerTurnStore, starSystemStore, shipStore, colonyStore, playerTechStore)

}
