package react.kstore.reaction

import mu.KotlinLogging
import react.kstore.*
import react.kstore.action.*
import kotlin.reflect.KClass

open class StoreReactionBuilder<ACTION : Any, STATE : Any>(
        private val actionClass: KClass<ACTION>,
        private val store: Store<STATE>,
        private val dispatcher: Dispatcher
) {
    protected val logger = KotlinLogging.logger(store::class.qualifiedName!!)

    fun update(stateModifier: StateModifier<STATE, ACTION>) {
        if (store !is UpdateableStore) {
            throw IllegalArgumentException("Isn`t updateable store")
        }

        logger.logInfo { "Registering ${actionClass.simpleName} state update" }
        dispatcher.subscribe(actionClass) { action ->
            store.update(stateModifier.invoke(store.state, action))
        }
    }

    fun react(stateModifier: Reaction<STATE, ACTION>) {
        logger.logInfo { "Registering ${actionClass.simpleName} reaction" }
        dispatcher.subscribe(actionClass) { action ->
            stateModifier.invoke(store.state, action)
        }
    }

    fun intercept(interceptor: StateInterceptor<ACTION, STATE>) {
        logger.logInfo { "Registering ${actionClass.simpleName} interception" }
        registerInterceptor { action -> interceptor(store.state, action) }
    }

    fun validate(validator: ActionValidator<ACTION, STATE>) {
        logger.logInfo { "Registering ${actionClass.simpleName} interception" }
        registerInterceptor { action ->
            val validation = validator(store.state, action)

            if (!validation.success) FailedActionValidation(action, validation.message)
            else action
        }
    }

    fun validate(message: String, validCondition: (STATE, ACTION) -> Boolean) {
        logger.logInfo { "Registering ${actionClass.simpleName} interception" }
        registerInterceptor { action ->
            val validation = Validation.condition(
                    validCondition(store.state, action),
                    message
            )

            if (!validation.success) FailedActionValidation(action, validation.message)
            else action
        }
    }

    fun validate(message: (STATE, ACTION) -> String, validCondition: (STATE, ACTION) -> Boolean) {
        logger.logInfo { "Registering ${actionClass.simpleName} interception" }
        registerInterceptor { action ->
            val validation = Validation.condition(
                    validCondition(store.state, action),
                    message(store.state, action)
            )

            if (!validation.success) FailedActionValidation(action, validation.message)
            else action
        }
    }

    private fun registerInterceptor(interceptor: Interceptor<ACTION>) {
        store.registerInterceptorUnsubscribe(dispatcher.intercept(actionClass, interceptor))
    }

}
