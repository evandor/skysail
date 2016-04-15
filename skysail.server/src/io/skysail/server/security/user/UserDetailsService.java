package io.skysail.server.security.user;

import org.restlet.security.User;

@FunctionalInterface 
public interface UserDetailsService {

	User loadUserByUsername(String username);
	
}
