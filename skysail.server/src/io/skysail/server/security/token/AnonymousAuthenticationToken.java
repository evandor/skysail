package io.skysail.server.security.token;

import java.io.Serializable;
import java.util.Collection;

import io.skysail.server.security.Authority;
import lombok.Getter;

public class AnonymousAuthenticationToken extends AbstractAuthenticationToken implements Serializable {

	private static final long serialVersionUID = -487673418753975481L;

	@Getter
	private final Object principal;

	public AnonymousAuthenticationToken(Object principal, Collection<? extends Authority> authorities) {
		super(authorities);
		this.principal = principal;
	}

	@Override
	public Object getCredentials() {
		return "";
	}

}
