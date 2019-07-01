package react.kstore.reaction

import react.kstore.UpdateableStore
import react.kstore.action.Dispatcher
import kotlin.reflect.KClass

open class ConstructorUpdateableStoreReaction<STATE : Any>(
        private val store: UpdateableStore<STATE>
) {

    fun <ACTION : Any> on(actionClass: KClass<ACTION>): UpdateableStoreInfixReactionBuilder<ACTION, STATE> {
        return UpdateableStoreInfixReactionBuilder(actionClass, store, store.dispatcher)
    }

    fun <ACTION : Any> on(actionClass: KClass<ACTION>, function: StoreReactionBuilder<ACTION, STATE>.() -> Unit) {
        val builder = StoreReactionBuilder(actionClass, store, store.dispatcher)
        builder.function()
    }

    fun <ACTION : Any> on(dispatcher: Dispatcher, actionClass: KClass<ACTION>): UpdateableStoreInfixReactionBuilder<ACTION, STATE> {
        return UpdateableStoreInfixReactionBuilder(actionClass, store, dispatcher)
    }

    fun <ACTION : Any> on(dispatcher: Dispatcher, actionClass: KClass<ACTION>, function: StoreReactionBuilder<ACTION, STATE>.() -> Unit) {
        val builder = StoreReactionBuilder(actionClass, store, dispatcher)
        builder.function()
    }

}

