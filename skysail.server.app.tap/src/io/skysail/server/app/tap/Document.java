package io.skysail.server.app.tap;

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
@GenerateResources(application = "io.skysail.server.app.tap.TapApplication", exclude = { io.skysail.server.codegen.ResourceType.POST })
public class Document implements Entity {

    @Id
    private String id;

    @Field
    private String filename;
    
    @Field
    private String importedFrom;

    @Field
    private java.util.Date importedAt;

    @Field
    private String version;
    
    

}