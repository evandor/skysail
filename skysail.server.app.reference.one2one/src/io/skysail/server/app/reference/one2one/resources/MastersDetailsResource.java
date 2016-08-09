package io.skysail.server.app.reference.one2one.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.reference.one2one.One2OneApplication;
import io.skysail.server.app.reference.one2one.One2OneRepository;
import io.skysail.server.app.reference.one2one.Detail;
import io.skysail.server.app.reference.one2one.Master;
import io.skysail.server.db.DbClassName;
import io.skysail.server.restlet.resources.ListServerResource;


public class MastersDetailsResource extends ListServerResource<Detail> {

    private One2OneApplication app;
	private One2OneRepository repo;

    public MastersDetailsResource() {
        super(MastersDetailResource.class);
        addToContext(ResourceContextId.LINK_GLYPH, "list");
        addToContext(ResourceContextId.LINK_TITLE, "list courses for this timetable");
    }

    @Override
    protected void doInit() {
        app = (One2OneApplication) getApplication();
        repo = (One2OneRepository) app.getRepository(Master.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Detail> getEntity() {
        String sql = "select * from " + DbClassName.of(Detail.class) + " where #"+getAttribute("id")+" in IN(todos)";
        return (List<Detail>) repo.execute(Detail.class, sql);
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(MastersDetailsResource.class, PostMasterToNewDetailRelationResource.class);
    }
}