package io.skysail.server.um.aws;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import io.skysail.api.um.UserManagementProvider;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.ext.aws.AwsConfiguration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Central Component for the amazon web service (AWS) user management service.
 *
 * Provides both AuthenticationService and AuthorizationService as usual.
 *
 */
@Component(immediate = false, configurationPolicy = ConfigurationPolicy.OPTIONAL, property = {
        "service.ranking:Integer=5" })
@Slf4j
public class AwsUserManagementProvider implements UserManagementProvider {

    @Getter
    private AwsAuthenticationService authenticationService;

    @Getter
    private AwsAuthorizationService authorizationService;

    @Getter
    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private AwsConfiguration awsConfiguration;

    @Getter
    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY, target = "(name=AwsUmApplication)")
    private volatile ApplicationProvider skysailApplication;

//    @Getter
//    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.AT_LEAST_ONE)
//    private volatile Collection<Verifier> verifiers = new HashSet<>();

    @Activate
    public void activate() {
        log.info("user management provider: activating provider '{}'", this.getClass().getName());
        try {
            authenticationService = new AwsAuthenticationService(this);
            authorizationService = new AwsAuthorizationService(this);
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
