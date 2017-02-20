package io.skysail.server.app.resources.swagger;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SwaggerExternalDoc {
	private final String description,url;
}
