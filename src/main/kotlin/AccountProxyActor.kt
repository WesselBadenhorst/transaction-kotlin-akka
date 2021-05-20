import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.javadsl.ActorContext
import akka.actor.typed.javadsl.Behaviors

// Created by the TransferProcess actor for the sole purpose of
// communicating with the Account actor and handling the responses
class AccountProxyActor(val context: ActorContext<AccountCommand>) {
    lateinit var replyTo: ActorRef<TransferEvent>

    fun behavior(): Behavior<AccountCommand> = Behaviors.receive(AccountCommand::class.java)
        .onMessage(Start::class.java, ::onStart)
        .onMessage(DebitAccountSuccess::class.java, ::onDebitAccountSuccess)
        .onMessage(CreditAccountSuccess::class.java, ::onCreditAccountSuccess)
        .build()

    private fun onStart(message: Start): Behavior<AccountCommand> {
        replyTo = message.replyTo
        message.account.tell(message.message)
        return Behaviors.same()
    }

    private fun onDebitAccountSuccess(message: DebitAccountSuccess): Behavior<AccountCommand> {
        replyTo.tell(DebitSuccess(context.self, message.balance, message.amount))
        return Behaviors.stopped()
    }

    private fun onCreditAccountSuccess(message: CreditAccountSuccess): Behavior<AccountCommand> {
        replyTo.tell(CreditSuccess(context.self, message.balance, message.amount))
        return Behaviors.stopped()
    }

    companion object {
        fun create(): Behavior<AccountCommand> = Behaviors.setup { AccountProxyActor(it).behavior() }
    }
}
