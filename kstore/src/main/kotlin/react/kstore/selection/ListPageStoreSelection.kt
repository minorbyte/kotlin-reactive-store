package react.kstore.selection

import react.kstore.*
import react.kstore.reaction.Validation.Companion.condition
import react.kstore.reaction.on
import java.util.*
import kotlin.reflect.KClass
import kotlin.streams.toList

interface DeselectPageItemAction
interface SelectNextListPageAction
interface SelectPreviousListPageAction
interface SelectExactListPageAction {
    val index: Int
}

data class Paged<out ITEM>(
        val total: Int,
        val page: List<ITEM>
)

fun pageCount(size: Int, pageSize: Int) = size / pageSize + if (size % pageSize == 0) 0 else 1

open class ListPageStoreSelection<STATE : Any>(
        private val pageSize: Int,
        store: Store<List<STATE>>,
        selectNextActionClass: KClass<out SelectNextListPageAction>? = null,
        prevActionClass: KClass<out SelectPreviousListPageAction>? = null,
        selectActionClass: KClass<out SelectExactListPageAction>? = null,
        deselectActionClass: KClass<out DeselectPageItemAction>? = null
) : OptionalStoreSelection<Paged<STATE>, List<STATE>, Optional<Int>>(
        store = store,
        selectorStore = BasicStore(
                initialState = Optional.empty(),
                reactions = {
                    if (selectNextActionClass != null)
                        on(selectNextActionClass) update { state, _ ->
                            if (store.state.isEmpty()) Optional.empty()
                            else
                                if (state.isPresent && state.get() < pageCount(store.state.size, pageSize) - 1) Optional.of(state.get()
                                        + 1)
                                else Optional.of(0)
                        }

                    if (prevActionClass != null)
                        on(prevActionClass) update { state, _ ->
                            if (store.state.isEmpty()) Optional.empty()
                            else
                                if (state.isPresent && state.get() > 0) Optional.of(pageCount(store.state.size, pageSize) - 1)
                                else Optional.of(pageCount(store.state.size, pageSize) - 1)
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
                        pageCount(state.size, pageSize) > action.index,
                        "Invalid page ${action.index}. Current size ${state.size}, page size $pageSize"
                )
            }
    }

    override fun select(storeState: List<STATE>, selector: Optional<Int>): Optional<Paged<STATE>> {
        return if (selector.isPresent) {
            Optional.of(
                    Paged(
                            total = storeState.size,
                            page = storeState.stream().skip(selector.get() * pageSize.toLong()).limit(pageSize.toLong()).toList())
            )
        } else {
            Optional.empty()
        }
    }
}
