package react.kstore.reaction

import react.kstore.action.FailedAction

open class FailedActionValidation(
        failed: Any,
        val validationMessage: ValidationMessage
) : FailedAction(failed)
