package react.kstore.dependency

import mu.KotlinLogging
import react.kstore.*
import rx.Observable

open class DependsOnMergeArrayBuilder<STATE : Any>(
        val store: UpdateableStore<STATE>,
        val dependencies: Array<out Any>
) {

    val logger = KotlinLogging.logger(this::class.qualifiedName!!)
    private val observables: List<Observable<*>> = dependencies.map {
        when (it) {
            is Observable<*> -> it
            is Subscribable<*> -> it.observable
            else -> {
                throw IllegalArgumentException("${it::class.simpleName} is not a observable")
            }
        }
    }

    fun merge(merge: (dependencies: Array<out Any>, state: STATE) -> STATE): DependsOnMergeArrayBuilder<STATE> {
        logger.logInfo { dependencyPrinter(store, "merges state with dependencies", dependencies) }

        doReaction({ args: Array<out Any> -> merge(args, store.state) }, { merged ->
            store.update(merged)
        }, "Error on store state update by merged dependencies")

        return this
    }

    fun rewrite(rewrite: (dependencies: Array<out Any>) -> STATE): DependsOnMergeArrayBuilder<STATE> {
        logger.logInfo { dependencyPrinter(store, "takes state from dependencies", dependencies) }

        doReaction({ args: Array<out Any> -> rewrite(args) }, { merged ->
            store.update(merged)
        }, "Error on store state update by merged dependencies")

        return this
    }

    fun react(reaction: (dependencies: Array<out Any>) -> Unit): DependsOnMergeArrayBuilder<STATE> {
        logger.logInfo { dependencyPrinter(store, "takes state from dependencies", dependencies) }

        doReaction({ it }, {
            reaction(it)
        }, "Error on store dependency update reaction")
        return this
    }

    private fun <T : Any, F : Any> doReaction(
            combine: (Array<out Any>) -> T,
            subscribe: (T) -> F,
            errorMessage: String
    ): DependsOnMergeArrayBuilder<STATE> {
        logger.logInfo { dependencyPrinter(store, "takes state from dependencies", dependencies) }

        Observable.combineLatest(observables) { combine(it) }.subscribe({ args ->
            try {
                subscribe(args)
            } catch (e: Exception) {
                logger.error(errorMessage, e)
            }
        }, {
            logger.error(it) { "Error on reacting at dependencies" }
        })

        return this
    }

}
