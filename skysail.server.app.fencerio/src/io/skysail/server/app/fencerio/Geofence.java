package io.skysail.server.app.fencerio;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import lombok.Data;

@Data
public class Geofence implements Entity {

    @Field
    private String id;

    @Field
    private String alias;

    @Field
    private String status;

    public Geofence(String id, String alias, String status) {
        this.id = id;
        this.alias = alias;
        this.status = status;
    }
}
