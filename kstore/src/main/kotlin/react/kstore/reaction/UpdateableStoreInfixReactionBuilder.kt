package react.kstore.reaction

import react.kstore.*
import react.kstore.action.Dispatcher
import kotlin.reflect.KClass


class UpdateableStoreInfixReactionBuilder<ACTION : Any, STATE : Any>(
        actionClass: KClass<ACTION>,
        store: UpdateableStore<STATE>,
        dispatcher: Dispatcher
) : StoreInfixReactionBuilder<ACTION, STATE>(actionClass, store, dispatcher) {

    infix fun update(stateModifier: StateModifier<STATE, ACTION>): UpdateableStoreInfixReactionBuilder<ACTION, STATE> {
        reactionBuilder.update(stateModifier)
        return this
    }

    override infix fun react(stateModifier: Reaction<STATE, ACTION>): UpdateableStoreInfixReactionBuilder<ACTION, STATE> {
        super.react(stateModifier)
        return this
    }

    override infix fun intercept(interceptor: StateInterceptor<ACTION, STATE>): UpdateableStoreInfixReactionBuilder<ACTION, STATE> {
        super.intercept(interceptor)
        return this
    }

    override infix fun validate(validator: ActionValidator<ACTION, STATE>): UpdateableStoreInfixReactionBuilder<ACTION, STATE> {
        super.validate(validator)
        return this
    }

}

