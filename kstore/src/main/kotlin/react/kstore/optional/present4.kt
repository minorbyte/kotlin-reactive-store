package react.kstore.optional

import java.util.*


inline fun <reified R : Any,
        DEPENDENCY1 : Any,
        DEPENDENCY2 : Any,
        DEPENDENCY3 : Any,
        DEPENDENCY4 : Any
        > present(
        default: R,
        dependency1: Optional<DEPENDENCY1>,
        dependency2: Optional<DEPENDENCY2>,
        dependency3: Optional<DEPENDENCY3>,
        dependency4: Optional<DEPENDENCY4>,
        function: (
                DEPENDENCY1,
                DEPENDENCY2,
                DEPENDENCY3,
                DEPENDENCY4
        ) -> R
): R = when {
    !dependency1.isPresent -> default
    !dependency2.isPresent -> default
    !dependency3.isPresent -> default
    !dependency4.isPresent -> default
    else -> function(
            dependency1.get(),
            dependency2.get(),
            dependency3.get(),
            dependency4.get()
    )
}
