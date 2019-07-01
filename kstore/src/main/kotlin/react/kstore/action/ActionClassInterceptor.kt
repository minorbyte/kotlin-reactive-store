package react.kstore.action

import kotlin.reflect.KClass

class ActionClassInterceptor<INTERCEPTED : Any>(
        private val intercept: Interceptor<INTERCEPTED>,
        val klass: KClass<out INTERCEPTED>
) : Interceptor<INTERCEPTED> {
    override fun invoke(p1: INTERCEPTED): Any = intercept(p1)
}
