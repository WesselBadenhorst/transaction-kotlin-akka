import akka.actor.AbstractLoggingActor
import akka.actor.ActorRef
import akka.actor.ActorSystem.create
import akka.actor.Props
import akka.japi.pf.ReceiveBuilder
import java.time.Duration.ofMillis

fun main() {
    data class ScheduledMessage(val message: String)
    class ScheduledActor : AbstractLoggingActor() {
        override fun createReceive() = ReceiveBuilder()
            .match(ScheduledMessage::class.java) {
                with(context.system) {
                    scheduler().scheduleOnce(ofMillis(500), self, it.message, dispatcher(), self)
                }
            }
            .match(String::class.java) { log().info(it) }
            .build()
    }

    val actorSystem = create("once-off-scheduler")
    val repeatingScheduler = actorSystem.actorOf(Props.create(ScheduledActor::class.java), "scheduler-actor")
    repeatingScheduler.tell(ScheduledMessage("once off message"), ActorRef.noSender())
    repeatingScheduler.tell("immediate message", ActorRef.noSender())
    Thread.sleep(1000)
    actorSystem.terminate()
}
