package react.kstore

import org.assertj.core.api.Assertions.assertThat
import org.awaitility.Awaitility.await
import org.awaitility.Duration
import org.junit.jupiter.api.*
import react.kstore.action.*
import react.kstore.action.Subscription
import kotlin.reflect.KClass


internal class DispatcherImplTest {


    @Nested
    @DisplayName("For sync emitted events")
    inner class SyncEmittedDispatchTest : EmittedDispatchTest({ dispatcher, action -> dispatcher.action(action) })

    @Nested
    @DisplayName("For async emitted events")
    inner class AsyncEmittedDispatchTest : EmittedDispatchTest({ dispatcher, action -> dispatcher.asyncAction(action) })

    open inner class EmittedDispatchTest(
            private val emit: (dispatcher: DispatcherImpl, action: Any) -> Unit
    ) {
        private val observed: MutableList<Action1> = mutableListOf()
        private val failedActions: MutableList<FailedAction> = mutableListOf()
        private val dispatcher = DispatcherImpl()

        @Nested
        @DisplayName("check interceptors")
        inner class DispatcherInterceptorTest {
            private val intercepted: MutableList<KClass<*>> = mutableListOf()

            @Test
            fun `there is no default interceptor`() {
                val action = Action3("Hello", " foo")

                dispatcher.subscribe(Action3::class) { observed.add(it) }
                emit(dispatcher, action)

                await().atMost(Duration.ONE_SECOND).untilAsserted {
                    assertThat(intercepted).containsExactlyInAnyOrder()
                    assertThat(observed).containsExactly(action)
                }
            }

            @Test
            fun `interceptor will not be called without subscriber`() {
                val action = Action3("Hello", " foo")

                dispatcher.intercept(Action3::class, { it -> intercepted.add(Action3::class); it })
                emit(dispatcher, action)

                await().atMost(Duration.ONE_SECOND).untilAsserted {
                    assertThat(intercepted).isEmpty()
                    assertThat(observed).isEmpty()
                }
            }

            @Test
            fun `interceptor intercept exact class action`() {
                val action = Action2("Hello", " foo")

                dispatcher.intercept(Action2::class, { it -> intercepted.add(Action2::class); it })

                dispatcher.subscribe(Action2::class) { observed.add(it) }
                emit(dispatcher, action)

                await().atMost(Duration.ONE_SECOND).untilAsserted {
                    assertThat(intercepted).containsExactly(Action2::class)
                    assertThat(observed).containsExactly(action)
                }
            }

            @Test
            fun `interceptor intercept superclass action`() {
                val action = Action3("Hello", " foo")

                dispatcher.intercept(Action1::class, { it -> intercepted.add(Action1::class); it })
                dispatcher.intercept(Action2::class, { it -> intercepted.add(Action2::class); it })
                dispatcher.intercept(Action21::class, { it -> intercepted.add(Action21::class); it })

                dispatcher.subscribe(Action3::class) { observed.add(it) }
                emit(dispatcher, action)

                await().atMost(Duration.ONE_SECOND).untilAsserted {
                    assertThat(intercepted).containsExactlyInAnyOrder(Action1::class, Action2::class)
                    assertThat(observed).containsExactly(action)
                }
            }

            @Test
            fun `interceptor don't intercept subclass action`() {
                val action = Action1("Hello")

                dispatcher.intercept(Action2::class, { it -> intercepted.add(Action2::class); it })
                dispatcher.intercept(Action21::class, { it -> intercepted.add(Action21::class); it })

                dispatcher.subscribe(Action1::class) { observed.add(it) }
                emit(dispatcher, action)

                await().atMost(Duration.ONE_SECOND).untilAsserted {
                    assertThat(intercepted).isEmpty()
                    assertThat(observed).containsExactly(action)
                }
            }

            @Test
            fun `all existing observer can use newly created interceptor`() {
                val action = Action3("Hello", " foo")

                dispatcher.subscribe(Action3::class) { observed.add(it) }

                emit(dispatcher, action)
                await().atMost(Duration.ONE_SECOND).untilAsserted {
                    assertThat(observed).hasSize(1).containsOnly(action)
                    assertThat(intercepted).isEmpty()
                }

                dispatcher.intercept(Action1::class, { it -> intercepted.add(Action1::class); it })

                emit(dispatcher, action)
                await().atMost(Duration.ONE_SECOND).untilAsserted {
                    assertThat(observed).hasSize(2).containsOnly(action)
                    assertThat(intercepted).hasSize(1).containsOnly(Action1::class)
                }
            }

            @Test
            fun `interceptor can throw Exception and it will be dispatched as FailedAction`() {
                dispatcher.subscribe(FailedAction::class) { failedActions.add(it) }

                val action = Action3("Hello", " foo")

                dispatcher.subscribe(Action3::class) { observed.add(it) }
                dispatcher.intercept(Action1::class, { _ -> throw RuntimeException() })

                emit(dispatcher, action)
                await().atMost(Duration.ONE_SECOND).untilAsserted {
                    assertThat(observed).isEmpty()
                    assertThat(intercepted).isEmpty()
                    assertThat(failedActions)
                            .hasSize(1)
                            .allMatch { it.failed == action }
                }
            }


            @Test
            fun `interceptor can throw Error and it will stop Dispatcher`() {
                dispatcher.subscribe(FailedAction::class) { failedActions.add(it) }

                val error = Error()
                val action = Action3("Hello", " foo")

                dispatcher.subscribe(Action1::class) { observed.add(it) }
                dispatcher.intercept(Action2::class, { _ -> throw error })

                assertThat(dispatcher.consistent).isTrue()
                try {
                    emit(dispatcher, action)
                } catch (e: Throwable) {
                }
                await().atMost(Duration.ONE_SECOND).untilAsserted {
                    assertThat(dispatcher.consistent).isFalse()
                }

                emit(dispatcher, Action1("Hello"))
                await().atMost(Duration.ONE_SECOND).untilAsserted {
                    assertThat(observed).isEmpty()
                    assertThat(intercepted).isEmpty()
                    assertThat(failedActions).isEmpty()
                }
            }

            @Test
            fun `interceptor can change action class`() {
                val action = Action3("Hello", " foo")

                dispatcher.subscribe(Action3::class) { observed.add(it) }
                dispatcher.subscribe(Action1::class) { observed.add(it) }

                dispatcher.intercept(Action3::class, { action -> Action1(action.h) })

                emit(dispatcher, action)
                await().atMost(Duration.ONE_SECOND).untilAsserted {
                    assertThat(observed)
                            .hasSize(1)
                            .allSatisfy {
                                assertThat(it)
                                        .isInstanceOf(Action1::class.java)
                                        .hasFieldOrPropertyWithValue("h", action.h)
                            }
                }
            }
        }

        @Nested
        @DisplayName("check subscribers")
        inner class DispatcherSubscriberTest {

            @Test
            fun `subscriber can unsubscribe`() {
                val action = Action2("Hello", " foo")

                val subscription = dispatcher.subscribe(Action2::class) { observed.add(it) }

                emit(dispatcher, action)

                await().atMost(Duration.ONE_SECOND).untilAsserted {
                    assertThat(observed).hasSize(1).containsOnly(action)
                }

                assertThat(subscription.unsubscribed).isFalse()
                subscription.unsubscribe()
                assertThat(subscription.unsubscribed).isTrue()

                emit(dispatcher, action)

                await().atLeast(Duration.ONE_HUNDRED_MILLISECONDS).untilAsserted {
                    assertThat(observed).hasSize(1).containsOnly(action)
                }
            }

            @Test
            fun `subscriber will not receive old messages`() {
                val otherObserved: MutableList<Action1> = mutableListOf()
                val action = Action2("Hello", " foo")

                emit(dispatcher, action)

                await().atMost(Duration.ONE_SECOND).untilAsserted {
                    observed.clear()

                    var subscription: Subscription? = null
                    try {
                        subscription = dispatcher.subscribe(Action2::class) { observed.add(it) }
                        assertThat(observed).isEmpty()
                    } catch (e: Throwable) {
                        subscription?.unsubscribe()
                        throw e
                    }
                }

                emit(dispatcher, action)

                await().atMost(Duration.ONE_SECOND).untilAsserted {
                    assertThat(observed).hasSize(1).containsOnly(action)
                }

                dispatcher.subscribe(Action2::class) { otherObserved.add(it) }
                assertThat(otherObserved).isEmpty()

                emit(dispatcher, action)

                await().atLeast(Duration.ONE_HUNDRED_MILLISECONDS).untilAsserted {
                    assertThat(observed).hasSize(2).containsOnly(action)
                    assertThat(otherObserved).hasSize(1).containsOnly(action)
                }
            }

            @Test
            fun `subscriber subscribe to current class actions`() {
                val action = Action2("Hello", " foo")

                dispatcher.subscribe(Action2::class) { observed.add(it) }

                emit(dispatcher, action)

                await().atMost(Duration.ONE_SECOND).untilAsserted {
                    assertThat(observed).hasSize(1).containsOnly(action)
                }
            }

            @Test
            fun `subscriber subscribe to subclass actions`() {
                val action = Action3("Hello", " foo")

                dispatcher.subscribe(Action2::class) { observed.add(it) }

                emit(dispatcher, action)

                await().atMost(Duration.ONE_SECOND).untilAsserted {
                    assertThat(observed).hasSize(1).containsOnly(action)
                }
            }

            @Test
            fun `subscriber don't subscribe to superclass actions`() {
                val action = Action1("Hello")

                dispatcher.subscribe(Action2::class) { observed.add(it) }

                emit(dispatcher, action)
                await().atMost(Duration.ONE_SECOND).untilAsserted {
                    assertThat(observed).isEmpty()
                }
            }

            @Test
            fun `subscriber can throw Exception and it will be dispatched as FailedAction`() {
                dispatcher.subscribe(FailedAction::class) { failedActions.add(it) }
                val action = Action2("Hello", " foo")

                dispatcher.subscribe(Action2::class) { throw RuntimeException() }

                emit(dispatcher, action)

                await().atMost(Duration.ONE_SECOND).untilAsserted {
                    assertThat(observed).isEmpty()
                    assertThat(failedActions)
                            .hasSize(1)
                            .allMatch { it.failed == action }
                }
            }
        }
    }
}

open class Action1(
        val h: String
) {
    open fun print() {
        println(h)
    }

    override fun toString(): String {
        return "Action1(h='$h')"
    }
}

open class Action2(
        h: String,
        val e: String
) : Action1(h) {
    override fun print() {
        println(h + e)
    }

    override fun toString(): String {
        return "Action2(e='$e') ${super.toString()}"
    }
}

open class Action21(
        h: String,
        val e: Int
) : Action1(h) {
    override fun print() {
        println(h + e + "!")
    }

    override fun toString(): String {
        return "Action21(e=$e) ${super.toString()}"
    }
}

open class Action3(
        h: String,
        e: String
) : Action2(h, e) {
    override fun print() {
        println(h + e + "?")
    }

    override fun toString(): String {
        return "Action3() ${super.toString()}"
    }
}

