package io.skysail.server.um.shiro.authorization;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.Subject;
import org.restlet.data.ClientInfo;
import org.restlet.security.Enroler;
import org.restlet.security.Role;

import io.skysail.api.um.AuthorizationService;
import io.skysail.server.um.domain.SkysailRole;
import io.skysail.server.um.domain.SkysailUser;
import io.skysail.server.um.shiro.ShiroBasedUserManagementProvider;
import io.skysail.server.um.shiro.authentication.SkysailHashedCredentialsMatcher;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleAuthorizationService implements AuthorizationService, Enroler {

    private SimpleAuthorizingRealm authorizingRealm;
    private ShiroBasedUserManagementProvider userManagementProvider;

    public SimpleAuthorizationService(ShiroBasedUserManagementProvider simpleUserManagementProvider) {
        this.userManagementProvider = simpleUserManagementProvider;
        SkysailHashedCredentialsMatcher hashedCredetialsMatcher = new SkysailHashedCredentialsMatcher();

        hashedCredetialsMatcher.setCacheManager(simpleUserManagementProvider.getCacheManager());
        authorizingRealm = new SimpleAuthorizingRealm(hashedCredetialsMatcher,
                simpleUserManagementProvider);
    }

    @Override
    public Set<Role> getRolesFor(String principal) {
        if (principal == null) {
            return Collections.emptySet();
        }
        SkysailUser user = userManagementProvider.getByPrincipal(principal);
        Set<SkysailRole> roles = user.getRoles();
        if (roles == null) {
            log.warn("User '" + principal + "' could not be found in the Repository");
            return Collections.emptySet();
        }
        return roles.stream().map(this::getOrCreateRole).collect(Collectors.toSet());
    }

    @Override
    public void enrole(ClientInfo clientInfo) {
        Subject subject = SecurityUtils.getSubject();
        if (subject == null) {
            return;
        }
        // Find all the inherited groups of this user
        // Set<Group> userGroups = findGroups(user);

        // Add roles specific to this user
        Set<Role> userRoles = findRoles(subject);

        for (Role role : userRoles) {
            clientInfo.getRoles().add(role);
        }

        // Add roles common to group members
        // Set<Role> groupRoles = findRoles(userGroups);

        // for (Role role : groupRoles) {
        // clientInfo.getRoles().add(role);
        // }

    }

    private Set<Role> findRoles(Subject subject) {
        return getRolesFor((String) subject.getPrincipal());
    }

    public Realm getRealm() {
        return authorizingRealm;
    }

    private Role getOrCreateRole(SkysailRole r) {
    	throw new IllegalStateException();
//        RestletRolesProvider restletRolesProvider = userManagementProvider.getRestletRolesProvider();
//        Role role = restletRolesProvider.getRole(r.getName());
//        if (role != null) {
//            return role;
//        }
//        Role newRole = new Role(r.getName());
//        restletRolesProvider.getRoles().add(newRole);
//        return newRole;
    }

    @Override
    public Enroler getEnroler() {
        // TODO Auto-generated method stub
        return null;
    }

}
