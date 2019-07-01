package org.kstore.demo.stars.common

import java.util.*

fun <T> Optional<T>.orNull(): T? {
    return if (this.isPresent) this.get() else null
}

val <T> Optional<T>.empty: Boolean
    get() = !this.isPresent

fun <T : F, F> Optional<T>.asType(): Optional<F> {
    return this as Optional<F>
}

inline fun <T> firstOf(vararg optional: Optional<out T>): Optional<out T> {
    return optional.first { it.isPresent }
}

inline fun <T, R> Optional<T>.ifPresentOrNull(action: (T) -> R): R? = if (this.isPresent) action.invoke(this.get()) else null
