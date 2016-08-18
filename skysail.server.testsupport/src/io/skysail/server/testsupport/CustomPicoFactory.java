package io.skysail.server.testsupport;

import cucumber.runtime.java.picocontainer.PicoFactory;

public class CustomPicoFactory extends PicoFactory {
    public CustomPicoFactory() {
        addClass(DomainAutomationApi.class);
    }
}
