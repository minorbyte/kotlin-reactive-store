@file:Suppress("UNCHECKED_CAST")

package react.kstore.dependency

import react.kstore.UpdateableStore

open class DependsOn1MergeBuilder<STATE : Any, D1 : Any>(
        store: UpdateableStore<STATE>,
        dependencies: Array<out Any>
) : DependsOnMergeArrayBuilder<STATE>(
        store, dependencies
) {
    infix fun merge(merge: (d1: D1, state: STATE) -> STATE)
            : DependsOn1MergeBuilder<STATE, D1> {
        super.merge { it, state ->
            merge(
                    it[0] as @kotlin.ParameterName(name = "d1") D1,
                    state
            )
        }
        return this
    }

    infix fun rewrite(rewrite: (d1: D1) -> STATE)
            : DependsOn1MergeBuilder<STATE, D1> {
        super.rewrite { it ->
            rewrite(
                    it[0] as @kotlin.ParameterName(name = "d1") D1
            )
        }
        return this
    }

    infix fun react(react: (d1: D1) -> Unit)
            : DependsOn1MergeBuilder<STATE, D1> {
        super.react { it ->
            react(
                    it[0] as @kotlin.ParameterName(name = "d1") D1
            )
        }
        return this
    }

}
