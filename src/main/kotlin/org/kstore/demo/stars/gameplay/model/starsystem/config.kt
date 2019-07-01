package org.kstore.demo.stars.gameplay.model.starsystem

import org.kstore.demo.stars.GameDescription
import org.springframework.context.annotation.*


@Configuration
class StarSystemConfig {

    @Bean
    fun starSystemStore(gameDescription: GameDescription) = StarSystemStore(gameDescription)

}
