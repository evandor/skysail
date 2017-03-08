package io.skysail.app.instagram.mocked;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import io.skysail.core.app.SkysailApplicationService;
import io.skysail.domain.Entity;
import io.skysail.app.instagram.mocked.repositories.InstagramUserRepository;
import io.skysail.server.db.DbService;
import io.skysail.server.services.EntityApi;

@Component(immediate = true)
public class TemplateAPI implements EntityApi<InstagramUser> {

    @Reference
    private DbService dbService;

    @Reference
    private SkysailApplicationService appService;

    private InstagramUserRepository repo;

    @Activate
    public void activate() {
        repo = new InstagramUserRepository(dbService);
    }

    @Override
    public Class<? extends Entity> getEntityClass() {
        return InstagramUser.class;
    }

    @Override
    public InstagramUser create() {
        return null;//new PostAggregateRootEntityResource().createEntityTemplate();
    }

    @Override
    public void persist(InstagramUser entity) {
        repo.save(entity, appService.getApplicationModel(InstagramApiMockApplication.APP_NAME));
    }

}
