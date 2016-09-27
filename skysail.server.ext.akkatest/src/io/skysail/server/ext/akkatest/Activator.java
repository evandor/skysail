package io.skysail.server.ext.akkatest;

import org.osgi.framework.BundleContext;

import akka.actor.ActorSystem;
import akka.osgi.ActorSystemActivator;

public class Activator extends ActorSystemActivator  {

	@Override
	public void configure(BundleContext context, ActorSystem system) {
		registerService(context, system);
		
		//system.actorOf(props)
	}

}
