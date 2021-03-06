import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.javadsl.AbstractBehavior
import akka.actor.typed.javadsl.ActorContext
import akka.actor.typed.javadsl.Behaviors

class HelloWorld(context: ActorContext<Greet>) : AbstractBehavior<HelloWorld.Greet>(context) {
    data class Greet(val whom: String, val replyTo: ActorRef<Greeted>)
    data class Greeted(val whom: String, val from: ActorRef<Greet>)

    override fun createReceive() = newReceiveBuilder()
        .onMessage(Greet::class.java, ::onGreet)
        .build()

    private fun onGreet(command: Greet): Behavior<Greet> {
        context.log.info("Hello ${command.whom}!")
        command.replyTo.tell(Greeted(command.whom, context.self))
        return this
    }

    companion object {
        fun create(): Behavior<HelloWorld.Greet> = Behaviors.setup { HelloWorld(it) }
    }
}
