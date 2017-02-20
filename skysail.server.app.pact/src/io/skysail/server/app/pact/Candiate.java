package io.skysail.server.app.pact;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import lombok.Data;

@Data
public class Candiate implements Entity {

    private String id;
    
    @Field(description = "the candiate for the next turn")
    private String candidate = "Georgios";    

    @Field(description = "information about the next turns confirmation")
    private String lastConfirmationInfo = "pact's first time";
}
