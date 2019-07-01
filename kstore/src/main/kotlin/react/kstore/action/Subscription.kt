package react.kstore.action


interface Subscription {
    fun unsubscribe()

    val unsubscribed: Boolean
}
