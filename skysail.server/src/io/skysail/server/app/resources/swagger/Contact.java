package io.skysail.server.app.resources.swagger;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Contact {
	private final String name, url, email;
}
