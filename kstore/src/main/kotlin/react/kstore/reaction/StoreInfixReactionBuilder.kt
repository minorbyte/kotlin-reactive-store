package react.kstore.reaction

import react.kstore.*
import react.kstore.action.Dispatcher
import kotlin.reflect.KClass

open class StoreInfixReactionBuilder<ACTION : Any, STATE : Any>(
        actionClass: KClass<ACTION>,
        store: Store<STATE>,
        dispatcher: Dispatcher
) {
    protected val reactionBuilder = StoreReactionBuilder(actionClass, store, dispatcher)

    open infix fun react(stateModifier: Reaction<STATE, ACTION>): StoreInfixReactionBuilder<ACTION, STATE> {
        reactionBuilder.react(stateModifier)
        return this
    }

    open infix fun intercept(interceptor: StateInterceptor<ACTION, STATE>): StoreInfixReactionBuilder<ACTION, STATE> {
        reactionBuilder.intercept(interceptor)
        return this
    }

    open infix fun validate(validator: ActionValidator<ACTION, STATE>): StoreInfixReactionBuilder<ACTION, STATE> {
        reactionBuilder.validate(validator)
        return this
    }

}
