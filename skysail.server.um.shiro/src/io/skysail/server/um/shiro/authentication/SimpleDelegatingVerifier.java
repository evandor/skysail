package io.skysail.server.um.shiro.authentication;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.restlet.security.SecretVerifier;

import lombok.extern.slf4j.Slf4j;

/**
 * A simple SecretVerifier calling the subjects login method and dealing with
 * the various outcomes.
 *
 */
@Slf4j
public class SimpleDelegatingVerifier extends SecretVerifier {

    @Override
    public int verify(String identifier, char[] secret) {
        identifier = identifier.replace("@", "&#64;");
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(identifier, new String(secret));
        log.debug("login event for user '{}'", identifier);
        try {
            currentUser.login(token);
            log.debug("login event for user '{}' successful", identifier);
            return RESULT_VALID;
        } catch (UnknownAccountException uae) {
            log.debug("UnknownAccountException '" + uae.getMessage() + "' when login in " + identifier);
        } catch (IncorrectCredentialsException ice) { // NOSONAR
            log.debug("IncorrectCredentialsException '" + ice.getMessage() + "' when login in " + identifier);
        } catch (LockedAccountException lae) {
            log.info("LockedAccountException '" + lae.getMessage() + "' when login in " + identifier, lae);
        } catch (AuthenticationException ae) {
            log.error("AuthenticationException '" + ae.getMessage() + "' when login in " + identifier, ae);
        }
        return RESULT_INVALID;
    }

}
