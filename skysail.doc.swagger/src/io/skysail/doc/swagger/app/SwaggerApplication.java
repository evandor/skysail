package io.skysail.doc.swagger.app;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.restlet.Restlet;

import io.skysail.core.app.ApiVersion;
import io.skysail.core.app.ApplicationConfiguration;
import io.skysail.core.app.ApplicationProvider;
import io.skysail.core.app.SkysailApplication;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class SwaggerApplication extends SkysailApplication implements ApplicationProvider {

    private Map<String, Restlet> restlets = new HashMap<>();

    public SwaggerApplication() {
        super("_doc/swagger/2.0", new ApiVersion(1));
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MULTIPLE)
    public synchronized void addApplicationProvider(ApplicationProvider provider) {
        SkysailApplication application = provider.getApplication();
        if (application == null) {
            return;
        }
        Restlet swaggerRestlet = new SwaggerRestlet(provider.getApplication());
        restlets.put(getIdentifier(application), swaggerRestlet);
    }

    public synchronized void removeApplicationProvider(ApplicationProvider provider) {
        SkysailApplication application = provider.getApplication();
        if (application == null) {
            return;
        }
        router.detach(restlets.get(getIdentifier(application)));
    }

    @Activate
    @Override
    public void activate(ApplicationConfiguration appConfig, ComponentContext componentContext)
            throws ConfigurationException {
        super.activate(appConfig, componentContext);
    }

    @Override
    protected void attach() {
        restlets.keySet().stream()
            .forEach(key -> router.attach("/api/" + key, restlets.get(key)));
    }

    private String getIdentifier(SkysailApplication application) {
        return application.getName() + application.getApiVersion().getVersionPath();
    }



}
