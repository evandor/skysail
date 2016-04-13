package io.skysail.server.security.token;

import java.security.Principal;
import java.util.Collections;

import lombok.Getter;

@Getter
public class UsernamePasswordAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 3243917161717739859L;

	private final transient Principal principal;
	
	private transient Object credentials;

	public UsernamePasswordAuthenticationToken(Principal principal, Object credentials) {
		super(Collections.emptyList());
		this.principal = principal;
		this.credentials = credentials;
	}
	
}
