package io.skysail.app.spotify;

import javax.persistence.Id;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import io.skysail.server.codegen.annotations.GenerateResources;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@GenerateResources(application = "io.skysail.app.spotify.SpotifyApplication")
public class AggregateRootEntity implements Entity {

    @Id
    private String id;

    @Field
    private String name;

}