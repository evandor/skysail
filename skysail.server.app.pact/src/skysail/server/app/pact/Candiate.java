package skysail.server.app.pact;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import lombok.Data;

@Data
public class Candiate implements Entity {

    private String id;
    
    @Field
    private String candidate = "Georgios";    

    @Field
    private String lastConfirmationInfo = "pact's first time";
}
