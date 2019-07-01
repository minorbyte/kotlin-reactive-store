package react.kstore.optional

import java.util.*


inline fun <reified R : Any,
        DEPENDENCY1 : Any,
        DEPENDENCY2 : Any
        > present(
        default: R,
        dependency1: Optional<DEPENDENCY1>,
        dependency2: Optional<DEPENDENCY2>,
        function: (
                DEPENDENCY1,
                DEPENDENCY2
        ) -> R
): R = when {
    !dependency1.isPresent -> default
    !dependency2.isPresent -> default
    else -> function(
            dependency1.get(),
            dependency2.get()
    )
}
