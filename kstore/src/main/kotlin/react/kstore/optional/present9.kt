package react.kstore.optional

import java.util.*


inline fun <reified R : Any,
        DEPENDENCY1 : Any,
        DEPENDENCY2 : Any,
        DEPENDENCY3 : Any,
        DEPENDENCY4 : Any,
        DEPENDENCY5 : Any,
        DEPENDENCY6 : Any,
        DEPENDENCY7 : Any,
        DEPENDENCY8 : Any,
        DEPENDENCY9 : Any
        > present(
        default: R,
        dependency1: Optional<DEPENDENCY1>,
        dependency2: Optional<DEPENDENCY2>,
        dependency3: Optional<DEPENDENCY3>,
        dependency4: Optional<DEPENDENCY4>,
        dependency5: Optional<DEPENDENCY5>,
        dependency6: Optional<DEPENDENCY6>,
        dependency7: Optional<DEPENDENCY7>,
        dependency8: Optional<DEPENDENCY8>,
        dependency9: Optional<DEPENDENCY9>,
        function: (
                DEPENDENCY1,
                DEPENDENCY2,
                DEPENDENCY3,
                DEPENDENCY4,
                DEPENDENCY5,
                DEPENDENCY6,
                DEPENDENCY7,
                DEPENDENCY8,
                DEPENDENCY9
        ) -> R
): R = when {
    !dependency1.isPresent -> default
    !dependency2.isPresent -> default
    !dependency3.isPresent -> default
    !dependency4.isPresent -> default
    !dependency5.isPresent -> default
    !dependency6.isPresent -> default
    !dependency7.isPresent -> default
    !dependency8.isPresent -> default
    !dependency9.isPresent -> default
    else -> function(
            dependency1.get(),
            dependency2.get(),
            dependency3.get(),
            dependency4.get(),
            dependency5.get(),
            dependency6.get(),
            dependency7.get(),
            dependency8.get(),
            dependency9.get()
    )
}
