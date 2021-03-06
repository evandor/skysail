package io.skysail.server.app.metrics;

import java.util.Arrays;
import java.util.List;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import io.skysail.api.metrics.MetricsImplementation;
import io.skysail.api.metrics.TimerDataProvider;
import io.skysail.core.app.ApiVersion;
import io.skysail.core.app.ApplicationConfiguration;
import io.skysail.core.app.ApplicationProvider;
import io.skysail.core.app.SkysailApplication;
import io.skysail.server.app.metrics.resources.BookmarkResource;
import io.skysail.server.app.metrics.resources.TimersChartResource;
import io.skysail.server.app.metrics.resources.TimersResource;
import io.skysail.server.menus.MenuItem;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.security.config.SecurityConfigBuilder;

@Component(immediate = true)
public class MetricsApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "MetricsApp";

    public MetricsApplication() {
        super(APP_NAME, new ApiVersion(1));
        setDescription("a skysail application");
    }

    @Reference //(cardinality =  ReferenceCardinality.MANDATORY)
    private volatile MetricsImplementation metricsImpl;

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

        router.attach(new RouteBuilder("/Bookmarks/{id}", BookmarkResource.class));
        //router.attach(new RouteBuilder("/Bookmarks/{id}/", PutBookmarkResource.class));
        router.attach(new RouteBuilder("/Timers", TimersResource.class));
        router.attach(new RouteBuilder("/Timers/chart", TimersChartResource.class));
        router.attach(new RouteBuilder("", TimersResource.class));
    }

    @Override
    public List<MenuItem> getMenuEntries() {
        MenuItem appMenu = new MenuItem(getName(), "/" + getName() + getApiVersion().getVersionPath());
        appMenu.setCategory(MenuItem.Category.ADMIN_MENU);
        return Arrays.asList(appMenu);
    }

	public List<TimerDataProvider> getTimerMetrics() {
		return metricsImpl.getTimers();
	}

}