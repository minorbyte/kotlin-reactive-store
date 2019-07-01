package react.kstore

import mu.KotlinLogging
import react.kstore.action.Dispatcher
import rx.Observable

abstract class Subscribable<STATE : Any>(
        val dispatcher: Dispatcher
) {

    protected val logger = KotlinLogging.logger(this::class.qualifiedName!!)

    abstract val observable: Observable<STATE>

    open fun subscribe(subscribe: Subscription<STATE>) {
        logger.logInfo { "Someone subscribed by subscribe" }
        observable.subscribe {
            try {
                subscribe(it)
            } catch (e: Exception) {
                dispatcher.action(FailedSubscription(it, e))
            }
        }
    }

    open fun <REDUCED : Any> reduce(reduce: (STATE) -> REDUCED): Subscribable<REDUCED> {
        return ReducedState(observable.map { reduce(it) }, dispatcher)
    }

    open fun filter(filter: (STATE) -> Boolean): Subscribable<STATE> {
        return ReducedState(observable.filter { filter(it) }, dispatcher)
    }
}

