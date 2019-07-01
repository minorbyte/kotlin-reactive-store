package react.kstore

import react.kstore.action.Dispatcher

abstract class UpdateableStore<STATE : Any>(
        dispatcher: Dispatcher
) : Store<STATE>(dispatcher) {

    private var listeners: List<StateConditionListener<STATE>> = listOf()

    internal fun registerListener(listener: StateConditionListener<STATE>) {
        listeners = listeners.plus(listener)
    }

    internal fun update(state: STATE) {
        val oldState = this.state
        if (oldState != state) {
            updateState(state)
            listeners.forEach { if (it.condition(oldState, state)) it.action(state) }
        }
    }

    protected abstract fun updateState(state: STATE)
}

