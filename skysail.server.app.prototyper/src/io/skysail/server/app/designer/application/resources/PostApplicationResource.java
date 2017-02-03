package io.skysail.server.app.designer.application.resources;

import java.security.Principal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.forms.Tab;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostApplicationResource extends PostEntityServerResource<DbApplication> {

    public PostApplicationResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new DbApplication");
    }

    @Override
    public DbApplication createEntityTemplate() {
        return new DbApplication();
    }

    @Override
    public void addEntity(DbApplication entity) {
        DesignerApplication app = (DesignerApplication) getApplication();
        app.invalidateMenuCache();
        Principal principal = getApplication().getAuthenticationService().getPrincipal(getRequest());
        entity.setOwner(principal.getName());
        if (StringUtils.isBlank(entity.getProjectName())) {
            entity.setProjectName("skysail.server.app." + entity.getName().substring(0, 1).toLowerCase() + entity.getName().substring(1));
        }
        if (StringUtils.isBlank(entity.getPackageName())) {
            entity.setPackageName("io.skysail.server.app." + entity.getName().toLowerCase());
        }
        if (StringUtils.isBlank(entity.getPath())) {
            entity.setPath("../");
        }

//        String id = app.getRepository(DbApplication.class).save(entity, app.getApplicationModel()).toString();
//        entity.setId(id);

        OrientVertex save = app.getRepository().save(entity, app.getApplicationModel());
//        String id = DesignerRepository.add(entity, app.getApplicationModel()).getId().toString();
        entity.setId(save.getId().toString());
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(ApplicationsResource.class);
    }

    @Override
    public List<Tab> getTabs() {
        return super.getTabs(new Tab("newApp","new application",1), new Tab("details","details",2));
    }
}
