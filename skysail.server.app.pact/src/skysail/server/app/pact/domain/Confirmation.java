package skysail.server.app.pact.domain;

import java.util.Date;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;
import lombok.Data;

@Data
public class Confirmation implements Identifiable {

	private String id;
	
	@Field(inputType = InputType.READONLY)
	private Date created;
	
	@Field
	private String user;
}
