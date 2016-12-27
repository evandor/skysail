package io.skysail.server.app.webconsole.osgi;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import io.skysail.server.app.webconsole.services.ServiceDescriptor;
import io.skysail.server.app.webconsole.services.ServiceDetails;
import io.skysail.server.app.webconsole.utils.BundleContextUtil;
import io.skysail.webconsole.osgi.entities.bundles.BundleDescriptor;
import io.skysail.webconsole.osgi.entities.bundles.BundleDetails;
import lombok.extern.slf4j.Slf4j;

@Component(service = OsgiService.class)
@Slf4j
public class OsgiService {

    private BundleContext bundleContext;

    @Activate
    protected void activate(ComponentContext ctx) {
        bundleContext = ctx.getBundleContext();
    }

    @Deactivate
    protected void deactivate() {
        bundleContext = null;
    }

    public List<BundleDescriptor> getBundleDescriptors() {
        if (bundleContext == null) {
            log.warn("bundleContext not available");
            return Collections.emptyList();
        }
        return Arrays.stream(bundleContext.getBundles()) // NOSONAR
                .map(b -> new BundleDescriptor(b,null))
                .sorted((b1, b2) -> Integer.valueOf(b1.getId()).compareTo(Integer.valueOf(b2.getId())))
                .collect(Collectors.toList());
    }

    public BundleDetails getBundleDetails(String id) {
        return Arrays.stream(bundleContext.getBundles())
                .filter(b -> id.equals(Long.toString(b.getBundleId())))
                .findFirst().map(b -> new BundleDetails(b,null))
                .orElseThrow(new Supplier<IllegalArgumentException>() {
                    @Override
                    public IllegalArgumentException get() {
                        return new IllegalArgumentException("yeah");
                    }
                });
    }

    public List<ServiceDescriptor> getOsgiServices() {
        try {
            ServiceReference<?>[] references = BundleContextUtil.getWorkingBundleContext(bundleContext)
                    .getAllServiceReferences(null, null);
            return Arrays.stream(references) // NOSONAR
                    .map(s -> new ServiceDescriptor(s))
                    .sorted((b1, b2) -> Integer.valueOf(b1.getId()).compareTo(Integer.valueOf(b2.getId())))
                    .collect(Collectors.toList());
        } catch (InvalidSyntaxException e) {
            log.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    public ServiceDetails getService(String serviceId) {
        try {
            ServiceReference<?>[] references = BundleContextUtil.getWorkingBundleContext(bundleContext)
                    .getAllServiceReferences(null, "(service.id=" + serviceId + ")");
            if (references == null || references.length == 0) {
                log.error("no service reference found for service.id = '{}'", serviceId);
            }
            return new ServiceDetails(references[0]);
        } catch (InvalidSyntaxException e) {
            log.error(e.getMessage(), e);
        }
        return null;

    }

}
