package io.skysail.server.app.designer.layout;

import java.util.Collections;
import java.util.List;

import io.skysail.server.restlet.resources.ListServerResource;

public class BpmnLayoutResource extends ListServerResource<Layout> {

    @Override
    public List<Layout> getEntity() {
        return Collections.emptyList();
    }

}
