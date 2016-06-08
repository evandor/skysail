package io.skysail.server.um.httpbasic;

import java.nio.charset.Charset;
import java.security.Principal;
import java.util.Base64;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.ChallengeScheme;
import org.restlet.security.Authenticator;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.User;

import io.skysail.api.links.Link;
import io.skysail.api.um.AuthenticationService;
import io.skysail.server.app.SkysailRootApplication;
import io.skysail.server.um.httpbasic.app.HttpBasicLoginPage;
import io.skysail.server.um.httpbasic.app.HttpBasicUmApplication;
import io.skysail.server.utils.LinkUtils;

public class HttpBasicAuthenticationService implements AuthenticationService {

    private static final String ANONYMOUS = "anonymous";

    private HttpBasicUserManagementProvider userManagementProvider;

    public HttpBasicAuthenticationService(HttpBasicUserManagementProvider userManagementProvider) {
        this.userManagementProvider = userManagementProvider;
    }

    @Override
    public Authenticator getApplicationAuthenticator(Context context) {
        return new Authenticator(context) {
            @Override
            protected boolean authenticate(Request request, Response response) {
                return true;
            }
        };
    }

    @Override
    public Authenticator getResourceAuthenticator(Context context) {
        ChallengeAuthenticator challengeAuthenticator = new ChallengeAuthenticator(context, ChallengeScheme.HTTP_BASIC,
                "Skysail Realm");
        challengeAuthenticator.setVerifier(userManagementProvider.getVerifiers().iterator().next());
        return challengeAuthenticator;
    }

    @Override
    public Principal getPrincipal(Request request) {
        String authorization = request.getHeaders().getFirstValue("Authorization");
        if (authorization != null && authorization.startsWith("Basic")) {
            String base64Credentials = authorization.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials), Charset.forName("UTF-8"));
            String[] split = credentials.split(":", 2);
            return new User(split[0], split[1]);
        }
        return new User(ANONYMOUS);
    }

    @Override
    public boolean isAuthenticated(Request request) {
        return !getPrincipal(request).getName().equals(ANONYMOUS);
    }

    @Override
    public String getLoginPath() {
        try {
            Link httpBasicLoginPageLink = LinkUtils.fromResource(
                    userManagementProvider.getSkysailApplication().getApplication(), HttpBasicLoginPage.class);
            return httpBasicLoginPageLink.getUri();
        } catch (Exception e) { // NOSONAR
            return "/" + HttpBasicUmApplication.class.getSimpleName() + "/v1" + SkysailRootApplication.LOGIN_PATH;
        }
    }

    @Override
    public String getLogoutPath() {
        return null;
    }

}
