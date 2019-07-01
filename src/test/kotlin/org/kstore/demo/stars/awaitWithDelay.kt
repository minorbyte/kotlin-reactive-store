package org.kstore.demo.stars

import org.awaitility.*


fun delayAwaitThat(assertion: ()->Unit) = Awaitility.await().pollDelay(Duration.ONE_HUNDRED_MILLISECONDS).untilAsserted(assertion)

fun awaitThat(assertion: ()->Unit) = Awaitility.await().untilAsserted(assertion)
