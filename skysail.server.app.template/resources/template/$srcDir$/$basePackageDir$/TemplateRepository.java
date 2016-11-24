package $basePackageName$;


import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.db.DbClassName;
import io.skysail.server.db.DbService;
import io.skysail.server.db.GraphDbRepository;

public class TemplateRepository extends GraphDbRepository<Bookmark> implements DbRepository {

    public TemplateRepository (DbService dbService) {
        this.dbSerivce = dbSerivce;
        activate();
    }

    public void activate() {
        dbService.createWithSuperClass("V", DbClassName.of(Bookmark.class));
        dbService.register(Bookmark.class);
    }

}