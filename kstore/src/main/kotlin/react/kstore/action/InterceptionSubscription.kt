package react.kstore.action


class InterceptionSubscription(
        private val unsubscription: () -> Unit
) : Subscription {

    @Volatile
    private var unsubscribedInternal = false

    override fun unsubscribe() {
        unsubscription()
        unsubscribedInternal = true
    }

    override val unsubscribed: Boolean
        get() = unsubscribedInternal
}
