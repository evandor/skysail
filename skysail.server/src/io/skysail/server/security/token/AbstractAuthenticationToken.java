package io.skysail.server.security.token;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import io.skysail.server.security.Authentication;
import io.skysail.server.security.Authority;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
public abstract class AbstractAuthenticationToken implements Authentication {

	private static final long serialVersionUID = -629805896116456106L;

	@Setter
	private boolean authenticated = false;

	private final Collection<Authority> authorities;

	public AbstractAuthenticationToken(@NonNull Collection<? extends Authority> authorities) {
		if (authorities.stream().anyMatch(a -> a == null)) {
			throw new IllegalArgumentException("null element found in Authorities collection");
		}
		ArrayList<Authority> authList = new ArrayList<>();
		authorities.stream().forEach(a -> authList.add(a));
		this.authorities = Collections.unmodifiableList(authList);
	}

	@Override
	public String getName() {
		if (getPrincipal() instanceof Principal) {
			return ((Principal) getPrincipal()).getName();
		}
		return (this.getPrincipal() == null) ? "" : this.getPrincipal().toString();
	}

}
