package Experiments.chatroom

interface SessionCommand

data class PostMessage(val message: String) : SessionCommand
data class NotifyClient(val message: MessagePosted) : SessionCommand
