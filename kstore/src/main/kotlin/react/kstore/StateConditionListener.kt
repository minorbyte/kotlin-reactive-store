package react.kstore

internal class StateConditionListener<in STATE>(
        val condition: (STATE, STATE) -> Boolean,
        val action: (STATE) -> Unit
)
