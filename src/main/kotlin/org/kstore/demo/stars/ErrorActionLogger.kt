package org.kstore.demo.stars

import mu.KotlinLogging
import react.kstore.*
import react.kstore.action.*
import react.kstore.reaction.FailedActionValidation

class ErrorActionLogger {

    private val logger = KotlinLogging.logger(this::class.qualifiedName!!)

    init {
        CommonDispatcher.subscribe(FailedAction::class) {
            logger.error("Failed action ${it.failed}")
        }
        CommonDispatcher.subscribe(FailedSubscription::class) {
            logger.error("Failed subscription on ${it.state}", it.exception)
        }
        CommonDispatcher.subscribe(FailedMergeUpdate::class) {
            logger.error("Failed state merge on ${it.state}", it.exception)
        }
        CommonDispatcher.subscribe(FailedActionValidation::class) {
            logger.error("Failed action validation on ${it.failed} with message ${it.validationMessage}")
        }
    }
}
