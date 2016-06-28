package io.skysail.server.um.auth0;

import java.io.Serializable;

public class Tokens implements Serializable {

	private static final long serialVersionUID = -3294153572080617347L;
	
	private String idToken;
	private String accessToken;
	private String type;
	private String refreshToken;

	public Tokens(final String idToken, final String accessToken, final String type, final String refreshToken) {
		this.idToken = idToken;
		this.accessToken = accessToken;
		this.type = type;
		this.refreshToken = refreshToken;
	}

	public String getIdToken() {
		return idToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getType() {
		return type;
	}

	public String getRefreshToken() {
		return refreshToken;
	}
}
