package io.skysail.server.app.tap;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import io.skysail.domain.Entity;
import io.skysail.server.app.tap.repositories.DocumentRepository;
import io.skysail.server.db.DbService;
import io.skysail.server.domain.jvm.SkysailApplicationService;
import io.skysail.server.services.EntityApi;

@Component(immediate = true)
public class TemplateAPI implements EntityApi<Document> {

    @Reference
    private DbService dbService;

    @Reference
    private SkysailApplicationService appService;

    private DocumentRepository repo;

    @Activate
    public void activate() {
        repo = new DocumentRepository(dbService);
    }

    @Override
    public Class<? extends Entity> getEntityClass() {
        return Document.class;
    }

    @Override
    public Document create() {
        return null;//new PostAggregateRootEntityResource().createEntityTemplate();
    }

    @Override
    public void persist(Document entity) {
        repo.save(entity, appService.getApplicationModel(TapApplication.APP_NAME));
    }

}
