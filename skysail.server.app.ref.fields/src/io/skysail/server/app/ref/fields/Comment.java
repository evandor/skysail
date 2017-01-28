package io.skysail.server.app.ref.fields;

import javax.persistence.Id;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Comment implements Entity {

    @Id
    private String id;

    @Field
    private String comment;


}
