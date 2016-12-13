package io.skysail.server.app.dashboard;

import io.skysail.domain.Identifiable;

public class Nothing implements Identifiable {

	@Override
	public String getId() {
		return null;
	}

	@Override
	public void setId(String id) {
	}

}
