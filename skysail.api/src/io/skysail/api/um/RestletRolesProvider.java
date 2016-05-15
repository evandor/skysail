package io.skysail.api.um;

import java.util.List;

import org.osgi.annotation.versioning.ProviderType;
import org.restlet.security.Role;

@ProviderType
// TODO check relevance
public interface RestletRolesProvider {

    Role getRole(String username);

    List<Role> getRoles();

}
