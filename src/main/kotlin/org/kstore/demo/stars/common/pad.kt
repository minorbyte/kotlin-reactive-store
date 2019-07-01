package org.kstore.demo.stars.common


fun List<String>.pad(amount: Int, sample: String): List<String> {
    val mutableList = this.toMutableList()

    while (mutableList.size < amount) {
        mutableList.add(sample)
    }

    return mutableList.toList()
}

fun List<String>.exact(amount: Int, sample: String): List<String> {
    val mutableList = this.take(amount).toMutableList()

    while (mutableList.size < amount) {
        mutableList.add(sample)
    }

    return mutableList.toList()
}

fun List<String>.add(amount: Int, sample: String): List<String> {
    val mutableList = this.take(amount).toMutableList()

    while (mutableList.size < amount) {
        mutableList.add(sample)
    }

    return mutableList.toList()
}


fun <T> MutableList<T>.append(element: T): MutableList<T> {
    this.add(element)
    return this
}

fun <T> MutableList<T>.append(vararg elements: T): MutableList<T> {
    this.addAll(elements)
    return this
}

fun <T> MutableList<T>.append(elements: List<T>): MutableList<T> {
    this.addAll(elements)
    return this
}
