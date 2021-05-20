import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.actor.typed.Behavior
import akka.actor.typed.Terminated
import akka.actor.typed.javadsl.Behaviors

fun create(): Behavior<Unit> = Behaviors.setup { context ->
    var accounts = mutableListOf<ActorRef<AccountEvent>>()
    (1..1_000).forEach { accounts.add(context.spawn(AccountActor.create(it.toString()), "Account$it")) }

    (1..2_000_000).forEach {
        accounts.shuffle()
        val transferFrom = accounts.first()
        val transferTo = accounts.last()

        val transferProcessActor = context.spawn(TransferProcessActor.create(), "Transfer$it")
        transferProcessActor.tell(StartTransfer(transferFrom, transferTo, 30))
    }

    Behaviors.receive(Unit::class.java)
        .onSignal(Terminated::class.java) { Behaviors.stopped() }
        .build()
}

fun main() {
    ActorSystem.create(create(), "TransactionsDemo")
}
