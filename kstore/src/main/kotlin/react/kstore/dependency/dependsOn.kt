package react.kstore.dependency

import react.kstore.UpdateableStore

fun <STATE : Any> UpdateableStore<STATE>.dependsOn(
        function: DependsOnBuilder<STATE>.() -> Unit
) {
    val builder = DependsOnBuilder(this)
    builder.function()
}

