package org.kstore.demo.stars.gameplay.model

import org.junit.jupiter.api.*
import org.kstore.action.EmittedActions
import react.kstore.action.*


internal open class DispatcherEnvironmentTest {

    lateinit var dispatcher: Dispatcher
    lateinit var emittedActions: EmittedActions

    @BeforeEach
    fun beforeDispatcher() {
        dispatcher = DispatcherImpl()
        emittedActions =  EmittedActions(dispatcher)
    }

    @AfterEach
    fun afterDispatcher() {
        dispatcher.stop()
    }
}
