package io.skysail.server.um.httpbasic;

import java.util.Optional;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Method;
import org.restlet.security.SecretVerifier;
import org.restlet.security.User;
import org.restlet.security.Verifier;

import io.skysail.server.security.SecurityContext;
import io.skysail.server.security.SecurityContextHolder;
import io.skysail.server.security.token.UsernamePasswordAuthenticationToken;
import lombok.Getter;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class FilebasedVerifier extends SecretVerifier implements Verifier {

    @Getter
    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY)
    private volatile io.skysail.api.um.UserManagementRepository userManagementRepository;

    @Override
    public int verify(String identifier, char[] secret) {
        SecurityContextHolder.clearContext();
        // identifier = identifier.replace("@", "&#64;"); // NOSONAR
        Optional<User> user = userManagementRepository.getUser(identifier);
        if (!user.isPresent()) {
            return RESULT_INVALID;
        }
        if (compare(secret, user.get().getSecret())) {
            SecurityContext securityContext = new SecurityContext(
                    new UsernamePasswordAuthenticationToken(user.get(), user.get().getSecret()));
            SecurityContextHolder.setContext(securityContext);
            return RESULT_VALID;
        } else {
            return RESULT_INVALID;
        }
    }

    @Override
    public int verify(Request request, Response response) {
        if (request.getMethod().equals(Method.OPTIONS)) {
            // let OPTION requests (preflight requests when using CORS and http
            // basic auth) pass
            return RESULT_VALID;
        }
        return super.verify(request, response);
    }

}
