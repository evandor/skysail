package io.skysail.server.commands;

import java.util.Arrays;

import org.apache.felix.service.command.CommandProcessor;
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
        System.out.println("installed bundles (without version)"); // NOSONAR
        System.out.println("==================================="); // NOSONAR
        Arrays.stream(ctx.getBundleContext().getBundles()).forEach(b -> {
            System.out.println(b.getSymbolicName());
        });
        System.out.println("installed bundles (with version)"); // NOSONAR
        System.out.println("================================"); // NOSONAR
        Arrays.stream(ctx.getBundleContext().getBundles()).forEach(b -> {
            System.out.println(b.getSymbolicName() + "("+b.getVersion()+")");
        });
    }

}
