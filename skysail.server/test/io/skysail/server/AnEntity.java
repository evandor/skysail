package io.skysail.server;

import java.io.Serializable;

import io.skysail.domain.Entity;

public class AnEntity implements Entity, Serializable {

    private static final long serialVersionUID = 917647823437327405L;

    private String id;

    private String name;

    public AnEntity(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
