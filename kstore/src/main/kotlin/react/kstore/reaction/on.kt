package react.kstore.reaction

import react.kstore.*
import react.kstore.action.Dispatcher
import kotlin.reflect.KClass


fun <ACTION : Any, STATE : Any> UpdateableStore<STATE>.on(actionClass: KClass<ACTION>): UpdateableStoreInfixReactionBuilder<ACTION, STATE> {
    return UpdateableStoreInfixReactionBuilder(actionClass, this, this.dispatcher)
}

fun <ACTION : Any, STATE : Any> Store<STATE>.on(actionClass: KClass<ACTION>): StoreInfixReactionBuilder<ACTION, STATE> {
    return StoreInfixReactionBuilder(actionClass, this, this.dispatcher)
}

fun <ACTION : Any, STATE : Any> Store<STATE>.on(actionClass: KClass<ACTION>, function: StoreReactionBuilder<ACTION, STATE>.() -> Unit) {
    val builder = StoreReactionBuilder(actionClass, this, this.dispatcher)
    builder.function()
}

fun <ACTION : Any, STATE : Any> UpdateableStore<STATE>.on(dispatcher: Dispatcher, actionClass: KClass<ACTION>):
        UpdateableStoreInfixReactionBuilder<ACTION, STATE> {
    return UpdateableStoreInfixReactionBuilder(actionClass, this, dispatcher)
}

fun <ACTION : Any, STATE : Any> Store<STATE>.on(dispatcher: Dispatcher, actionClass: KClass<ACTION>): StoreInfixReactionBuilder<ACTION, STATE> {
    return StoreInfixReactionBuilder(actionClass, this, dispatcher)
}

fun <ACTION : Any, STATE : Any> Store<STATE>.on(dispatcher: Dispatcher, actionClass: KClass<ACTION>, function: StoreReactionBuilder<ACTION, STATE>.() -> Unit) {
    val builder = StoreReactionBuilder(actionClass, this, dispatcher)
    builder.function()
}
