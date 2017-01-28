package io.skysail.server.domain.jvm.test;

import java.util.Date;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import io.skysail.server.forms.PostView;
import lombok.*;

@Getter
@Setter
public class AThingWithSingleTabDefinition implements Entity {

    private String id;

    @Field
    @PostView(tab = "theTab")
    private Date created;

    @Field
    private String stringField;
    

}