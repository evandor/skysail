package io.skysail.server.security.user;

import org.osgi.annotation.versioning.ProviderType;
import org.restlet.security.Enroler;
import org.restlet.security.User;

@ProviderType
public interface UserDetailsService {

	User loadUserByUsername(String username);

	Enroler getEnroler();

}
