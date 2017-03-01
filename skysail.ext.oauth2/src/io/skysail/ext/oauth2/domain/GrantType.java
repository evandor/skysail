package io.skysail.ext.oauth2.domain;

public enum GrantType {
    AUTHORIZATION_CODE("authorization_code");
    
    private String identifier;

    private GrantType(String identifier) {
        this.identifier = identifier;
    }
    
    public String getIdentifier() {
		return identifier;
	}
}

