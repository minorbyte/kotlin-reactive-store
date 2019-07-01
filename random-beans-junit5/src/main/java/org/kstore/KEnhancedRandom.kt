package org.kstore

import io.github.benas.randombeans.api.*
import java.util.*
import java.util.stream.Stream
import kotlin.reflect.KClass


open class KEnhancedRandom(
        private val enhancedRandom: EnhancedRandom
) : Random() {

    /**
     * Generate a random string.
     *
     * @return a random string
     * @throws ObjectGenerationException when unable to populate an instance of the given type
    </T> */
    fun nextString(): String {
        return enhancedRandom.nextObject(String::class.java)
    }

    /**
     * Generate a random instance of the given type.
     *
     * @param type           the type for which an instance will be generated
     * @param excludedFields the name of fields to exclude
     * @param <T>            the actual type of the target object
     * @return a random instance of the given type
     * @throws ObjectGenerationException when unable to populate an instance of the given type
    </T> */
    fun <T : Any> nextObject(type: KClass<T>, vararg excludedFields: String): T {
        return enhancedRandom.nextObject(type.java, *excludedFields)
    }

    /**
     * Generate a stream of random instances of the given type.
     *
     * @param type           the type for which instances will be generated
     * @param amount         the number of instances to generate
     * @param excludedFields the name of fields to exclude
     * @param <T>            the actual type of the target objects
     * @return a stream of random instances of the given type
     * @throws ObjectGenerationException when unable to populate an instance of the given type
    </T> */
    fun <T : Any> objects(type: KClass<T>, amount: Int, vararg excludedFields: String): Stream<T> {
        return enhancedRandom.objects(type.java, amount, *excludedFields)
    }

    companion object {
        /**
         * Generate a random instance of the given type.
         *
         * @param type           the target class type
         * @param excludedFields the name of fields to exclude
         * @param <T>            the target type
         * @return a random instance of the given type
        </T> */
        fun <T : Any> random(type: KClass<T>, vararg excludedFields: String): T {
            return EnhancedRandom.random(type.java, *excludedFields)
        }

        /**
         * Generate a stream of random instances of the given type.
         *
         * @param amount         the number of instances to generate
         * @param type           the type for which instances will be generated
         * @param excludedFields the name of fields to exclude
         * @param <T>            the actual type of the target objects
         * @return a stream of random instances of the given type
         * @throws ObjectGenerationException when unable to populate an instance of the given type
        </T> */
        fun <T : Any> randomStreamOf(amount: Int, type: KClass<T>, vararg excludedFields: String): Stream<T> {
            return EnhancedRandom.randomStreamOf(amount, type.java, *excludedFields)
        }

        /**
         * Generate a [List] of random instances of the given type.
         *
         * @param amount         the number of instances to generate
         * @param type           the type for which instances will be generated
         * @param excludedFields the name of fields to exclude
         * @param <T>            the actual type of the target objects
         * @return a list of random instances of the given type
         * @throws ObjectGenerationException when unable to populate an instance of the given type
        </T> */
        fun <T : Any> randomListOf(amount: Int, type: KClass<T>, vararg excludedFields: String): List<T> {
            return EnhancedRandom.randomListOf(amount, type.java, *excludedFields)
        }

        /**
         * Generate a [Set] of random instances of the given type.
         *
         * @param amount         the number of instances to generate
         * @param type           the type for which instances will be generated
         * @param excludedFields the name of fields to exclude
         * @param <T>            the actual type of the target objects
         * @return a set of random instances of the given type
         * @throws ObjectGenerationException when unable to populate an instance of the given type
        </T> */
        fun <T : Any> randomSetOf(amount: Int, type: KClass<T>, vararg excludedFields: String): Set<T> {
            return EnhancedRandom.randomSetOf(amount, type.java, *excludedFields)
        }

        /**
         * Generate a [Collection] of random instances of the given type.
         *
         * @param amount         the number of instances to generate
         * @param type           the type for which instances will be generated
         * @param excludedFields the name of fields to exclude
         * @param <T>            the actual type of the target objects
         * @return a collection of random instances of the given type
         * @throws ObjectGenerationException when unable to populate an instance of the given type
        </T> */
        fun <T : Any> randomCollectionOf(amount: Int, type: KClass<T>, vararg excludedFields: String): Collection<T> {
            return EnhancedRandom.randomCollectionOf(amount, type.java, *excludedFields)
        }
    }
}
