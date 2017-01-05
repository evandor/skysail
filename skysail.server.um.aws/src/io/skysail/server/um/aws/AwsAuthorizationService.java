package io.skysail.server.um.aws;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import org.restlet.data.ClientInfo;
import org.restlet.security.Enroler;
import org.restlet.security.MemoryRealm;
import org.restlet.security.Role;
import org.restlet.security.User;

import io.skysail.api.um.AuthorizationService;
import io.skysail.api.um.UserManagementRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AwsAuthorizationService implements AuthorizationService {

    private MemoryRealm realm = new MemoryRealm();

    private UserManagementRepository userManagementRepository;

    public AwsAuthorizationService(AwsUserManagementProvider provider) {
        realm.setEnroler(new Enroler() {
            @Override
            public void enrole(ClientInfo clientInfo) {
                User theUser = clientInfo.getUser();
                if (theUser == null) {
                    return;
                }
                Optional<User> user = userManagementRepository.getUser(theUser.getIdentifier());

                if (user.isPresent()) {
                    // Find all the inherited groups of this user
                    //Set<Group> userGroups = findGroups(user);

                    // Add roles specific to this user
                    for (Role role : userManagementRepository.getUsersRoles().get(user.get())) {
                        clientInfo.getRoles().add(role);
                    }

                    // Add roles common to group members
                   // Set<Role> groupRoles = findRoles(userGroups);

//                    for (Role role : groupRoles) {
//                        clientInfo.getRoles().add(role);
//                    }
                }
            }
        });
        //userManagementRepository = provider.getUserManagementRepository();
        //Map<String, User> allUsers = userManagementRepository.getUsers();
        //realm.getUsers().addAll(allUsers.values());
        //Map<User, Set<Role>> usersRoles = userManagementRepository.getUsersRoles();
        //allUsers.values().stream().forEach(addUsersRoles(usersRoles));
    }

    private Consumer<? super User> addUsersRoles(Map<User, Set<Role>> usersRoles) {
        return user -> {
            log.info("adding roles for user '{}'", user.getName());
            usersRoles.get(user).stream().forEach(addRoles(user));
        };
    }

    private Consumer<? super Role> addRoles(User user) {
        return role -> {
            log.info("adding role '{}'", role.getName());
            realm.map(user, role);
        };
    }

    @Override
    public Set<Role> getRolesFor(String username) {
        return userManagementRepository.getRoles();
    }

    @Override
    public Enroler getEnroler() {
        return realm.getEnroler();
    }

}
