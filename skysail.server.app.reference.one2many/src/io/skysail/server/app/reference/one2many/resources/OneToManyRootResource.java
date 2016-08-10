package io.skysail.server.app.reference.one2many.resources;

import java.util.Arrays;
import java.util.List;

import io.skysail.server.restlet.resources.ListServerResource;

public class OneToManyRootResource extends ListServerResource<Id2> {

    public OneToManyRootResource() {
        super(OneToManyRootResourceItem.class);
    }

    @Override
    public List<?> getEntity() {
        return Arrays.asList(new Id2("TodoList"));
    }


}
