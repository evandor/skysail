package io.skysail.server.ext.akka

import language.postfixOps
import scala.concurrent.duration._
import akka.actor.Terminated
//import akka.cluster.Cluster
//import akka.cluster.ClusterEvent.{ CurrentClusterState, LeaderChanged }
import akka.event.Logging
//import akka.sample.osgi.api._
import akka.actor.{ RootActorPath, Address, ActorRef, Actor }
//import akka.sample.osgi.api.SubscribeToHakkerStateChanges
//import akka.sample.osgi.api.HakkerStateChange

class Chopstick extends Actor {

  val log = Logging(context.system, this)

  import context._
  
}