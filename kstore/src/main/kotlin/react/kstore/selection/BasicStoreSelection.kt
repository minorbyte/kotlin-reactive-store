package react.kstore.selection

import react.kstore.*
import rx.Observable
import rx.subjects.PublishSubject

abstract class BasicStoreSelection<STATE : Any, FULL_STATE : Any, REDUCER : Any>(
        protected val store: Subscribable<FULL_STATE>,
        protected val selectorStore: UpdateableStore<REDUCER>,
        initialState: STATE
) : BasicStore<STATE>(initialState) {

    private val combineSubject = PublishSubject.create<Pair<FULL_STATE, REDUCER>>()

    init {
        Observable.combineLatest(store.observable, selectorStore.observable) { t1, t2 ->
            Pair(t1, t2)
        }.subscribe {
            try {
                update(select(it.first, it.second))
            } catch (e: Exception) {
                dispatcher.action(FailedMergeUpdate(it, e))
            }
        }
    }

    abstract fun select(storeState: FULL_STATE, selector: REDUCER): STATE

}
