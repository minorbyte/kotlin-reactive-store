package react.kstore.action

import kotlin.reflect.KClass

fun action(action: Any) = CommonDispatcher.action(action)
fun asyncAction(action: Any) = CommonDispatcher.asyncAction(action)

interface Dispatcher {
    fun <ACTION : Any> action(action: ACTION)
    fun <ACTION : Any> asyncAction(action: ACTION)

    fun <ACTION : INTERCEPTED, INTERCEPTED : Any> intercept(actionClass: KClass<ACTION>, interceptor: Interceptor<INTERCEPTED>): Subscription

    fun <ACTION : Any> subscribe(actionClass: KClass<ACTION>, subscription: (ACTION) -> Unit): Subscription

    fun stop()
}


