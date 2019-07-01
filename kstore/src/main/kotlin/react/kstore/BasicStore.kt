package react.kstore

import react.kstore.action.*
import react.kstore.dependency.DependsOnBuilder
import react.kstore.reaction.ConstructorUpdateableStoreReaction
import rx.subjects.BehaviorSubject


open class BasicStore<STATE : Any>(
        initialState: STATE,
        reactions: ConstructorUpdateableStoreReaction<STATE>.() -> Unit = {},
        dispatcher: Dispatcher = CommonDispatcher,
        dependsOn: DependsOnBuilder<STATE>.() -> Unit = {}
) : UpdateableStore<STATE>(dispatcher) {

    private val subject = BehaviorSubject.create(initialState)!!

    override val observable = subject.toSerialized()!!
        get() {
            logger.logInfo { "Someone subscribed by observable" }
            return field
        }

    init {
        logger.logInfo { "${this::class.qualifiedName} is starting" }
        DependsOnBuilder(this).dependsOn()

        ConstructorUpdateableStoreReaction(this).reactions()
        subject.doOnError { logger.error("Error on store update. Store is in an inconsistent state. State will be not changed", it) }
    }

    override val state: STATE
        get() = subject.value

    override fun updateState(state: STATE) {
//        logger.log { "Updating state: \nold=${subject.value}\nnew=$state" }
        subject.onNext(state)
    }

    fun stop() {
        subject.onCompleted()
        interceptors.forEach { it.unsubscribe() }
    }
}
