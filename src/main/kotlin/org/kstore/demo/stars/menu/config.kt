package org.kstore.demo.stars.menu

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.kstore.demo.stars.GameDescription
import org.kstore.demo.stars.rule.map.alphaCentauriNewGameDescription
import org.springframework.context.annotation.*

@Configuration
@ComponentScan("org.kstore.demo.stars.menu")
class MenuConfig {

    @Bean
    fun gameDescriptions(): List<GameDescription> = listOf(alphaCentauriNewGameDescription.copy())

    @Bean
    fun objectMapper() = ObjectMapper().registerKotlinModule()
}
