package io.skysail.server.app.pact;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;
import lombok.Data;

@Data
public class Pact implements Entity{

    private String id;

    @Field(description = "the pacts title, a descriptive string, not necessarily unique", cssStyle = "background-color:#f9f9f9;padding-top:15px;border:1px solid gray;")
    private String title;

//    @Field(inputType = InputType.READONLY, description = "a list of participant names, fixed for now")
//    private List<String> members = Arrays.asList("Carsten", "Georgios");

    @Field(inputType = InputType.READONLY, description = "a string identifying a selection strategy, fixed for now")
    private String selectionStrategy = "Fixed Order, starting with Georgios";

    @Field(inputType = InputType.READONLY, description = "a string identifying a confirmation strategy, fixed for now")
    private String confirmationStrategy = "On a confidential basis";
}
