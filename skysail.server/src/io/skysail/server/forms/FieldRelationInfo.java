package io.skysail.server.forms;

import io.skysail.domain.html.FieldRelation;
import io.skysail.server.domain.jvm.SkysailApplicationService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class FieldRelationInfo {
	
	private String path;

	public FieldRelationInfo(FieldRelation annotation, SkysailApplicationService appService) {
		if (annotation == null) {
			return;
		}
		if (appService == null) {
			log.warn("SkysailApplicationService is null");
			return;
		}
		this.path = appService.pathForEntityResource(annotation.targetEntity(), "type");
	}

	
}
