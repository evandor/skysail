package io.skysail.server.security;

import java.util.Collections;

import org.restlet.security.User;

import io.skysail.server.security.token.AnonymousAuthenticationToken;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString 
@EqualsAndHashCode
public class SecurityContext {
	
	public static final String ANONYMOUS = "anonymous";

	private Authentication authentication;
	
	public SecurityContext() {
		User anonymous = new User(ANONYMOUS);
		this.authentication = new AnonymousAuthenticationToken(anonymous, Collections.emptyList());
	}

}
