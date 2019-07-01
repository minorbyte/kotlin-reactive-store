@file:Suppress("UNCHECKED_CAST")

package react.kstore.dependency

import react.kstore.UpdateableStore

open class DependsOn2MergeBuilder<STATE : Any, D1 : Any, D2 : Any>(
        store: UpdateableStore<STATE>,
        dependencies: Array<out Any>
) : DependsOnMergeArrayBuilder<STATE>(
        store, dependencies
) {
    infix fun merge(merge: (d1: D1, d2: D2, state: STATE) -> STATE)
            : DependsOn2MergeBuilder<STATE, D1, D2> {
        super.merge { it, state ->
            merge(
                    it[0] as @kotlin.ParameterName(name = "d1") D1,
                    it[1] as @kotlin.ParameterName(name = "d2") D2,
                    state
            )
        }
        return this
    }

    infix fun rewrite(rewrite: (d1: D1, d2: D2) -> STATE)
            : DependsOn2MergeBuilder<STATE, D1, D2> {
        super.rewrite { it ->
            rewrite(
                    it[0] as @kotlin.ParameterName(name = "d1") D1,
                    it[1] as @kotlin.ParameterName(name = "d2") D2
            )
        }
        return this
    }

    infix fun react(react: (d1: D1, d2: D2) -> Unit)
            : DependsOn2MergeBuilder<STATE, D1, D2> {
        super.react { it ->
            react(
                    it[0] as @kotlin.ParameterName(name = "d1") D1,
                    it[1] as @kotlin.ParameterName(name = "d2") D2
            )
        }
        return this
    }

}
