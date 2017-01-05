package io.skysail.server.um.aws;

import java.nio.charset.Charset;
import java.security.Principal;
import java.util.Base64;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.ChallengeScheme;
import org.restlet.security.Authenticator;
import org.restlet.security.User;

import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentity;
import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentityClient;
import com.amazonaws.services.cognitoidentity.model.GetIdRequest;
import com.amazonaws.services.cognitoidentity.model.GetIdResult;

import io.skysail.api.links.Link;
import io.skysail.api.um.AuthenticationService;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.SkysailRootApplication;
import io.skysail.server.ext.aws.AwsConfigDescriptor;
import io.skysail.server.utils.LinkUtils;

public class AwsAuthenticationService implements AuthenticationService {

    private static final String ANONYMOUS = "anonymous";

    private AwsUserManagementProvider userManagementProvider;

    private String identityId;

    public AwsAuthenticationService(AwsUserManagementProvider userManagementProvider) {
        this.userManagementProvider = userManagementProvider;

        AmazonCognitoIdentity identityClient = new AmazonCognitoIdentityClient(new AnonymousAWSCredentials());
        GetIdRequest idRequest = new GetIdRequest();
        AwsConfigDescriptor awsConfig = userManagementProvider.getAwsConfiguration().getConfig();
        idRequest.setAccountId(awsConfig.awsAccountId());
        idRequest.setIdentityPoolId(awsConfig.awsCognitoIdentityPool());
        // If you are authenticating your users through an identity provider
        // then you can set the Map of tokens in the request
        // Map providerTokens = new HashMap();
        // providerTokens.put("graph.facebook.com", "facebook session key");
        // idRequest.setLogins(providerTokens);

        GetIdResult idResp = identityClient.getId(idRequest);

        identityId = idResp.getIdentityId();

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
        // ChallengeAuthenticator challengeAuthenticator = new
        // ChallengeAuthenticator(context, ChallengeScheme.HTTP_BASIC,
        // "Skysail Realm");
        // https://github.com/qwerky/DataVault/blob/master/src/qwerky/tools/datavault/DataVault.java
        AwsAuthenticator authenticator = new AwsAuthenticator(context, ChallengeScheme.HTTP_AWS_IAM,
                "SKYSAIL_SHIRO_DB_REALM");
        authenticator.setVerifier(new AwsVerifier(identityId));
        return authenticator;
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
            SkysailApplication application = userManagementProvider.getSkysailApplication().getApplication();
            Link loginLink = LinkUtils.fromResource(application,AwsLoginPage.class);
            return loginLink.getUri();
        } catch (Exception e) { // NOSONAR
            return "/" + AwsUmApplication.class.getSimpleName() + "/v1" + SkysailRootApplication.LOGIN_PATH;
        }
    }

    @Override
    public String getLogoutPath() {
        return null;
    }

}
