package io.skysail.server.app.resources.swagger;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SwaggerContact {
	private final String name, url, email;
}
