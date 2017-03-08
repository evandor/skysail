package io.skysail.app.instagram.mocked;

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
//@GenerateResources(application = "io.skysail.app.instagram.mocked.InstagramApiMockApplication")
public class InstagramUser implements Entity {

    @Id
    private String id;

    @Field
    private String username;

}