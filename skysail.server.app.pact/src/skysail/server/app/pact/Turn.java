package skysail.server.app.pact;

import io.skysail.domain.Identifiable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Turn implements Identifiable {

	private String id;
	
	private String nextTurn;
	
	private String lastConfirmation;
	
}
