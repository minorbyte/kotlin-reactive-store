@file:Suppress("UNCHECKED_CAST")

package react.kstore.dependency

import react.kstore.UpdateableStore

open class DependsOn7MergeBuilder<STATE : Any, D1 : Any, D2 : Any, D3 : Any, D4 : Any, D5 : Any, D6 : Any, D7 : Any>(
        store: UpdateableStore<STATE>,
        dependencies: Array<out Any>
) : DependsOnMergeArrayBuilder<STATE>(
        store, dependencies
) {
    infix fun merge(merge: (d1: D1, d2: D2, d3: D3, d4: D4, d5: D5, d6: D6, d7: D7, state: STATE) -> STATE)
            : DependsOn7MergeBuilder<STATE, D1, D2, D3, D4, D5, D6, D7> {
        super.merge { it, state ->
            merge(
                    it[0] as @kotlin.ParameterName(name = "d1") D1,
                    it[1] as @kotlin.ParameterName(name = "d2") D2,
                    it[2] as @kotlin.ParameterName(name = "d3") D3,
                    it[3] as @kotlin.ParameterName(name = "d4") D4,
                    it[4] as @kotlin.ParameterName(name = "d5") D5,
                    it[5] as @kotlin.ParameterName(name = "d6") D6,
                    it[6] as @kotlin.ParameterName(name = "d7") D7,
                    state
            )
        }
        return this
    }

    infix fun rewrite(rewrite: (d1: D1, d2: D2, d3: D3, d4: D4, d5: D5, d6: D6, d7: D7) -> STATE)
            : DependsOn7MergeBuilder<STATE, D1, D2, D3, D4, D5, D6, D7> {
        super.rewrite { it ->
            rewrite(
                    it[0] as @kotlin.ParameterName(name = "d1") D1,
                    it[1] as @kotlin.ParameterName(name = "d2") D2,
                    it[2] as @kotlin.ParameterName(name = "d3") D3,
                    it[3] as @kotlin.ParameterName(name = "d4") D4,
                    it[4] as @kotlin.ParameterName(name = "d5") D5,
                    it[5] as @kotlin.ParameterName(name = "d6") D6,
                    it[6] as @kotlin.ParameterName(name = "d7") D7
            )
        }
        return this
    }

    infix fun react(react: (d1: D1, d2: D2, d3: D3, d4: D4, d5: D5, d6: D6, d7: D7) -> Unit)
            : DependsOn7MergeBuilder<STATE, D1, D2, D3, D4, D5, D6, D7> {
        super.react { it ->
            react(
                    it[0] as @kotlin.ParameterName(name = "d1") D1,
                    it[1] as @kotlin.ParameterName(name = "d2") D2,
                    it[2] as @kotlin.ParameterName(name = "d3") D3,
                    it[3] as @kotlin.ParameterName(name = "d4") D4,
                    it[4] as @kotlin.ParameterName(name = "d5") D5,
                    it[5] as @kotlin.ParameterName(name = "d6") D6,
                    it[6] as @kotlin.ParameterName(name = "d7") D7
            )
        }
        return this
    }

}
