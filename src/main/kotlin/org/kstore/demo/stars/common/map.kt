package org.kstore.demo.stars.common

inline fun <K, V> Map<out K, V>.mapOne(key: K, defaultValue: V, transform: (V) -> V): Map<K, V> {
    return this.plus(Pair(key, transform(this.getOrDefault(key, defaultValue))))
}

inline fun <K, V> MutableMap<K, V>.putOrReplace(key: K, defaultValue: V, transform: (V) -> V): Map<K, V> {
    this[key] = transform(this.getOrDefault(key, defaultValue))
    return this
}

inline fun <K, V> MutableMap<K, V>.putOrReplace(key: K, transform: (V) -> V): Map<K, V> {
    this[key] = transform(this[key]!!)
    return this
}

inline fun <K, V> MutableMap<K, V>.append(action: MutableMap<K, V>.() -> Unit): MutableMap<K, V> {
    this.action()
    return this
}

