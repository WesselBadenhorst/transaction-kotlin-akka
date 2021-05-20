package Experiments.helloworldcomplex

import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.actor.typed.Behavior
import akka.actor.typed.javadsl.AbstractBehavior
import akka.actor.typed.javadsl.ActorContext
import akka.actor.typed.javadsl.Behaviors

class HelloWorldMain(
    context: ActorContext<Start>,
    val greeter: ActorRef<HelloWorld.Greet>) : AbstractBehavior<HelloWorldMain.Start>(context) {

    data class Start(val name: String)

    override fun createReceive() = newReceiveBuilder()
        .onMessage(Start::class.java, ::onStart).build()

    fun onStart(command: Start): Behavior<Start> {
        val replyTo: ActorRef<HelloWorld.Greeted> = context.spawn(HelloWorldBot.create(3), command.name)
        greeter.tell(HelloWorld.Greet(command.name, replyTo))
        return this
    }

    companion object {
        fun create(): Behavior<Start> = Behaviors.setup { context ->
            HelloWorldMain(context, context.spawn(HelloWorld.create(), "greeter"))
        }
    }
}

fun main() {
    val system = ActorSystem.create(HelloWorldMain.create(), "hello")
    system.tell(HelloWorldMain.Start("Dan"))
    system.tell(HelloWorldMain.Start("Laura"))
}
