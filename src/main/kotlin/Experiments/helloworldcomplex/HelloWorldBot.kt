package Experiments.helloworldcomplex

import akka.actor.typed.Behavior
import akka.actor.typed.javadsl.AbstractBehavior
import akka.actor.typed.javadsl.ActorContext
import akka.actor.typed.javadsl.Behaviors


class HelloWorldBot(context: ActorContext<HelloWorld.Greeted>, val max: Int) : AbstractBehavior<HelloWorld.Greeted>(context) {
    var counter = 0

    override fun createReceive() = newReceiveBuilder()
        .onMessage(HelloWorld.Greeted::class.java, ::onGreeted)
        .build()

    fun onGreeted(message: HelloWorld.Greeted): Behavior<HelloWorld.Greeted> {
        counter++
        context.log.info("Greeting $counter for ${message.whom}")
        if(counter == max) {
            return Behaviors.stopped()
        } else {
            message.from.tell(HelloWorld.Greet(message.whom, context.self))
        }
        return this
    }

    companion object {
        fun create(max: Int): Behavior<HelloWorld.Greeted> = Behaviors.setup { HelloWorldBot(it, max) }
    }
}
