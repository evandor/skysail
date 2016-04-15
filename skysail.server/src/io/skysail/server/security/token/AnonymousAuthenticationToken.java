package io.skysail.server.security.token;

import java.io.Serializable;
import java.security.Principal;
import java.util.Collection;

import io.skysail.server.security.Authority;
import lombok.Getter;

public class AnonymousAuthenticationToken extends AbstractAuthenticationToken implements Serializable {

	private static final long serialVersionUID = -487673418753975481L;

	@Getter
	private final transient Principal principal;

	public AnonymousAuthenticationToken(Principal principal, Collection<? extends Authority> authorities) {
		super(authorities);
		this.principal = principal;
	}

	@Override
	public Object getCredentials() {
		return "";
	}

}
