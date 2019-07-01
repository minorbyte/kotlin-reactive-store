package org.kstore.demo.stars.gameplay.model

import org.junit.jupiter.api.BeforeEach
import org.kstore.demo.stars.TestGameDescription


internal open class GameDescriptionEnvironmentTest : DispatcherEnvironmentTest() {

    lateinit var gameDescription: TestGameDescription

    @BeforeEach
    fun beforeGameDescription() {
        gameDescription = TestGameDescription()
    }

}
