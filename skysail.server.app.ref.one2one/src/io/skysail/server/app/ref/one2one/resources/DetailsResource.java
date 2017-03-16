package io.skysail.server.app.ref.one2one.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.app.ref.one2one.Detail;
import io.skysail.server.app.ref.one2one.One2OneApplication;
import io.skysail.server.db.DbClassName;
import io.skysail.server.restlet.resources.ListServerResource;

public class DetailsResource extends ListServerResource<Detail> {

    private One2OneApplication app;

    public DetailsResource() {
        super(MasterResource.class);
    }

    @Override
    protected void doInit() {
        app = (One2OneApplication) getApplication();
    }

    @Override
    public List<Detail> getEntity() {
       //return repository.find(new Filter(getRequest()));
        String sql = "SELECT from " + DbClassName.of(Detail.class) + " WHERE #" + getAttribute("id") + " IN in('todos')";
        return null;//((SpaceRepository)app.getRepository(Space.class)).execute(Course.class, sql);
    }


    @Override
    public List<Link> getLinks() {
        // this will add a link to the "post new account" resource
        return super.getLinks(PostMasterResource.class);
    }

}
