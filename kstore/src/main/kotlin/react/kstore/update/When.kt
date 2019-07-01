package react.kstore.update

import react.kstore.*


fun <STATE : Any> UpdateableStore<STATE>.whenState(condition: (oldState: STATE, newState: STATE) -> Boolean, update: (STATE) -> Unit) {
    this.registerListener(StateConditionListener(condition, update))
}
