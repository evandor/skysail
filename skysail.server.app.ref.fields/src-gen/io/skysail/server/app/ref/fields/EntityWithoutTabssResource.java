package io.skysail.server.app.ref.fields;

import java.util.List;
import io.skysail.api.links.Link;
//import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.server.ResourceContextId;

import javax.annotation.Generated;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class EntityWithoutTabssResource extends ListServerResource<EntityWithoutTabs> {

    private FieldsDemoApplication app;
    private EntityWithoutTabsRepo repository;

    public EntityWithoutTabssResource() {
        super(EntityWithoutTabsResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "List of EntityWithoutTabss");
    }

    protected void doInit() {
        super.doInit();
        app = (FieldsDemoApplication)getApplication();
        //repository = (EntityWithoutTabsRepo) app.getRepository(EntityWithoutTabs.class);
    }

    @Override
    public List<EntityWithoutTabs> getEntity() {
        return null;//repository.find(new Filter());
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks();
    }
}
