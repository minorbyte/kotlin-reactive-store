@file:Suppress("UNCHECKED_CAST")

package react.kstore.dependency

import react.kstore.UpdateableStore

open class DependsOn10MergeBuilder<STATE : Any, D1 : Any, D2 : Any, D3 : Any, D4 : Any, D5 : Any, D6 : Any, D7 : Any, D8 : Any, D9 : Any, D10 : Any>(
        store: UpdateableStore<STATE>,
        dependencies: Array<out Any>
) : DependsOnMergeArrayBuilder<STATE>(
        store, dependencies
) {
    infix fun merge(merge: (d1: D1, d2: D2, d3: D3, d4: D4, d5: D5, d6: D6, d7: D7, d8: D8, d9: D9, d10: D10, state: STATE) -> STATE)
            : DependsOn10MergeBuilder<STATE, D1, D2, D3, D4, D5, D6, D7, D8, D9, D10> {
        super.merge { it, state ->
            merge(
                    it[0] as @kotlin.ParameterName(name = "d1") D1,
                    it[1] as @kotlin.ParameterName(name = "d2") D2,
                    it[2] as @kotlin.ParameterName(name = "d3") D3,
                    it[3] as @kotlin.ParameterName(name = "d4") D4,
                    it[4] as @kotlin.ParameterName(name = "d5") D5,
                    it[5] as @kotlin.ParameterName(name = "d6") D6,
                    it[6] as @kotlin.ParameterName(name = "d7") D7,
                    it[7] as @kotlin.ParameterName(name = "d8") D8,
                    it[8] as @kotlin.ParameterName(name = "d9") D9,
                    it[9] as @kotlin.ParameterName(name = "d10") D10,
                    state
            )
        }
        return this
    }

    infix fun rewrite(rewrite: (d1: D1, d2: D2, d3: D3, d4: D4, d5: D5, d6: D6, d7: D7, d8: D8, d9: D9, d10: D10) -> STATE)
            : DependsOn10MergeBuilder<STATE, D1, D2, D3, D4, D5, D6, D7, D8, D9, D10> {
        super.rewrite { it ->
            rewrite(
                    it[0] as @kotlin.ParameterName(name = "d1") D1,
                    it[1] as @kotlin.ParameterName(name = "d2") D2,
                    it[2] as @kotlin.ParameterName(name = "d3") D3,
                    it[3] as @kotlin.ParameterName(name = "d4") D4,
                    it[4] as @kotlin.ParameterName(name = "d5") D5,
                    it[5] as @kotlin.ParameterName(name = "d6") D6,
                    it[6] as @kotlin.ParameterName(name = "d7") D7,
                    it[7] as @kotlin.ParameterName(name = "d8") D8,
                    it[8] as @kotlin.ParameterName(name = "d9") D9,
                    it[9] as @kotlin.ParameterName(name = "d10") D10
            )
        }
        return this
    }

    infix fun react(react: (d1: D1, d2: D2, d3: D3, d4: D4, d5: D5, d6: D6, d7: D7, d8: D8, d9: D9, d10: D10) -> Unit)
            : DependsOn10MergeBuilder<STATE, D1, D2, D3, D4, D5, D6, D7, D8, D9, D10> {
        super.react { it ->
            react(
                    it[0] as @kotlin.ParameterName(name = "d1") D1,
                    it[1] as @kotlin.ParameterName(name = "d2") D2,
                    it[2] as @kotlin.ParameterName(name = "d3") D3,
                    it[3] as @kotlin.ParameterName(name = "d4") D4,
                    it[4] as @kotlin.ParameterName(name = "d5") D5,
                    it[5] as @kotlin.ParameterName(name = "d6") D6,
                    it[6] as @kotlin.ParameterName(name = "d7") D7,
                    it[7] as @kotlin.ParameterName(name = "d8") D8,
                    it[8] as @kotlin.ParameterName(name = "d9") D9,
                    it[9] as @kotlin.ParameterName(name = "d10") D10
            )
        }
        return this
    }

}
