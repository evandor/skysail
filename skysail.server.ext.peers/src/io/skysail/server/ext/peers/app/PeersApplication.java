package io.skysail.server.ext.peers.app;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.event.EventAdmin;

import io.skysail.domain.core.Repositories;
import io.skysail.server.app.ApiVersion;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.security.config.SecurityConfigBuilder;

@Component(immediate = true)
public class PeersApplication extends SkysailApplication implements ApplicationProvider {

	@Reference(cardinality = ReferenceCardinality.OPTIONAL)
	private volatile EventAdmin eventAdmin;

	public PeersApplication() {
		super("peers", new ApiVersion(1));
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

		router.attach(new RouteBuilder("/heartbeat/{id}/", PutHeartbeatResource.class));
	}
}
