import akka.actor.AbstractLoggingActor
import akka.actor.Actor.noSender
import akka.actor.ActorSystem.create
import akka.actor.Props.create
import akka.japi.pf.ReceiveBuilder

fun main() {
    class HelloActor : AbstractLoggingActor() {
        override fun createReceive() = ReceiveBuilder()
            .match(String::class.java) {
                log().info("Hello $it!")
            }.build()
    }

    val actorSystem = create("example-hello")
    val actorRef = actorSystem.actorOf(
        create(HelloActor::class.java),
        "hello1"
    )
    actorSystem.log().info("Sending 'Tim'")
    actorRef.tell("Tom", noSender())
    Thread.sleep(1000)
    actorSystem.terminate()
}
