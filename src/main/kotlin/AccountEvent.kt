import akka.actor.typed.ActorRef

interface AccountEvent

data class BalanceEnquiry(val replyTo: ActorRef<AccountCommand>) : AccountEvent
data class Credit(val replyTo: ActorRef<AccountCommand>, val amount: Int) : AccountEvent
data class Debit(val replyTo: ActorRef<AccountCommand>, val amount: Int) : AccountEvent
