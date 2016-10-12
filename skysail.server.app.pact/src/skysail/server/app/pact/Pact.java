package skysail.server.app.pact;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import lombok.Data;

@Data
public class Pact implements Identifiable{

    private String id;

    @Field
    private String name;
}
