package io.skysail.server.app.sidebar;

import io.skysail.domain.Entity;
import lombok.Data;

@Data
public class Sidebar implements Entity {

	private String id;
	private String userId;
	private String sidebarName;
	private String uuid;
	
}
