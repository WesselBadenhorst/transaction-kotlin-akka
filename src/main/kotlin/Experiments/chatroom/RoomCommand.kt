package Experiments.chatroom

import akka.actor.typed.ActorRef

interface RoomCommand

data class GetSession(val screenName: String, val replyTo: ActorRef<SessionEvent>) : RoomCommand
