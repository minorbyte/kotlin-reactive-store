package org.kstore.demo.stars

import io.github.benas.randombeans.EnhancedRandomBuilder
import org.kstore.KEnhancedRandom
import kotlin.reflect.KClass

private const val MIN_COLLECTION_SIZE = 0
private const val MAX_COLLECTION_SIZE = 10

val RANDOM = ExceptRandom

object ExceptRandom : KEnhancedRandom(
        EnhancedRandomBuilder
                .aNewEnhancedRandomBuilder()
                .collectionSizeRange(MIN_COLLECTION_SIZE, MAX_COLLECTION_SIZE)
                .build()
) {

    fun nextInt(from: Int, to: Int) = from + nextInt(to - from)

    fun <T> except(any: T, generate: KEnhancedRandom.() -> T): T {
        var result = generate()
        while (result == any) {
            result = generate()
        }
        return result
    }

    fun <T> except(condition: (T) -> Boolean, generate: KEnhancedRandom.() -> T): T {
        var result = generate()
        while (condition(result)) {
            result = generate()
        }
        return result
    }

    fun <T : Any> except(klass: KClass<T>, condition: (T) -> Boolean): T {
        var result = RANDOM.nextObject<T>(klass)
        while (condition(result)) {
            result = RANDOM.nextObject<T>(klass)
        }
        return result
    }

    inline fun <reified T : Any> except(condition: (T) -> Boolean): T {
        var result = RANDOM.nextObject<T>()
        while (condition(result)) {
            result = RANDOM.nextObject()
        }
        return result
    }

    fun <T : Any> nextList(kclass: KClass<T>): List<T> {
        val max = MIN_COLLECTION_SIZE + RANDOM.nextInt(MAX_COLLECTION_SIZE)
        return (MIN_COLLECTION_SIZE..max).map {
            nextObject(kclass)
        }
    }

    fun <K : Any, V : Any> nextMap(keyClass: KClass<K>, valueClass: KClass<V>): Map<K, V> {
        val max = MIN_COLLECTION_SIZE + RANDOM.nextInt(MAX_COLLECTION_SIZE)
        return (MIN_COLLECTION_SIZE..max).associateBy({
            nextObject(keyClass)
        }, {
            nextObject(valueClass)
        })
    }

    fun <T : Any> next(vararg values: T): T {
        return values[RANDOM.nextInt(values.size)]
    }

    fun <T : Any> next(values: List<T>): T {
        return values[RANDOM.nextInt(values.size)]
    }

    inline fun <reified T : Any> nextObject(): T = nextObject(T::class)
    inline fun <reified T : Any> nextList(): List<T> = nextList(T::class)
    inline fun <reified K : Any, reified V : Any> nextMap(): Map<K, V> = nextMap(K::class, V::class)
}
