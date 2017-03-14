package io.skysail.server.commands;

import java.util.stream.IntStream;

import org.apache.felix.service.command.CommandProcessor;
import org.osgi.framework.Bundle;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

@Component(property = {
        CommandProcessor.COMMAND_SCOPE + ":String=skysail",
        CommandProcessor.COMMAND_FUNCTION + ":String=fingerprint",
}, service = Object.class)
public class FingerprintCommand { // NO_UCD (unused code)

    private ComponentContext ctx;

    @Activate
    public void activate(ComponentContext ctx) {
        this.ctx = ctx;
    }

    @Deactivate
    public void deactivate(ComponentContext ctx) {
        this.ctx = null;
    }

    public void fingerprint() {
        Bundle[] bundles = ctx.getBundleContext().getBundles();

        System.out.println("installed bundles (without version)"); // NOSONAR
        System.out.println("==================================="); // NOSONAR
        IntStream.range(0,bundles.length).forEach(n -> printSimple(bundles, n));

        System.out.println("installed bundles (with version)"); // NOSONAR
        System.out.println("================================"); // NOSONAR
        IntStream.range(0,bundles.length).forEach(n -> printExtended(bundles, n));
    }

    private void printSimple(Bundle[] bundles, int n) {
        System.out.println(n + ": " + bundles[n].getSymbolicName()); // NOSONAR
    }

    private void printExtended(Bundle[] bundles, int n) {
        System.out.println(n + ": " + bundles[n].getSymbolicName() + "("+bundles[n].getVersion()+")"); // NOSONAR
    }

}
