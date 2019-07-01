package org.kstore.demo.stars.gameplay.model.player

import org.kstore.demo.stars.GameDescription
import org.kstore.demo.stars.gameplay.model.player.colony.*
import org.kstore.demo.stars.gameplay.model.player.resources.PlayerResourcesStore
import org.kstore.demo.stars.gameplay.model.player.resources.income.PlayerIncomeStore
import org.kstore.demo.stars.gameplay.model.player.ship.*
import org.kstore.demo.stars.gameplay.model.player.tech.*
import org.kstore.demo.stars.gameplay.model.starsystem.StarSystemStore
import org.springframework.context.annotation.*
import react.kstore.action.CommonDispatcher


@Configuration
class PlayerConfig {

    @Bean
    fun playerStore(gameDescription: GameDescription) = PlayerStore(gameDescription)

    @Bean
    fun playerResourcesStore(gameDescription: GameDescription) = PlayerResourcesStore(CommonDispatcher, gameDescription)

    @Bean
    fun colonyStore(gameDescription: GameDescription) = ColonyStore(CommonDispatcher, gameDescription)

    @Bean
    fun playerIncomeStore(gameDescription: GameDescription, colonyStore: ColonyStore, colonyBuildingsStore: ColonyBuildingsStore) =
            PlayerIncomeStore(CommonDispatcher, gameDescription, colonyStore, colonyBuildingsStore)

    @Bean
    fun colonyBuildQueueStore(gameDescription: GameDescription, colonyStore: ColonyStore, colonyBuildingsStore: ColonyBuildingsStore) =
            ColonyBuildQueueStore(CommonDispatcher, gameDescription, colonyStore, colonyBuildingsStore)

    @Bean
    fun colonyBuildingsStore(gameDescription: GameDescription) = ColonyBuildingsStore(CommonDispatcher, gameDescription)


    @Bean
    fun colonyIncomeStore(
            colonyBuildingsStore: ColonyBuildingsStore,
            colonyStore: ColonyStore,
            playerTechStore: PlayerTechStore
    ) = ColonyIncomeStore(CommonDispatcher, colonyBuildingsStore, colonyStore, playerTechStore)

    @Bean
    fun playerShipsStore(gameDescription: GameDescription, playerTechStore: PlayerTechStore, starSystemStore: StarSystemStore) =
            PlayerShipsStore(CommonDispatcher, gameDescription, playerTechStore, starSystemStore)

    @Bean
    fun shipBattleProcessor() = ShipBattleProcessor(CommonDispatcher)

    @Bean
    fun shipFuelConsumptionValidator(
            playerResourcesStore: PlayerResourcesStore,
            shipsStore: PlayerShipsStore
    ) = ShipFuelConsumptionValidator(playerResourcesStore, shipsStore)

    @Bean
    fun playerTechStore(gameDescription: GameDescription) = PlayerTechStore(CommonDispatcher, gameDescription)

    @Bean
    fun playerResearchingTechStore(gameDescription: GameDescription) = PlayerResearchingTechStore(CommonDispatcher, gameDescription)

    @Bean
    fun shipRepairValidator(playerShipsStore: PlayerShipsStore, colonyStore: ColonyStore, colonyBuildingsStore: ColonyBuildingsStore) =
            ShipRepairValidator(CommonDispatcher, playerShipsStore, colonyStore, colonyBuildingsStore)

    @Bean
    fun bombColonyValidator(playerShipsStore: PlayerShipsStore, colonyStore: ColonyStore) =
            BombColonyValidator(CommonDispatcher, playerShipsStore, colonyStore)

}
