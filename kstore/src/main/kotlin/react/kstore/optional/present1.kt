package react.kstore.optional

import java.util.*


inline fun <reified R : Any,
        DEPENDENCY1 : Any
        > present(
        default: R,
        dependency1: Optional<DEPENDENCY1>,
        function: (
                DEPENDENCY1
        ) -> R
): R = when {
    !dependency1.isPresent -> default
    else -> function(
            dependency1.get()
    )
}

// TODO: add everywhere
inline fun <reified R : Any,
        DEPENDENCY1 : Any
        > notNull(
        default: R,
        dependency1: DEPENDENCY1?,
        function: (
                DEPENDENCY1
        ) -> R
): R = when (dependency1) {
    null -> default
    else -> function(
            dependency1
    )
}
