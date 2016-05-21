package io.skysail.api.um;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.osgi.annotation.versioning.ProviderType;
import org.restlet.security.Role;
import org.restlet.security.User;

/**
 * A UserManagmentRepository provides a skysail installation with information
 * about existing users, roles, and which roles are associated with which user.
 *
 */
@ProviderType
public interface UserManagementRepository {

    /**
     * @return the user for the given username.
     */
    Optional<User> getUser(String username);

    /**
     * @return a mapping between usernames and the actual user objects.
     */
    Map<String, User> getUsers();

    /**
     * @return all (existing) roles
     */
    Set<Role> getRoles();

    /**
     * @return the mapping between users and their associated roles.
     */
    public Map<User, Set<Role>> getUsersRoles();

}
