package react.kstore

import kotlin.reflect.KClass


inline fun <T : Any, R : Any> Array<out T>.mapAs(): List<R> = map { it as R }

inline fun <T, R : Any> Iterable<T>.mapAs(kclass: KClass<R>): List<R> {
    return this.map { it as R }
}
