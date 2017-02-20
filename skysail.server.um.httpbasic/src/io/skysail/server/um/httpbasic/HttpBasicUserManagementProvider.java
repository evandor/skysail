package io.skysail.server.um.httpbasic;

import java.util.Collection;
import java.util.HashSet;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.restlet.security.Verifier;

import io.skysail.api.um.UserManagementProvider;
import io.skysail.core.app.ApplicationProvider;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Central Component for the http basic user management service.
 *
 * Provides both AuthenticationService and AuthorizationService as usual.
 *
 */
@Component(immediate = false, configurationPolicy = ConfigurationPolicy.OPTIONAL, property = {
        "service.ranking:Integer=10" })
@Slf4j
public class HttpBasicUserManagementProvider implements UserManagementProvider {

    @Getter
    private HttpBasicAuthenticationService authenticationService;

    @Getter
    private HttpBasicAuthorizationService authorizationService;

    @Getter
    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.AT_LEAST_ONE)
    private volatile Collection<Verifier> verifiers = new HashSet<>();

    @Getter
    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY, target = "(name=HttpBasicUmApplication)")
    private volatile ApplicationProvider skysailApplication;

    @Getter
    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY)
    private volatile io.skysail.api.um.UserManagementRepository userManagementRepository;

    @Activate
    public void activate() {
        log.info("user management provider: activating provider '{}'", this.getClass().getName());
        try {
            authenticationService = new HttpBasicAuthenticationService(this);
            authorizationService = new HttpBasicAuthorizationService(this);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.error("===============================================");
            log.error("installation cannot run without usermanagement!");
            log.error("===============================================");
            throw e;
        }
    }

    @Deactivate
    public void deactivate() {
        log.info("user management provider: deactivating provider '{}'", this.getClass().getName());
        authenticationService = null;
        authorizationService = null;
    }

}
