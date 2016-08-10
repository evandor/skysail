package io.skysail.server.restlet.resources;

import java.util.List;
import java.util.stream.Collectors;

import io.skysail.domain.core.EntityModel;
import io.skysail.server.domain.jvm.JavaEntityModel;

public class AggregateListResource extends ListServerResource<AggregateResource> {

    @Override
    public List<?> getEntity() {
        List<EntityModel<?>> rootEntities = getApplicationModel().getRootEntities();
        List<Class<?>> aggregateClasses = rootEntities.stream()
            .map(JavaEntityModel.class::cast)
            .filter(e -> e.isAggregate())
            .map(e -> e.getListResourceClass())
            .collect(Collectors.toList());
        return aggregateClasses.stream().map(c -> new AggregateResource(c)).collect(Collectors.toList());
    }

}
