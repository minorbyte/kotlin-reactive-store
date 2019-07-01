package react.kstore

import react.kstore.action.Dispatcher

abstract class Store<STATE : Any>(
        dispatcher: Dispatcher
) : Subscribable<STATE>(dispatcher) {

    abstract val state: STATE
    protected var interceptors: List<react.kstore.action.Subscription> = listOf()

    internal fun registerInterceptorUnsubscribe(subscription: react.kstore.action.Subscription) {
        interceptors = interceptors.plus(subscription)
    }

}


