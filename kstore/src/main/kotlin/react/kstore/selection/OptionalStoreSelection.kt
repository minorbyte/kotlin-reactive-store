package react.kstore.selection

import react.kstore.*
import java.util.*

abstract class OptionalStoreSelection<STATE : Any, FULL_STATE : Any, REDUCER : Any>(
        store: Subscribable<FULL_STATE>,
        selectorStore: UpdateableStore<REDUCER>
) : BasicStoreSelection<Optional<STATE>, FULL_STATE, REDUCER>(
        store,
        selectorStore,
        Optional.empty<STATE>()
) {

    abstract override fun select(storeState: FULL_STATE, selector: REDUCER): Optional<STATE>

}
