package $basePackageName$;

import java.io.Serializable;
import java.net.URL;

import javax.persistence.Id;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;
import io.skysail.server.codegen.annotations.GenerateResources;
import io.skysail.server.forms.ListView;
import io.skysail.server.forms.PostView;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@GenerateResources(application = "$basePackageName$.TemplateApplication")
public class AggregateRootEntity implements Identifiable {

    @Id
    private String id;

    @Field
    private String name;

}