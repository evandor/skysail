package io.skysail.server.app.ref.one2many.noagg.test;

import cucumber.runtime.java.picocontainer.PicoFactory;

public class CustomPicoFactory extends PicoFactory {
    public CustomPicoFactory() {
        addClass(DomainAutomationApi.class);
    }
}
