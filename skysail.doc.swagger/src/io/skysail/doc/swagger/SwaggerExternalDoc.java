package io.skysail.doc.swagger;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SwaggerExternalDoc {
	private final String description,url;
}
