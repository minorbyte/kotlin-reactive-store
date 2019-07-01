package react.kstore

import react.kstore.action.Dispatcher
import rx.Observable

internal class ReducedState<STATE : Any>(
        override val observable: Observable<STATE>,
        dispatcher: Dispatcher
) : Subscribable<STATE>(dispatcher)
