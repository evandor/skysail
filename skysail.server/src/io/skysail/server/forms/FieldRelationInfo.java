package io.skysail.server.forms;

import java.lang.reflect.Type;
import java.util.Collection;

import io.skysail.server.domain.jvm.SkysailApplicationService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class FieldRelationInfo {
	
	private String path;

	public FieldRelationInfo(FormField formField, SkysailApplicationService appService) {
		if (appService == null) {
			log.warn("SkysailApplicationService is null");
			return;
		}
		if (Collection.class.isAssignableFrom(formField.getType())) {
	        Type entityType = formField.getEntityType();
            this.path = appService.pathForEntityResource(entityType.getTypeName(), "type");
		}
	}

	
}
