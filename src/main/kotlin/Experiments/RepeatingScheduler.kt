import akka.actor.AbstractLoggingActor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Cancellable
import akka.actor.Props
import akka.japi.pf.ReceiveBuilder
import java.time.Duration

fun main() {

    data class ScheduledMessage(val message: String)

    class ScheduledActor : AbstractLoggingActor() {
        private lateinit var cancellable: Cancellable

        override fun postStop() {
            super.postStop()
            log().info("Cancelling the repeating task")
            cancellable.cancel()
        }

        override fun createReceive() = ReceiveBuilder()
            .match(ScheduledMessage::class.java) {
                cancellable = with(context.system) {
                    scheduler().schedule(Duration.ofMillis(0), Duration.ofMillis(100), self, it.message, dispatcher(), self)
                }
            }
            .match(String::class.java) { log().info(it) }
            .build()
    }

    val actorSystem = ActorSystem.create("repeating-scheduler")
    val repeatingScheduler = actorSystem.actorOf(Props.create(ScheduledActor::class.java), "scheduled-actor")
    repeatingScheduler.tell(ScheduledMessage("repeated task"), ActorRef.noSender())
    Thread.sleep(500)
    actorSystem.stop(repeatingScheduler)
    Thread.sleep(1000)
    actorSystem.terminate()
}
