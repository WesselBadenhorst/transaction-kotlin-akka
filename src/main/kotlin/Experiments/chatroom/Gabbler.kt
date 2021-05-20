package Experiments.chatroom

import akka.actor.typed.Behavior
import akka.actor.typed.javadsl.ActorContext
import akka.actor.typed.javadsl.Behaviors

// Does not Extend [AbstractBehavior] as this example is done in a functional style
class Gabbler(private val context: ActorContext<SessionEvent>) {
    fun behavior() = Behaviors.receive(SessionEvent::class.java)
        .onMessage(SessionDenied::class.java, ::onSessionDenied)
        .onMessage(SessionGranted::class.java, ::onSessionGranted)
        .onMessage(MessagePosted::class.java, ::onMessagePosted)
        .build()

    fun onSessionDenied(message: SessionDenied): Behavior<SessionEvent> {
        context.log.info("Cannot start char room session ${message.reason}")
        return Behaviors.stopped()
    }

    fun onSessionGranted(message: SessionGranted): Behavior<SessionEvent> {
        message.handle.tell(PostMessage("Hello world!"))
        return Behaviors.same()
    }

    fun onMessagePosted(message: MessagePosted): Behavior<SessionEvent> {
        context.log.info("Message has been posted by '${message.screenName}'")
        return Behaviors.stopped()
    }

    companion object {
        fun create(): Behavior<SessionEvent> = Behaviors.setup { Gabbler(it).behavior() }
    }
}
