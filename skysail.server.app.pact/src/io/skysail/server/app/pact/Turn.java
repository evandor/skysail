package io.skysail.server.app.pact;

import io.skysail.domain.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Turn implements Entity {

	private String id;
	
	private String nextTurn;
	
	private String lastConfirmation;
	
}
