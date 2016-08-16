package io.skysail.server.app.ref.singleentity;

import cucumber.runtime.java.picocontainer.PicoFactory;

public class CustomPicoFactory extends PicoFactory {
	public CustomPicoFactory() {
//		if ("web".equals(System.getProperty("my.app.testDepth"))) {
//			addClass(WebAutomationApi.class);
//		} else {
			addClass(DomainAutomationApi.class);
		//}
	}
}
