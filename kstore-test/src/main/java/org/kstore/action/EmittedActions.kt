package org.kstore.action

import react.kstore.action.*
import java.util.*
import kotlin.reflect.KClass

class EmittedActions(
        dispatcher: Dispatcher = CommonDispatcher,
        private val actions: LinkedList<Any> = LinkedList()
) : Collection<Any> by actions {

    init {
        dispatcher.subscribe(Any::class) {
            actions.add(it)
        }
    }

    fun <T : Any> last(kclass: KClass<T>): T {
        return actions.last { kclass.isInstance(it) } as T
    }

    fun <T : Any> lastOrNull(kclass: KClass<T>): T? {
        return actions.lastOrNull { kclass.isInstance(it) } as T?
    }

    fun <T : Any> none(kclass: KClass<T>): Boolean {
        return actions.none { kclass.isInstance(it) }
    }

    fun last(count: Int) = actions.takeLast(count)

    fun <T : Any> last(count: Int, kclass: KClass<T>) = actions.filter { kclass.isInstance(it) }.takeLast(count)

    fun clear() = actions.clear()
}

