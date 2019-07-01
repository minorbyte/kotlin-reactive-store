package org.kstore.store

import react.kstore.*
import rx.subjects.BehaviorSubject


open class TestSubscribable<STATE : Any>(
        initialState: STATE
) : Subscribable<STATE>(TestExceptionDispatcher) {

    private val subject = BehaviorSubject.create(initialState)!!
    override val observable = subject.toSerialized()!!
        get() {
            logger.logInfo { "Someone subscribed by observable" }
            return field
        }

    fun setState(state: STATE) = subject.onNext(state)

    val state: STATE
        get() = subject.value

    fun stop() {
        subject.onCompleted()
    }
}
