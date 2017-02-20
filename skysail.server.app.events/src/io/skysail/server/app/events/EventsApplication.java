package io.skysail.server.app.events;

import java.util.ArrayList;
import java.util.List;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

import io.skysail.core.app.ApiVersion;
import io.skysail.core.app.ApplicationConfiguration;
import io.skysail.core.app.ApplicationProvider;
import io.skysail.core.app.SkysailApplication;
import io.skysail.server.app.events.domain.EventDesc;
import io.skysail.server.app.events.resources.EventsResource;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.security.config.SecurityConfigBuilder;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL, property = { EventConstants.EVENT_TOPIC+"=*"})
public class EventsApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider, EventHandler {

    public static final String APP_NAME = "events";

    private List<EventDesc> events = new ArrayList<>();

    public EventsApplication() {
        super(APP_NAME, new ApiVersion(1));
        setDescription("a skysail application");
    }

    @Activate
    @Override
    public void activate(ApplicationConfiguration appConfig, ComponentContext componentContext)
            throws ConfigurationException {
        super.activate(appConfig, componentContext);
    }

    @Override
    protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
        securityConfigBuilder
            .authorizeRequests().startsWithMatcher("").authenticated();
    }

    @Override
    protected void attach() {
        super.attach();
        router.attach(new RouteBuilder("", EventsResource.class));
        router.attach(new RouteBuilder("/events", EventsResource.class));
    }

    public List<EventDesc> getEvents() {
        return this.events;
    }

    @Override
    public void handleEvent(Event event) {
        this.events.add(new EventDesc(event));
    }

    public void clearEvents() {
        this.events.clear();
    }

}