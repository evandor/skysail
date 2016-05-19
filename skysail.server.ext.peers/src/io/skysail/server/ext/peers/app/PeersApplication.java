package io.skysail.server.ext.peers.app;

import java.util.Arrays;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.event.EventAdmin;

import io.skysail.domain.core.Repositories;
import io.skysail.server.app.ApiVersion;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.menus.MenuItem;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.security.config.SecurityConfigBuilder;

@Component(immediate = true)
public class PeersApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

	private static final String APP_NAME = "peers";
	@Reference(cardinality = ReferenceCardinality.OPTIONAL)
	private volatile EventAdmin eventAdmin;

	public PeersApplication() {
		super(APP_NAME, new ApiVersion(1));
		setDescription("The skysail peers application");
	}

	@Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
	@Override
	public void setRepositories(Repositories repos) {
		super.setRepositories(repos);
	}

	public void unsetRepositories(Repositories repo) { // NOSONAR
		super.setRepositories(null);
	}

	@Override
	protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
		securityConfigBuilder.authorizeRequests().startsWithMatcher("").permitAll();
	}

	@Override
	protected void attach() {
		super.attach();

		router.attach(new RouteBuilder("", HeartbeatsResource.class));
		router.attach(new RouteBuilder("/heartbeats", HeartbeatsResource.class));
		router.attach(new RouteBuilder("/heartbeats/{id}/", PutHeartbeatResource.class));
	}

	@Override
	public List<MenuItem> getMenuEntries() {
		MenuItem appMenu = new MenuItem(APP_NAME, "/" + APP_NAME + getApiVersion().getVersionPath());
		appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
		return Arrays.asList(appMenu);
	}
}
