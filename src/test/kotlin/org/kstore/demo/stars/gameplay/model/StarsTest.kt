package org.kstore.demo.stars.gameplay.model

import org.junit.jupiter.api.extension.*
import org.kstore.RandomBeansExtension
import org.kstore.action.CommonDispatcherActionSinkExtension
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS

@Target(CLASS)
@Retention(RUNTIME)
@Extensions(ExtendWith(RandomBeansExtension::class, CommonDispatcherActionSinkExtension::class))
annotation class StarsTest
