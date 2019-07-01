package react.kstore.reaction

open class Validation(
        val success: Boolean,
        val message: ValidationMessage = NoValidationMessage
) {
    init {
        if (!success && message == NoValidationMessage) {
            throw IllegalStateException("If validation fails, then message must be set")
        }
    }

    companion object {
        fun success() = Validation(true)
        fun fails(message: ValidationMessage) = Validation(false, message)
        fun fails(message: String) = Validation(false, ValidationMessage(message))
        fun condition(isValid: Boolean, message: ValidationMessage) = Validation(isValid, if (isValid) NoValidationMessage else message)
        fun condition(isValid: Boolean, message: String) = Validation(isValid, if (isValid) NoValidationMessage else ValidationMessage(message))
    }
}
