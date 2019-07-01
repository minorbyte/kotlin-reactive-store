package org.kstore

fun <T> List<T>.randomItem() = this[Random.nextInt(this.size)]

fun <T> List<T>.randomItem(except: (T) -> Boolean): T {
    val variants = this.filterNot(except)
    if (variants.isEmpty()) {
        throw IllegalStateException("No items satisfying the condition")
    }

    return variants[Random.nextInt(variants.size)]
}

fun <T> List<T>.randomItemButNot(item: T): T {
    var result = this[Random.nextInt(this.size)]
    while (result == item) {
        result = this[Random.nextInt(this.size)]
    }
    return result
}


fun <T> List<T>.randomItem(random: java.util.Random) = this[random.nextInt(this.size)]
