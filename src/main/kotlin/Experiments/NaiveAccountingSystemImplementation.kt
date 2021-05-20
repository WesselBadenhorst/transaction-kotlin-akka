import akka.actor.AbstractLoggingActor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.japi.pf.ReceiveBuilder

fun main() {

    data class CreditMessage(val amount: Int)
    data class DebitMessage(val amount: Int)

    class AccountActor(val accountId: String) : AbstractLoggingActor() {
        // Simulation of possible troublesome remove service
        override fun createReceive() = ReceiveBuilder()
            .match(CreditMessage::class.java) {

            }
            .match(DebitMessage::class.java) {

            }
            .match(String::class.java) { message ->
                log().info("Message Received: $message")
            }
            .build()
    }

    class TransferProcessActor : AbstractLoggingActor() {
        // The main actor that coordinates the process of debiting and crediting
        // From saga perspective this is the Saga Execution Component
        // Manages the transfer process
        override fun createReceive() = ReceiveBuilder()
            .match(String::class.java) { message ->
                log().info("Message Received: $message")
            }
            .build()
    }

    class AccountProxyActor : AbstractLoggingActor() {
        // Sole purpose of attempting to communicate with AccountActor
        override fun createReceive() = ReceiveBuilder()
            .match(String::class.java) { message ->
                log().info("Message Received: $message")
            }
            .build()

    }

    val actorSystem = ActorSystem.create("account-actor-system")

    Thread.sleep(1000)
    actorSystem.terminate()
}
