package org.kstore.demo.stars.common.menu

import react.kstore.BasicStore
import react.kstore.reaction.on
import react.kstore.selection.*
import kotlin.reflect.KClass

open class BasicMenuSelectedItemStore<DESELECT_CLASS : DeselectListItemAction>(
        val items: Map<String, () -> Any>,
        prevItemClass: KClass<out SelectPreviousListItemAction>,
        nextItemClass: KClass<out SelectNextListItemAction>,
        showMenuClass: KClass<out Any>,
        runItem: KClass<out Any>,
        deselectMenuActionClass: KClass<DESELECT_CLASS>,
        deselectMenuActionBuilder: () -> DESELECT_CLASS
) : ListItemStoreSelection<String>(
        store = BasicStore(items.keys.toList()),
        selectNextActionClass = nextItemClass,
        prevActionClass = prevItemClass,
        deselectActionClass = deselectMenuActionClass
) {

    init {
        on(showMenuClass) react { _, _ ->
            dispatcher.action(deselectMenuActionBuilder())
        }
        on(runItem) react { state, _ ->
            state.ifPresent { itemKey ->
                dispatcher.asyncAction(items[itemKey]!!())
            }
        }
    }

}
