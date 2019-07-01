package react.kstore.optional

import java.util.*


inline fun <reified R : Any,
        DEPENDENCY1 : Any,
        DEPENDENCY2 : Any,
        DEPENDENCY3 : Any,
        DEPENDENCY4 : Any,
        DEPENDENCY5 : Any,
        DEPENDENCY6 : Any
        > present(
        default: R,
        dependency1: Optional<DEPENDENCY1>,
        dependency2: Optional<DEPENDENCY2>,
        dependency3: Optional<DEPENDENCY3>,
        dependency4: Optional<DEPENDENCY4>,
        dependency5: Optional<DEPENDENCY5>,
        dependency6: Optional<DEPENDENCY6>,
        function: (
                DEPENDENCY1,
                DEPENDENCY2,
                DEPENDENCY3,
                DEPENDENCY4,
                DEPENDENCY5,
                DEPENDENCY6
        ) -> R
): R = when {
    !dependency1.isPresent -> default
    !dependency2.isPresent -> default
    !dependency3.isPresent -> default
    !dependency4.isPresent -> default
    !dependency5.isPresent -> default
    !dependency6.isPresent -> default
    else -> function(
            dependency1.get(),
            dependency2.get(),
            dependency3.get(),
            dependency4.get(),
            dependency5.get(),
            dependency6.get()
    )
}
