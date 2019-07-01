package react.kstore.action

import mu.KotlinLogging
import react.kstore.logDebug
import rx.subjects.PublishSubject
import rx.subjects.Subject
import java.util.concurrent.Executors
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.isSuperclassOf

const val ERROR_MESSAGE = "Error thrown while dispatching action. Dispatcher is in an inconsistent state. No actions will be further " +
        "dispatched"

open class DispatcherImpl : Dispatcher {

    private val executor = Executors.newSingleThreadExecutor()!!
    private val logger = KotlinLogging.logger {}
    private val subject: Subject<Any, Any> = PublishSubject.create<Any>().toSerialized()
    private var interceptors: List<ActionClassInterceptor<*>> = listOf()

    @Volatile
    var consistent = true

    override fun <ACTION : Any> action(action: ACTION) {
        logger.logDebug { "Sync action: $action" }
        emitAction(action)
    }

    override fun <ACTION : Any> asyncAction(action: ACTION) {
        logger.logDebug { "Async action: $action" }
        executor.submit {
            emitAction(action)
        }
    }

    private fun emitAction(action: Any) {
        if (!subject.hasObservers() || !consistent) {
            return
        }

        subject.onNext(interceptors
                .filter { it.klass.isSuperclassOf(action::class) }
                .fold(action, { acc: Any, interceptors: ActionClassInterceptor<*> ->
                    if (acc::class != action::class) {
                        emitAction(acc)
                        return
                    }

                    try {
                        (interceptors as ActionClassInterceptor<Any>)(acc)
                    } catch (e: Exception) {
                        logger.error("Failed action: $acc", e)
                        FailedAction(acc)
                    } catch (e: Throwable) {
                        logger.error(ERROR_MESSAGE)
                        consistent = false
                        throw e
                    }
                }))
    }

    override fun <ACTION : INTERCEPTED, INTERCEPTED : Any> intercept(actionClass: KClass<ACTION>, interceptor: Interceptor<INTERCEPTED>): Subscription {
        logger.logDebug { "Intercept action: ${actionClass.qualifiedName}" }
        val actionClassInterceptor = ActionClassInterceptor(interceptor, actionClass)
        interceptors = interceptors.plus(actionClassInterceptor)

        return InterceptionSubscription({
            interceptors = interceptors.minus(actionClassInterceptor)
        })
    }

    override fun <ACTION : Any> subscribe(actionClass: KClass<ACTION>, subscription: (ACTION) -> Unit): Subscription {
        logger.logDebug { "Subscription to action: ${actionClass.qualifiedName}" }
        return RxSubscription(
                subject
                        .filter { action -> action::class.isSubclassOf(actionClass) }
                        .subscribe { t: Any ->
                            try {
                                subscription(t as ACTION)
                            } catch (e: Exception) {
                                logger.error("Exception in action $t subscription", e)
                                action(FailedAction(t))
                            } catch (e: Throwable) {

                                logger.error(ERROR_MESSAGE)
                                consistent = false
                                throw e
                            }
                        }
        )
    }

    override fun stop() {
        subject.onCompleted()
        interceptors = emptyList()
    }
}
