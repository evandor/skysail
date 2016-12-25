package io.skysail.server.model;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.skysail.server.menus.MenuItem;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class STMenuItemWrapper {

	@Getter
	public class AdaptedMenuItem {

		private String label;
		private boolean selected;
		private String link;

		public AdaptedMenuItem(MenuItem m) {
			this.label = m.getName();
			this.selected = false;
			this.link = m.getLink();
		}
	}

	ObjectMapper mapper = new ObjectMapper();
	
	private List<AdaptedMenuItem> menuItems;

	public STMenuItemWrapper(List<MenuItem> menuItems) {
		this.menuItems = menuItems.stream().map(AdaptedMenuItem::new).collect(Collectors.toList());
	}

	public String getAsJson() { //
		try {
			return mapper.writeValueAsString(menuItems);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(),e);
			return "[]";
		}
	}
}
