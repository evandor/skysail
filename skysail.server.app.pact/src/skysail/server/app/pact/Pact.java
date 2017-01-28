package skysail.server.app.pact;

import java.util.Arrays;
import java.util.List;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;
import lombok.Data;

@Data
public class Pact implements Entity{

    private String id;

    @Field
    private String title;

    @Field(inputType = InputType.READONLY)
    private List<String> members = Arrays.asList("Carsten", "Georgios");

    @Field(inputType = InputType.READONLY)
    private String selectionStrategy = "Fixed Order, starting with Georgios";

    @Field(inputType = InputType.READONLY)
    private String confirmationStrategy = "On a confidential basis";
}
