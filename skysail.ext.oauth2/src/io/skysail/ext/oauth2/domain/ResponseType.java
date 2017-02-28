package io.skysail.ext.oauth2.domain;

import lombok.Getter;

public enum ResponseType {
    CODE("code"), 
    TOKEN("token");
    
    @Getter
    private String identifier;

    private ResponseType(String identifier) {
        this.identifier = identifier;
    }
}

