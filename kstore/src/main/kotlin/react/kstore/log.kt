package react.kstore

import mu.KLogger

fun KLogger.logInfo(msg: () -> Any?) {
    info(msg)
}

fun KLogger.logDebug(msg: () -> Any?) {
    info(msg)
}
