package io.skysail.doc.swagger;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SwaggerContact {
	private final String name, url, email;
}
