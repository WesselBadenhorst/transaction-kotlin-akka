import akka.actor.typed.ActorRef

interface AccountCommand

data class Start(
    val replyTo: ActorRef<TransferEvent>,
    val account: ActorRef<AccountEvent>,
    val message: AccountEvent) : AccountCommand

data class DebitAccountSuccess(
    val account: ActorRef<AccountEvent>,
    val amount: Int,
    val balance: Int) : AccountCommand

data class CreditAccountSuccess(
    val account: ActorRef<AccountEvent>,
    val amount: Int,
    val balance: Int) : AccountCommand
