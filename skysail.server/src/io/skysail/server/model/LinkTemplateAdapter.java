package io.skysail.server.model;

import io.skysail.api.links.Link;
import lombok.Getter;

@Getter
public class LinkTemplateAdapter {

	private String title;
	private String uri;

	public LinkTemplateAdapter(Link link) {
	    title = link.getTitle();
	    uri = link.getUri();
	}
}
