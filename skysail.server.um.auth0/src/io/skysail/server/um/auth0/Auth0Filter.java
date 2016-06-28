//package io.skysail.server.um.auth0;
//
//import com.auth0.Auth0Exception;
//import com.auth0.jwt.JWTVerifier;
//import com.auth0.jwt.JWTVerifyException;
//import org.apache.commons.codec.binary.Base64;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//import java.security.SignatureException;
//
///**
// * Handles interception on a secured endpoint and does JWT Verification
// * Ensures for instance expired JWT tokens are not permitted access
// * Success and Failure navigation options are also configurable
// * <p>
// * This filter is ENABLED by setting the auth0.properties entry:
// * <p>
// * auth0.servletFilterEnabled: true
// * <p>
// * This option exists because a Spring App may wish to use classes
// * in this library but not have auto-configuration of the Filter enabled
// * <p>
// * A common scenario for this would be when wishing to leverage Spring Security
// * instead.
// */
//public class Auth0Filter implements Filter {
//
//    private static final Auth0Exception AUTH_ERROR = new Auth0Exception("Authentication Error");
//
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    private String onFailRedirectTo;
//
//    private JWTVerifier jwtVerifier;
//
//    private Auth0Config auth0Config;
//
//    public Auth0Filter(final Auth0Config auth0Config) {
//        this.auth0Config = auth0Config;
//    }
//
//    @Override
//    public void init(final FilterConfig filterConfig) throws ServletException {
//        onFailRedirectTo = filterConfig.getInitParameter("redirectOnAuthError");
//        if (onFailRedirectTo == null) {
//            throw new IllegalArgumentException("redirectOnAuthError cannot be null");
//        }
//        final String clientSecret = auth0Config.getClientSecret();
//        final String clientId = auth0Config.getClientId();
//        jwtVerifier = new JWTVerifier(new Base64(true).decodeBase64(clientSecret), clientId);
//    }
//
//    protected void onSuccess(final ServletRequest req, final ServletResponse res, final FilterChain next,
//                             final Auth0User auth0User) throws IOException, ServletException {
//        final Auth0RequestWrapper auth0RequestWrapper = new Auth0RequestWrapper((HttpServletRequest) req, auth0User);
//        next.doFilter(auth0RequestWrapper, res);
//    }
//
//    protected void onReject(final HttpServletResponse res) throws IOException, ServletException {
//        res.sendRedirect(onFailRedirectTo);
//    }
//
//    protected boolean tokensExist(final Tokens tokens) {
//        if (tokens == null) {
//            return false;
//        }
//        return tokens.getIdToken() != null && tokens.getAccessToken() != null;
//    }
//
//    @Override
//    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain next) throws IOException, ServletException {
//        final HttpServletRequest req = (HttpServletRequest) request;
//        final HttpServletResponse res = (HttpServletResponse) response;
//        final Tokens tokens = SessionUtils.getTokens(req);
//        if (!tokensExist(tokens)) {
//            onReject(res);
//            return;
//        }
//        try {
//            jwtVerifier.verify(tokens.getIdToken());
//            final Auth0User auth0User = SessionUtils.getAuth0User(req);
//            onSuccess(req, res, next, auth0User);
//        } catch (InvalidKeyException e) {
//            logger.debug("InvalidKeyException thrown while decoding JWT token "
//                    + e.getLocalizedMessage());
//            throw AUTH_ERROR;
//        } catch (NoSuchAlgorithmException e) {
//            logger.debug("NoSuchAlgorithmException thrown while decoding JWT token "
//                    + e.getLocalizedMessage());
//            throw AUTH_ERROR;
//        } catch (IllegalStateException e) {
//            logger.debug("IllegalStateException thrown while decoding JWT token "
//                    + e.getLocalizedMessage());
//            throw AUTH_ERROR;
//        } catch (SignatureException e) {
//            logger.debug("SignatureException thrown while decoding JWT token "
//                    + e.getLocalizedMessage());
//            throw AUTH_ERROR;
//        } catch (IOException e) {
//            logger.debug("IOException thrown while decoding JWT token "
//                    + e.getLocalizedMessage());
//            throw AUTH_ERROR;
//        } catch (JWTVerifyException e) {
//            logger.debug("JWTVerifyException thrown while decoding JWT token "
//                    + e.getLocalizedMessage());
//            throw AUTH_ERROR;
//        }
//    }
//
//    @Override
//    public void destroy() {
//    }
//}