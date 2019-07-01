package org.kstore.store

import react.kstore.Store
import rx.subjects.BehaviorSubject


open class TestStore<STATE : Any>(
        initialState: STATE
) : Store<STATE>(TestExceptionDispatcher) {

    private val subject = BehaviorSubject.create(initialState)!!
    override val observable = subject.toSerialized()!!

    fun setState(state: STATE) = subject.onNext(state)

    override val state: STATE
        get() = subject.value

    fun stop(){
        subject.onCompleted()
    }
}
