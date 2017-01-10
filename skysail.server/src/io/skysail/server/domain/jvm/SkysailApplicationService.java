package io.skysail.server.domain.jvm;

import java.util.List;
import java.util.Optional;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

import io.skysail.domain.Identifiable;
import io.skysail.domain.core.EntityModel;
import io.skysail.server.app.ApiVersion;
import io.skysail.server.app.ApplicationListProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.RouteBuilder;

@Component(immediate = true, service = SkysailApplicationService.class)
public class SkysailApplicationService {
	
	@Reference(cardinality = ReferenceCardinality.MANDATORY)
	private ApplicationListProvider applicationListProvider;

	public String pathForEntityResource(Class<? extends Identifiable> cls, String type) {
		for (SkysailApplication app : applicationListProvider.getApplications()) {
			Optional<EntityModel<? extends Identifiable>> entity = app.getApplicationModel().getEntityValues().stream()
				.filter(e -> e.getId().equals(cls.getName()))
				.findFirst();
			if (entity.isPresent()) {
				SkysailEntityModel sem = (SkysailEntityModel)entity.get();
				Class postResourceClass = sem.getPostResourceClass();
				List<RouteBuilder> routeBuilders = app.getRouteBuildersForResource(postResourceClass);
				if (routeBuilders.size() > 0) {
					return "/" + app.getName() + "/" + app.getApiVersion() + routeBuilders.get(0).getPathTemplate();
				}
			}
		}
		return "";
	}
	
}
