package io.skysail.server.um.shiro.authentication;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.Principal;
import java.util.Properties;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.subject.Subject;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.security.Authenticator;
import org.restlet.security.User;

import io.skysail.api.links.Link;
import io.skysail.api.um.AlwaysAuthenticatedAuthenticator;
import io.skysail.api.um.AuthenticationMode;
import io.skysail.api.um.AuthenticationService;
import io.skysail.api.um.NeverAuthenticatedAuthenticator;
import io.skysail.core.utils.LinkUtils;
import io.skysail.server.um.shiro.ShiroBasedUserManagementProvider;
import io.skysail.server.um.shiro.app.LoginResource;
import io.skysail.server.utils.PasswordUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShiroAuthenticationService implements AuthenticationService {

    private ShiroBasedUserManagementProvider provider;

    public ShiroAuthenticationService(ShiroBasedUserManagementProvider provider) {
        this.provider = provider;
    }

    @Override
    public Authenticator getApplicationAuthenticator(Context context, AuthenticationMode authMode) {
        return getResourceAuthenticator(context, authMode);
    }

    @Override
    public Authenticator getResourceAuthenticator(Context context, AuthenticationMode authMode) {
    	
    	switch (authMode) {
		case DENY_ALL:
			return new NeverAuthenticatedAuthenticator(context);
		case PERMIT_ALL:
			return new AlwaysAuthenticatedAuthenticator(context);
		default:
			break;
		}
    	
        CacheManager cacheManager = null;
        if (provider != null) {
            cacheManager = this.provider.getCacheManager();
        } else {
            log.info("no cacheManager available in {}", this.getClass().getName());
        }
        boolean allowAnonymous = false;
        if (authMode.equals(AuthenticationMode.ANONYMOUS)) {
        	allowAnonymous = true;
        }
        // https://github.com/qwerky/DataVault/blob/master/src/qwerky/tools/datavault/DataVault.java
        return new SkysailCookieAuthenticator(context, "SKYSAIL_SHIRO_DB_REALM", "thisHasToBecomeM".getBytes(), allowAnonymous,
                cacheManager);
    }

    @Override
    public Principal getPrincipal(Request request) {
        Object principal = SecurityUtils.getSubject().getPrincipal();
        return new Principal() {
            @Override
            public String getName() {
                return principal != null ? principal.toString() : "anonymous";
            }
        };
    }

    public void updatePassword(User user, String newPassword) {
        validateUser(user);
        updateConfigFile(user, newPassword);
        // clearCache(user.getName());
        SecurityUtils.getSecurityManager().logout(SecurityUtils.getSubject());
    }

    private void updateConfigFile(User user, String newPassword) {
        String bCryptHash = PasswordUtils.createBCryptHash(newPassword);
        String fileInstallDir = System.getProperty("felix.fileinstall.dir");
        String configFileName = fileInstallDir + java.io.File.separator
                + ShiroBasedUserManagementProvider.class.getName() + ".cfg";
        Properties properties = new Properties();

        File configFile = new File(configFileName);
        try {
            properties.load(new FileReader(configFileName));
            properties.replace(user.getName() + ".password", bCryptHash);
            properties.store(new FileWriter(configFile), "");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void validateUser(User user) {
        UsernamePasswordToken token = new UsernamePasswordToken(user.getName(), user.getSecret());
        Subject subject = SecurityUtils.getSubject();
        subject.login(token);
    }

    @Override
    public boolean isAuthenticated(Request request) {
        return SecurityUtils.getSubject().isAuthenticated();
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
