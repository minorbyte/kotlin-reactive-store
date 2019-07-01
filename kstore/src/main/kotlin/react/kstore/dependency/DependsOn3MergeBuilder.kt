@file:Suppress("UNCHECKED_CAST")

package react.kstore.dependency

import react.kstore.UpdateableStore

open class DependsOn3MergeBuilder<STATE : Any, D1 : Any, D2 : Any, D3 : Any>(
        store: UpdateableStore<STATE>,
        dependencies: Array<out Any>
) : DependsOnMergeArrayBuilder<STATE>(
        store, dependencies
) {
    infix fun merge(merge: (d1: D1, d2: D2, d3: D3, state: STATE) -> STATE)
            : DependsOn3MergeBuilder<STATE, D1, D2, D3> {
        super.merge { it, state ->
            merge(
                    it[0] as @kotlin.ParameterName(name = "d1") D1,
                    it[1] as @kotlin.ParameterName(name = "d2") D2,
                    it[2] as @kotlin.ParameterName(name = "d3") D3,
                    state
            )
        }
        return this
    }

    //TODO: rename to assign or replace
    infix fun rewrite(rewrite: (d1: D1, d2: D2, d3: D3) -> STATE)
            : DependsOn3MergeBuilder<STATE, D1, D2, D3> {
        super.rewrite { it ->
            rewrite(
                    it[0] as @kotlin.ParameterName(name = "d1") D1,
                    it[1] as @kotlin.ParameterName(name = "d2") D2,
                    it[2] as @kotlin.ParameterName(name = "d3") D3
            )
        }
        return this
    }

    infix fun react(react: (d1: D1, d2: D2, d3: D3) -> Unit)
            : DependsOn3MergeBuilder<STATE, D1, D2, D3> {
        super.react { it ->
            react(
                    it[0] as @kotlin.ParameterName(name = "d1") D1,
                    it[1] as @kotlin.ParameterName(name = "d2") D2,
                    it[2] as @kotlin.ParameterName(name = "d3") D3
            )
        }
        return this
    }

}
