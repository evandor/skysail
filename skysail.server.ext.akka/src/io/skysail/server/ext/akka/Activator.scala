package io.skysail.server.ext.akka

import akka.actor.{ Props, ActorSystem }
import org.osgi.framework.BundleContext
import akka.osgi.ActorSystemActivator
import akka.event.{ LogSource, Logging }

//http://doc.akka.io/docs/akka/2.4.11/additional/osgi.html
class Activator extends ActorSystemActivator {
 
  def configure(context: BundleContext, system: ActorSystem) {
    val log = Logging(system, this)
    log.info("Core bundle configured")
    // optionally register the ActorSystem in the OSGi Service Registry
    registerService(context, system)
 
    val someActor = system.actorOf(Props[Table], name = "someName")
    //someActor ! SomeMessage
  }
 
}

object Activator {
  implicit val logSource: LogSource[AnyRef] = new LogSource[AnyRef] {
    def genString(o: AnyRef): String = o.getClass.getName
    override def getClazz(o: AnyRef): Class[_] = o.getClass
  }
}