package io.skysail.server.app.pline.resources;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Mail implements Entity {

    private String id;

    private String from = "evandor@gmail.com";
    private String to = "mira.v.graef@gmail.com";
    private String subject = "subject";
    
    @Field
    private String name;
    
    @Field
    private String email;

    @Field
    private String message;
}
