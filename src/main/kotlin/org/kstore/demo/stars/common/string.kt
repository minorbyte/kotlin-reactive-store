package org.kstore.demo.stars.common

fun String.leftField(maxChars: Int) = this.capitalize().take(maxChars).padEnd(maxChars, ' ')

fun String.rightField(maxChars: Int) = this.capitalize().take(maxChars).padStart(maxChars, ' ')
