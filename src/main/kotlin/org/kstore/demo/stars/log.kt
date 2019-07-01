package org.kstore.demo.stars

import mu.KLogger

fun KLogger.log(msg: () -> Any?) {
    info(msg)
}
