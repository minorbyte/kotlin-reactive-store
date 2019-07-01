package react.kstore.action


class RxSubscription(val subscription: rx.Subscription) : Subscription {
    override fun unsubscribe() {
        subscription.unsubscribe()
    }

    override val unsubscribed: Boolean
        get() = subscription.isUnsubscribed
}
