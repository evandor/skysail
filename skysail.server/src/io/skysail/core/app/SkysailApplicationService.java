package io.skysail.core.app;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import io.skysail.core.model.SkysailApplicationModel;
import io.skysail.core.model.SkysailEntityModel;
import io.skysail.domain.Entity;
import io.skysail.domain.core.EntityModel;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.services.EntityApi;
import io.skysail.server.services.NoOpEntityApi;

@Component(immediate = true, service = SkysailApplicationService.class)
public class SkysailApplicationService {

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private ApplicationListProvider applicationListProvider;

    private List<EntityApi<?>> entityApis = new ArrayList<>();

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    public void addEntityApi(EntityApi<?> api) {
        entityApis.add(api);
    }

    public void removeEntityApi(EntityApi<?> api) {
        entityApis.remove(api);
    }

    public String pathForEntityResource(String className, String type) {
        for (SkysailApplication app : applicationListProvider.getApplications()) {
            Optional<EntityModel<? extends Entity>> entity = app.getApplicationModel().getEntityValues().stream()
                    .filter(e -> e.getId().equals(className))
                    .findFirst();
            if (entity.isPresent()) {
                SkysailEntityModel sem = (SkysailEntityModel) entity.get();
                Class postResourceClass = sem.getPostResourceClass();
                List<RouteBuilder> routeBuilders = app.getRouteBuildersForResource(postResourceClass);
                if (routeBuilders.size() > 0) {
                    return "/" + app.getName() + "/" + app.getApiVersion() + routeBuilders.get(0).getPathTemplate();
                }
            }
        }
        return "";
    }

    public EntityApi<?> getEntityApi(String entityName) {
        return entityApis.stream()
                .filter(api -> api.getEntityClass().getName().equals(entityName))
                .findFirst()
                .orElse(new NoOpEntityApi());
    }

    public SkysailApplicationModel getApplicationModel(String appName) {
        return applicationListProvider.getApplications().stream()
                .filter(a -> a.getName().equals(appName))
                .map(SkysailApplication::getApplicationModel)
                .findFirst().orElse(new SkysailApplicationModel("unknown"));
    }

    public SkysailEntityModel getEntityModel(String name) {
        return applicationListProvider.getApplications().stream()
                .map(a -> a.getApplicationModel())
                .map(model -> model.getEntityValues())
                .flatMap(mil -> mil.stream())
                .filter(entityModel -> entityModel.getId().equals(name))
                .map(SkysailEntityModel.class::cast)
                .findFirst().orElse(null);

    }

    public List<SkysailEntityModel> getEntityModels() {
        return applicationListProvider.getApplications().stream()
                .map(SkysailApplication::getApplicationModel)
                .map(SkysailApplicationModel::getEntityValues)
                .flatMap(Collection<EntityModel<? extends Entity>>::stream)
                .map(SkysailEntityModel.class::cast)
                .collect(Collectors.toList());

    }

}
