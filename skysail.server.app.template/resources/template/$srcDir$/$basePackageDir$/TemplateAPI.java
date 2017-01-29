package $basePackageName$;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import io.skysail.domain.Entity;
import $basePackageName$.repositories.AggregateRootEntitysRepo;
import io.skysail.server.db.DbService;
import io.skysail.server.domain.jvm.SkysailApplicationService;
import io.skysail.server.services.EntityApi;

@Component(immediate = true)
public class TemplateAPI implements EntityApi<AggregateRootEntity> {

    @Reference
    private DbService dbService;

    @Reference
    private SkysailApplicationService appService;

    private AggregateRootEntityRepo repo;

    @Activate
    public void activate() {
        repo = new AggregateRootEntityRepo(dbService);
    }

    @Override
    public Class<? extends Entity> getEntityClass() {
        return AggregateRootEntity.class;
    }

    @Override
    public AggregateRootEntity create() {
        return new PostAggregateRootEntityResource().createEntityTemplate();
    }

    @Override
    public void persist(AggregateRootEntity entity) {
        repo.save(entity, appService.getApplicationModel(TemplateApplication.APP_NAME));
    }

}
