package io.skysail.server.ext.akka

import akka.actor.{ Props, Actor }

class Table extends Actor {
  val chopsticks = for (i <- 1 to 5) yield context.actorOf(Props[Chopstick], "Chopstick" + i)

  def receive = {
    case x: Int => sender() ! ((chopsticks(x), chopsticks((x + 1) % 5)))
  }
}