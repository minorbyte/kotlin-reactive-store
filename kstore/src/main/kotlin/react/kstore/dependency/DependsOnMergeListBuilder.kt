package react.kstore.dependency

import react.kstore.*

internal open class DependsOnMergeListBuilder<STATE : Any, DEPENDENCY : Any>(
        store: UpdateableStore<STATE>,
        dependencies: Array<out Any>
) : DependsOnMergeArrayBuilder<STATE>(
        store, dependencies
) {
    infix fun merge(merge: (dependencies: List<DEPENDENCY>, state: STATE) -> STATE): DependsOnMergeListBuilder<STATE, DEPENDENCY> {
        super.merge { it, state -> merge(it.mapAs(), state) }
        return this
    }

    infix fun rewrite(rewrite: (dependencies: List<DEPENDENCY>) -> STATE): DependsOnMergeListBuilder<STATE, DEPENDENCY> {
        super.rewrite { rewrite(it.mapAs()) }
        return this
    }

    infix fun react(reaction: (dependencies: List<DEPENDENCY>) -> Unit): DependsOnMergeListBuilder<STATE, DEPENDENCY> {
        super.react { reaction(it.mapAs()) }
        return this
    }
}
