package react.kstore.selection

import react.kstore.*
import react.kstore.reaction.Validation.Companion.condition
import react.kstore.reaction.on
import java.util.*
import kotlin.reflect.KClass

data class ListItemSelectedAction<STATE : Any>(val item: STATE)
interface DeselectListItemAction
interface SelectNextListItemAction
interface SelectPreviousListItemAction
interface SelectExactListItemAction {
    val index: Int
}

open class ListItemStoreSelection<STATE : Any>(
        store: Store<List<STATE>>,
        selectNextActionClass: KClass<out SelectNextListItemAction>? = null,
        prevActionClass: KClass<out SelectPreviousListItemAction>? = null,
        selectActionClass: KClass<out SelectExactListItemAction>? = null,
        deselectActionClass: KClass<out DeselectListItemAction>? = null
) : OptionalStoreSelection<STATE, List<STATE>, Optional<Int>>(
        store = store,
        selectorStore = BasicStore(
                initialState = Optional.empty(),
                reactions = {
                    if (selectNextActionClass != null)
                        on(selectNextActionClass) update { state, _ ->
                            if (store.state.isEmpty()) Optional.empty()
                            else
                                if (state.isPresent && state.get() < store.state.size - 1) Optional.of(state.get() + 1)
                                else Optional.of(0)
                        }

                    if (prevActionClass != null)
                        on(prevActionClass) update { state, _ ->
                            if (store.state.isEmpty()) Optional.empty()
                            else
                                if (state.isPresent && state.get() > 0) Optional.of(state.get() - 1)
                                else Optional.of(store.state.size - 1)
                        }

                    if (selectActionClass != null)
                        on(selectActionClass) update { _, action ->
                            Optional.ofNullable(action.index)
                        }

                    if (deselectActionClass != null)
                        on(deselectActionClass) update { _, _ ->
                            Optional.empty()
                        }
                }
        )
) {

    init {
        if (selectActionClass != null)
            store.on(selectActionClass) validate { state, action ->
                condition(
                        state.size > action.index,
                        "Invalid index ${action.index}. Current size ${state.size}"
                )
            }
    }

    override fun select(storeState: List<STATE>, selector: Optional<Int>): Optional<STATE> {
        return if (selector.isPresent && storeState.size > selector.get()) {
            Optional.ofNullable(storeState[selector.get()])
        } else {
            Optional.empty()
        }
    }
}
