package io.skysail.server.app.ref.one2many.noagg.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.ref.one2many.noagg.One2ManyNoAggApplication;
import io.skysail.server.app.ref.one2many.noagg.One2ManyNoAggRepository;
import io.skysail.server.app.ref.one2many.noagg.Contact;
import io.skysail.server.app.ref.one2many.noagg.Company;
import io.skysail.server.db.DbClassName;
import io.skysail.server.restlet.resources.ListServerResource;


public class CompanysContactsResource extends ListServerResource<Contact> {

    private One2ManyNoAggApplication app;
	private One2ManyNoAggRepository repo;

    public CompanysContactsResource() {
        super(CompanysContactResource.class);
        addToContext(ResourceContextId.LINK_GLYPH, "list");
        addToContext(ResourceContextId.LINK_TITLE, "list courses for this timetable");
    }

    @Override
    protected void doInit() {
        app = (One2ManyNoAggApplication) getApplication();
        repo = (One2ManyNoAggRepository) app.getRepository(Company.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Contact> getEntity() {
        String sql = "select * from " + DbClassName.of(Contact.class) + " where #"+getAttribute("id")+" in IN(todos)";
        return (List<Contact>) repo.execute(Contact.class, sql);
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(CompanysContactsResource.class, PostTodoListToNewTodoRelationResource.class);
    }
}