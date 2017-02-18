package io.skysail.server.restlet.resources.test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.skysail.domain.Entity;
import lombok.Data;

@Data
public class AnEntity  implements Entity {
    private String id, name;

    @JsonCreator
    public AnEntity(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name
            ) {
        this.id = id;
        this.name = name;
    }
}