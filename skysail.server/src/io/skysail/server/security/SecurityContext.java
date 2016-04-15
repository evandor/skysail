package io.skysail.server.security;

import java.util.Collections;

import org.restlet.security.User;

import io.skysail.server.security.token.AnonymousAuthenticationToken;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class SecurityContext {

	public static final String ANONYMOUS = "anonymous";

	private final Authentication authentication;

	public SecurityContext() {
		this.authentication = new AnonymousAuthenticationToken(new User(ANONYMOUS), Collections.emptyList());
	}

	public SecurityContext(@NonNull Authentication authentication) {
		this.authentication = authentication;
	}

}
