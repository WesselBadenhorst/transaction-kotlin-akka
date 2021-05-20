package Experiments.chatroom

import akka.actor.typed.ActorSystem
import akka.actor.typed.Behavior
import akka.actor.typed.Terminated
import akka.actor.typed.javadsl.Behaviors

fun create(): Behavior<Unit> = Behaviors.setup { context ->
    // Create the [ChatRoom] and [Gabbler] actors
    val chatRoom = context.spawn(ChatRoom.create(), "ChatRoom")
    val gabbler = context.spawn(Gabbler.create(), "Gabbler")

    // [ActorContext.watch] watches the [Gabbler] actor to emit a [Terminated] signa once completing
    context.watch(gabbler)
    // send message to the [ChatRoom]
    chatRoom.tell(GetSession("ol, Gabbler", gabbler))

    // [BehaviorBuilder.onSignal] receives the [Terminated] signal fron the [Gabbler] actor
    Behaviors.receive(Unit::class.java)
        // Forgetting to call [BehaviorBuilder.nSignal] when watching another ator will cause a [DeathPactException]
        .onSignal(Terminated::class.java) { Behaviors.stopped() }
        .build()
}

fun main() {
    ActorSystem.create(create(), "ChatRoomDemo")
}
