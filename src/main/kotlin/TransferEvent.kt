import akka.actor.typed.ActorRef

interface TransferEvent

data class StartTransfer(
    val debitAccount: ActorRef<AccountEvent>,
    val creditAccount: ActorRef<AccountEvent>,
    val amount: Int) : TransferEvent

data class Stop(val balance: Int, val reason: String) : TransferEvent

data class Fail(val reason: String) : TransferEvent

data class DebitSuccess(
    val debitAccount: ActorRef<AccountCommand>,
    val debitAccountBalance: Int,
    val amount: Int) : TransferEvent

data class CreditSuccess(
    val creditAccount: ActorRef<AccountCommand>,
    val creditAccountBalance: Int,
    val amount: Int) : TransferEvent

data class CreditFailed(
    val debitAccount: ActorRef<AccountCommand>,
    val creditAccount: ActorRef<AccountCommand>,
    val amount: Int) : TransferEvent

data class Succeed(
    val debitAccount: ActorRef<AccountCommand>,
    val debitAccountBalance: Int,
    val creditAccount: ActorRef<AccountCommand>,
    val creditAccountBalance: Int) : TransferEvent
