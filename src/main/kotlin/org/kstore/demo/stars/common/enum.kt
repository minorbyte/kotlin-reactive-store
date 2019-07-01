package org.kstore.demo.stars.common

fun <T : Enum<*>> T.isIn(vararg values: T): Boolean {
    return values.any { it == this }
}
