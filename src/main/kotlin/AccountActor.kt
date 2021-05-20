import akka.actor.typed.Behavior
import akka.actor.typed.javadsl.ActorContext
import akka.actor.typed.javadsl.Behaviors

class AccountActor(private val context: ActorContext<AccountEvent>, val accountId: String) {
    var balance = 0

    fun behavior(): Behavior<AccountEvent> = Behaviors.receive(AccountEvent::class.java)
        .onMessage(Credit::class.java, ::onCredit)
        .onMessage(Debit::class.java, ::onDebit)
        .build()

    private fun onDebit(message: Debit): Behavior<AccountEvent> {
        balance += message.amount
        message.replyTo.tell(DebitAccountSuccess(context.self, message.amount, balance))
        context.log.info("DEBIT ${message.amount} to $accountId BALANCE: $balance")
        return Behaviors.same()
    }

    private fun onCredit(message: Credit): Behavior<AccountEvent> {
        balance -= message.amount
        message.replyTo.tell(CreditAccountSuccess(context.self, message.amount, balance))
        context.log.info("CREDIT ${message.amount} from $accountId BALANCE: $balance")
        return Behaviors.same()
    }

    companion object {
        fun create(accountId: String): Behavior<AccountEvent> = Behaviors.setup { AccountActor(it, accountId).behavior() }
    }
}
