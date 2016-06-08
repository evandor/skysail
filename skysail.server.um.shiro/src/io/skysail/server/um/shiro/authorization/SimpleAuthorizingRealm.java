package io.skysail.server.um.shiro.authorization;

import io.skysail.server.um.domain.SkysailUser;
import io.skysail.server.um.shiro.ShiroBasedUserManagementProvider;
import io.skysail.server.um.shiro.authentication.SkysailAuthenticationInfo;
import io.skysail.server.um.shiro.authentication.SkysailHashedCredentialsMatcher;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.MapCache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class SimpleAuthorizingRealm extends AuthorizingRealm {

    private ShiroBasedUserManagementProvider simpleUserManagementProvider;

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    public SimpleAuthorizingRealm(SkysailHashedCredentialsMatcher hashedCredetialsMatcher,
            ShiroBasedUserManagementProvider simpleUserManagementProvider) {

        

        this.simpleUserManagementProvider = simpleUserManagementProvider;
        //.setCacheManager(memoryConstrainedCacheManager);
        setCredentialsMatcher(hashedCredetialsMatcher);
        setAuthenticationCachingEnabled(true);
        Cache<Object, AuthenticationInfo> authenticationCache = new MapCache<>("credentialsCache",
                new ConcurrentHashMap<Object, AuthenticationInfo>());
        setAuthenticationCache(authenticationCache);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();
        if (username == null) {
            throw new IllegalArgumentException("Null usernames are not allowed by this realm.");
        }
        SkysailUser user = getUser(username);
        if (user != null) {

            // TODO lock account, credentials expired ...
            // if (user.isLocked()) {
            // throw new LockedAccountException("Account [" + account +
            // "] is locked.");
            // }
            // if (user.isCredentialsExpired()) {
            // String msg = "The credentials for account [" + account +
            // "] are expired";
            // throw new ExpiredCredentialsException(msg);
            // }
        }

        return new SkysailAuthenticationInfo(user);

    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = getUsername(principals);
        SkysailUser user = getUserByPrincipal(username);

        return new SkysailAuthorizationInfo(user);
    }

    protected String getUsername(PrincipalCollection principals) {
        return getAvailablePrincipal(principals).toString();
    }

    private SkysailUser getUser(String username) {
        return simpleUserManagementProvider.getByUsername(username);
    }

    private SkysailUser getUserByPrincipal(String principal) {
        return simpleUserManagementProvider.getByPrincipal(principal);
    }

//    private SkysailUser createUser(SkysailUser simpleUser) {
//        if (simpleUser == null) {
//            return null;
//        }
//
//        SkysailUser skysailUser = new SkysailUser(simpleUser.getUsername(), simpleUser.getPassword(),
//                simpleUser.getId());
//        skysailUser.setRoles(simpleUser.getRoles().stream().map(r -> {
//            return new SkysailRole(r);
//        }).collect(Collectors.toList()));
//        return skysailUser;
//    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

}
