package io.skysail.ext.oauth2.domain;

import lombok.Getter;

public enum GrantType {
    AUTHORIZATION_CODE("authorization_code");
    
    @Getter
    private String identifier;

    private GrantType(String identifier) {
        this.identifier = identifier;
    }
}

