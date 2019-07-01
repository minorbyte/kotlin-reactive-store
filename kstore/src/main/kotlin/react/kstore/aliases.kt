package react.kstore

import react.kstore.reaction.Validation


typealias StateInterceptor<INTERCEPTED, STATE> = (state: STATE, action: INTERCEPTED) -> INTERCEPTED
typealias ActionValidator<INTERCEPTED, STATE> = (state: STATE, action: INTERCEPTED) -> Validation

typealias StateModifier<STATE, ACTION> = StateAction<STATE, ACTION, STATE>
typealias Reaction<STATE, ACTION> = StateAction<STATE, ACTION, Unit>
typealias StateAction<STATE, ACTION, RETURN> = (state: STATE, action: ACTION) -> RETURN

typealias Subscription<T> = (T) -> Unit

