package io.skysail.server.app.sidebar;

import io.skysail.domain.Identifiable;
import lombok.Data;

@Data
public class Sidebar implements Identifiable {

	private String id;
	private String userId;
	private String sidebarName;
	private String uuid;
	
}
