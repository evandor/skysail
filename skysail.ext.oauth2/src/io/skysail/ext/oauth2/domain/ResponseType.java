package io.skysail.ext.oauth2.domain;

public enum ResponseType {
    CODE("code"), 
    TOKEN("token");
    
    private String identifier;

    private ResponseType(String identifier) {
        this.identifier = identifier;
    }
    
    public String getIdentifier() {
		return identifier;
	}
}

