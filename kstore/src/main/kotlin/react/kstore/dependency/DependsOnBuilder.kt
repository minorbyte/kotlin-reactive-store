package react.kstore.dependency

import react.kstore.*

class DependsOnBuilder<STATE : Any>(
        val store: UpdateableStore<STATE>
) {

    infix fun stores(stores: Array<out Any>): DependsOnMergeArrayBuilder<STATE> {
        return DependsOnMergeArrayBuilder(store, stores)
    }

    fun stores(
            stores: Array<out Any>,
            function: DependsOnMergeArrayBuilder<STATE>.() -> Unit
    ) {
        DependsOnMergeArrayBuilder(store, stores).function()
    }

    fun <DEPENDENCY : Any> stores(
            stores: List<DEPENDENCY>,
            function: DependsOnMergeArrayBuilder<STATE>.() -> Unit
    ) {
        DependsOnMergeArrayBuilder(store, (stores as List<Any>).toTypedArray()).function()
    }

    fun <D1 : Any, D2 : Any, D3 : Any, D4 : Any, D5 : Any, D6 : Any, D7 : Any, D8 : Any, D9 : Any, D10 : Any>
            stores(
            store1: Subscribable<D1>,
            store2: Subscribable<D2>,
            store3: Subscribable<D3>,
            store4: Subscribable<D4>,
            store5: Subscribable<D5>,
            store6: Subscribable<D6>,
            store7: Subscribable<D7>,
            store8: Subscribable<D8>,
            store9: Subscribable<D9>,
            store10: Subscribable<D10>
    ): DependsOn10MergeBuilder<STATE, D1, D2, D3, D4, D5, D6, D7, D8, D9, D10> {
        return DependsOn10MergeBuilder(store, arrayOf(store1, store2, store3, store4, store5, store6, store7, store8, store9, store10))
    }

    fun <D1 : Any, D2 : Any, D3 : Any, D4 : Any, D5 : Any, D6 : Any, D7 : Any, D8 : Any, D9 : Any, D10 : Any>
            stores(
            store1: Subscribable<D1>,
            store2: Subscribable<D2>,
            store3: Subscribable<D3>,
            store4: Subscribable<D4>,
            store5: Subscribable<D5>,
            store6: Subscribable<D6>,
            store7: Subscribable<D7>,
            store8: Subscribable<D8>,
            store9: Subscribable<D9>,
            store10: Subscribable<D10>,
            function: DependsOn10MergeBuilder<STATE, D1, D2, D3, D4, D5, D6, D7, D8, D9, D10>.() -> Unit
    ) {
        DependsOn10MergeBuilder<STATE, D1, D2, D3, D4, D5, D6, D7, D8, D9, D10>(
                store, arrayOf(store1, store2, store3, store4, store5, store6, store7, store8, store9, store10)
        ).function()
    }

    fun <D1 : Any, D2 : Any, D3 : Any, D4 : Any, D5 : Any, D6 : Any, D7 : Any, D8 : Any, D9 : Any>
            stores(
            store1: Subscribable<D1>,
            store2: Subscribable<D2>,
            store3: Subscribable<D3>,
            store4: Subscribable<D4>,
            store5: Subscribable<D5>,
            store6: Subscribable<D6>,
            store7: Subscribable<D7>,
            store8: Subscribable<D8>,
            store9: Subscribable<D9>
            ): DependsOn9MergeBuilder<STATE, D1, D2, D3, D4, D5, D6, D7, D8, D9> {
        return DependsOn9MergeBuilder(store, arrayOf(store1, store2, store3, store4, store5, store6, store7, store8, store9))
    }

    fun <D1 : Any, D2 : Any, D3 : Any, D4 : Any, D5 : Any, D6 : Any, D7 : Any, D8 : Any, D9 : Any>
            stores(
            store1: Subscribable<D1>,
            store2: Subscribable<D2>,
            store3: Subscribable<D3>,
            store4: Subscribable<D4>,
            store5: Subscribable<D5>,
            store6: Subscribable<D6>,
            store7: Subscribable<D7>,
            store8: Subscribable<D8>,
            store9: Subscribable<D9>,
            function: DependsOn9MergeBuilder<STATE, D1, D2, D3, D4, D5, D6, D7, D8, D9>.() -> Unit
    ) {
        DependsOn9MergeBuilder<STATE, D1, D2, D3, D4, D5, D6, D7, D8, D9>(
                store, arrayOf(store1, store2, store3, store4, store5, store6, store7, store8, store9)
        ).function()
    }

    fun <D1 : Any, D2 : Any, D3 : Any, D4 : Any, D5 : Any, D6 : Any, D7 : Any, D8 : Any>
            stores(
            store1: Subscribable<D1>,
            store2: Subscribable<D2>,
            store3: Subscribable<D3>,
            store4: Subscribable<D4>,
            store5: Subscribable<D5>,
            store6: Subscribable<D6>,
            store7: Subscribable<D7>,
            store8: Subscribable<D8>
    ): DependsOn8MergeBuilder<STATE, D1, D2, D3, D4, D5, D6, D7, D8> {
        return DependsOn8MergeBuilder(store, arrayOf(store1, store2, store3, store4, store5, store6, store7, store8))
    }

    fun <D1 : Any, D2 : Any, D3 : Any, D4 : Any, D5 : Any, D6 : Any, D7 : Any, D8 : Any>
            stores(
            store1: Subscribable<D1>,
            store2: Subscribable<D2>,
            store3: Subscribable<D3>,
            store4: Subscribable<D4>,
            store5: Subscribable<D5>,
            store6: Subscribable<D6>,
            store7: Subscribable<D7>,
            store8: Subscribable<D8>,
            function: DependsOn8MergeBuilder<STATE, D1, D2, D3, D4, D5, D6, D7, D8>.() -> Unit
    ) {
        DependsOn8MergeBuilder<STATE, D1, D2, D3, D4, D5, D6, D7, D8>(
                store, arrayOf(store1, store2, store3, store4, store5, store6, store7, store8)
        ).function()
    }

    fun <D1 : Any, D2 : Any, D3 : Any, D4 : Any, D5 : Any, D6 : Any, D7 : Any>
            stores(
            store1: Subscribable<D1>,
            store2: Subscribable<D2>,
            store3: Subscribable<D3>,
            store4: Subscribable<D4>,
            store5: Subscribable<D5>,
            store6: Subscribable<D6>,
            store7: Subscribable<D7>
    ): DependsOn7MergeBuilder<STATE, D1, D2, D3, D4, D5, D6, D7> {
        return DependsOn7MergeBuilder(store, arrayOf(store1, store2, store3, store4, store5, store6, store7))
    }

    fun <D1 : Any, D2 : Any, D3 : Any, D4 : Any, D5 : Any, D6 : Any, D7 : Any>
            stores(
            store1: Subscribable<D1>,
            store2: Subscribable<D2>,
            store3: Subscribable<D3>,
            store4: Subscribable<D4>,
            store5: Subscribable<D5>,
            store6: Subscribable<D6>,
            store7: Subscribable<D7>,
            function: DependsOn7MergeBuilder<STATE, D1, D2, D3, D4, D5, D6, D7>.() -> Unit
    ) {
        DependsOn7MergeBuilder<STATE, D1, D2, D3, D4, D5, D6, D7>(
                store, arrayOf(store1, store2, store3, store4, store5, store6, store7)
        ).function()
    }

    fun <D1 : Any, D2 : Any, D3 : Any, D4 : Any, D5 : Any, D6 : Any>
            stores(
            store1: Subscribable<D1>,
            store2: Subscribable<D2>,
            store3: Subscribable<D3>,
            store4: Subscribable<D4>,
            store5: Subscribable<D5>,
            store6: Subscribable<D6>
    ): DependsOn6MergeBuilder<STATE, D1, D2, D3, D4, D5, D6> {
        return DependsOn6MergeBuilder(store, arrayOf(store1, store2, store3, store4, store5, store6))
    }

    fun <D1 : Any, D2 : Any, D3 : Any, D4 : Any, D5 : Any, D6 : Any>
            stores(
            store1: Subscribable<D1>,
            store2: Subscribable<D2>,
            store3: Subscribable<D3>,
            store4: Subscribable<D4>,
            store5: Subscribable<D5>,
            store6: Subscribable<D6>,
            function: DependsOn6MergeBuilder<STATE, D1, D2, D3, D4, D5, D6>.() -> Unit
    ) {
        DependsOn6MergeBuilder<STATE, D1, D2, D3, D4, D5, D6>(
                store, arrayOf(store1, store2, store3, store4, store5, store6)
        ).function()
    }

    fun <D1 : Any, D2 : Any, D3 : Any, D4 : Any, D5 : Any>
            stores(
            store1: Subscribable<D1>,
            store2: Subscribable<D2>,
            store3: Subscribable<D3>,
            store4: Subscribable<D4>,
            store5: Subscribable<D5>
    ): DependsOn5MergeBuilder<STATE, D1, D2, D3, D4, D5> {
        return DependsOn5MergeBuilder(store, arrayOf(store1, store2, store3, store4, store5))
    }

    fun <D1 : Any, D2 : Any, D3 : Any, D4 : Any, D5 : Any>
            stores(
            store1: Subscribable<D1>,
            store2: Subscribable<D2>,
            store3: Subscribable<D3>,
            store4: Subscribable<D4>,
            store5: Subscribable<D5>,
            function: DependsOn5MergeBuilder<STATE, D1, D2, D3, D4, D5>.() -> Unit
    ) {
        DependsOn5MergeBuilder<STATE, D1, D2, D3, D4, D5>(
                store, arrayOf(store1, store2, store3, store4, store5)
        ).function()
    }

    fun <D1 : Any, D2 : Any, D3 : Any, D4 : Any>
            stores(
            store1: Subscribable<D1>,
            store2: Subscribable<D2>,
            store3: Subscribable<D3>,
            store4: Subscribable<D4>
    ): DependsOn4MergeBuilder<STATE, D1, D2, D3, D4> {
        return DependsOn4MergeBuilder(store, arrayOf(store1, store2, store3, store4))
    }

    fun <D1 : Any, D2 : Any, D3 : Any, D4 : Any>
            stores(
            store1: Subscribable<D1>,
            store2: Subscribable<D2>,
            store3: Subscribable<D3>,
            store4: Subscribable<D4>,
            function: DependsOn4MergeBuilder<STATE, D1, D2, D3, D4>.() -> Unit
    ) {
        DependsOn4MergeBuilder<STATE, D1, D2, D3, D4>(
                store, arrayOf(store1, store2, store3, store4)
        ).function()
    }

    fun <D1 : Any, D2 : Any, D3 : Any>
            stores(
            store1: Subscribable<D1>,
            store2: Subscribable<D2>,
            store3: Subscribable<D3>
    ): DependsOn3MergeBuilder<STATE, D1, D2, D3> {
        return DependsOn3MergeBuilder(store, arrayOf(store1, store2, store3))
    }

    fun <D1 : Any, D2 : Any, D3 : Any>
            stores(
            store1: Subscribable<D1>,
            store2: Subscribable<D2>,
            store3: Subscribable<D3>,
            function: DependsOn3MergeBuilder<STATE, D1, D2, D3>.() -> Unit
    ) {
        DependsOn3MergeBuilder<STATE, D1, D2, D3>(
                store, arrayOf(store1, store2, store3)
        ).function()
    }

    fun <D1 : Any, D2 : Any>
            stores(
            store1: Subscribable<D1>,
            store2: Subscribable<D2>
    ): DependsOn2MergeBuilder<STATE, D1, D2> {
        return DependsOn2MergeBuilder(store, arrayOf(store1, store2))
    }

    fun <D1 : Any, D2 : Any>
            stores(
            store1: Subscribable<D1>,
            store2: Subscribable<D2>,
            function: DependsOn2MergeBuilder<STATE, D1, D2>.() -> Unit
    ) {
        DependsOn2MergeBuilder<STATE, D1, D2>(
                store, arrayOf(store1, store2)
        ).function()
    }

    fun <D1 : Any>
            stores(
            store1: Subscribable<D1>
    ): DependsOn1MergeBuilder<STATE, D1> {
        return DependsOn1MergeBuilder(store, arrayOf(store1))
    }

    fun <D1 : Any>
            stores(
            store1: Subscribable<D1>,
            function: DependsOn1MergeBuilder<STATE, D1>.() -> Unit
    ) {
        DependsOn1MergeBuilder<STATE, D1>(
                store, arrayOf(store1)
        ).function()
    }
}
