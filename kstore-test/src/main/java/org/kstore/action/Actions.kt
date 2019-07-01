package org.kstore.action

import kotlin.reflect.KClass

annotation class SubscribeTo(
        val kclass: KClass<*> = Any::class
)
