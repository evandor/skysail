package io.skysail.server.restlet.resources;

import java.util.List;
import java.util.stream.Collectors;

import io.skysail.core.model.SkysailEntityModel;
import io.skysail.core.utils.LinkUtils;
import io.skysail.domain.core.EntityModel;
import io.skysail.server.domain.jvm.ResourceType;

public class AggregatesResource extends ListServerResource<Aggregate> {

    public AggregatesResource() {
        super(AggregateResource.class);
    }

    @Override
    public List<?> getEntity() {
        List<EntityModel<?>> rootEntities = getApplicationModel().getRootEntities();
        @SuppressWarnings("rawtypes")
        List<Class> aggregateClasses = rootEntities.stream()
                .map(SkysailEntityModel.class::cast)
                .filter(e -> e.isAggregate())
                .map(e -> e.getAssociatedResource(ResourceType.LIST))
                .map(e -> e.getResourceClass())
                .collect(Collectors.toList());

        return aggregateClasses.stream()
                .map(c -> new Aggregate(c,LinkUtils.fromResource(getApplication(), c)))
                .collect(Collectors.toList());
    }

}
