package org.kstore.demo.stars.gameplay.save

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.context.annotation.*


@Configuration
@ComponentScan("org.kstore.demo.stars.gameplay.save")
class SaveGameConfig {

    @Bean
    fun objectMapper() = ObjectMapper().registerKotlinModule()
}
