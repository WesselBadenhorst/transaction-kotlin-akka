import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.javadsl.ActorContext
import akka.actor.typed.javadsl.Behaviors

// This is the Saga Execution Component. Keeps track of the steps
// that have been completed and applies any compensating actions on failures.
// Behaviors is modeled as a State Machine
// - Starting -> { AwaitingDebitConfirmation }
// - AwaitingDebitConfirmation -> { AwaitingCreditConfirmation, Stop(ConsistentSystem), Stop(Unknown) }
// - AwaitingCreditConfirmation -> { Stop(Success), RollingBackDebit, Stop(Unknown) }
// - RollingBackDebit -> { Stop(ConsistentSystem), Stop(Unknown) }
class TransferProcessActor(val context: ActorContext<TransferEvent>) {
    lateinit var debitAccount: ActorRef<AccountEvent>
    lateinit var creditAccount: ActorRef<AccountEvent>

    fun starting(): Behavior<TransferEvent> = Behaviors.receive(TransferEvent::class.java)
        .onMessage(StartTransfer::class.java) { message ->
            debitAccount = message.debitAccount
            creditAccount = message.creditAccount
            // start debit part of the transaction via account proxy
            val debitAccountProxy = context.spawn(AccountProxyActor.create(), "debitAccount")
            debitAccountProxy.tell(
                Start(
                    context.self,
                    debitAccount,
                    Debit(debitAccountProxy, message.amount)
                )
            )
            awaitingDebitConfirmation()
        }.build()

    fun awaitingDebitConfirmation(): Behavior<TransferEvent> = Behaviors.receive(TransferEvent::class.java)
        .onMessage(DebitSuccess::class.java) { message ->
            val creditAccountProxy = context.spawn(AccountProxyActor.create(), "creditAccount")
            creditAccountProxy.tell(
                Start(
                    context.self,
                    creditAccount,
                    Credit(creditAccountProxy, message.amount)
                )
            )
            awaitingCreditConfirmation()
        }.build()

    fun awaitingCreditConfirmation(): Behavior<TransferEvent> = Behaviors.receive(TransferEvent::class.java)
        .onMessage(CreditSuccess::class.java) { message ->

            Behaviors.stopped()
        }.build()

    fun RollingBackDebit(): Behavior<TransferEvent> {
        TODO("Not Implemented yet")
        return Behaviors.stopped()
    }

    companion object {
        fun create(): Behavior<TransferEvent> = Behaviors.setup { TransferProcessActor(it).starting() }
    }
}
