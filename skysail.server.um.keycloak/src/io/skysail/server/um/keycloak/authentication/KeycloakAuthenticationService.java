package io.skysail.server.um.keycloak.authentication;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.Principal;
import java.util.Properties;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.security.Authenticator;
import org.restlet.security.User;

import io.skysail.api.links.Link;
import io.skysail.api.um.AuthenticationMode;
import io.skysail.api.um.AuthenticationService;
import io.skysail.core.utils.LinkUtils;
import io.skysail.server.um.keycloak.KeycloakBasedUserManagementProvider;
import io.skysail.server.um.keycloak.app.LoginResource;
import io.skysail.server.utils.PasswordUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KeycloakAuthenticationService implements AuthenticationService {

    private KeycloakBasedUserManagementProvider provider;

    public KeycloakAuthenticationService(KeycloakBasedUserManagementProvider provider) {
        this.provider = provider;
    }

    @Override
    public Authenticator getApplicationAuthenticator(Context context, AuthenticationMode authMode) {
        return getResourceAuthenticator(context, authMode);
    }

    @Override
    public Authenticator getResourceAuthenticator(Context context, AuthenticationMode authMode) {
//        CacheManager cacheManager = null;
//        if (provider != null) {
//            cacheManager = this.provider.getCacheManager();
//        } else {
//            log.info("no cacheManager available in {}", this.getClass().getName());
//        }
        // https://github.com/qwerky/DataVault/blob/master/src/qwerky/tools/datavault/DataVault.java
        return new KeycloakOIDCAuthenticator(context, "SKYSAIL_SHIRO_DB_REALM", "thisHasToBecomeM".getBytes());
    }

    @Override
    public Principal getPrincipal(Request request) {
//        Object principal = SecurityUtils.getSubject().getPrincipal();
//        return new Principal() {
//            @Override
//            public String getName() {
//                return principal != null ? principal.toString() : "anonymous";
//            }
//        };
    	return null;
    }

    public void updatePassword(User user, String newPassword) {
//        validateUser(user);
//        updateConfigFile(user, newPassword);
//        // clearCache(user.getName());
//        SecurityUtils.getSecurityManager().logout(SecurityUtils.getSubject());
    }

    private void updateConfigFile(User user, String newPassword) {
//        String bCryptHash = PasswordUtils.createBCryptHash(newPassword);
//        String fileInstallDir = System.getProperty("felix.fileinstall.dir");
//        String configFileName = fileInstallDir + java.io.File.separator
//                + ShiroBasedUserManagementProvider.class.getName() + ".cfg";
//        Properties properties = new Properties();
//
//        File configFile = new File(configFileName);
//        try {
//            properties.load(new FileReader(configFileName));
//            properties.replace(user.getName() + ".password", bCryptHash);
//            properties.store(new FileWriter(configFile), "");
//        } catch (IOException e) {
//            log.error(e.getMessage(), e);
//        }
    }

    private void validateUser(User user) {
//        UsernamePasswordToken token = new UsernamePasswordToken(user.getName(), user.getSecret());
//        Subject subject = SecurityUtils.getSubject();
//        subject.login(token);
    }

    @Override
    public boolean isAuthenticated(Request request) {
        return false;//SecurityUtils.getSubject().isAuthenticated();
    }

    @Override
    public String getLoginPath() {
        Link httpBasicLoginPageLink = LinkUtils.fromResource(provider.getSkysailApplication().getApplication(),
                LoginResource.class);
        return httpBasicLoginPageLink.getUri();
    }

    @Override
    public String getLogoutPath() {
        return null;
    }

}
