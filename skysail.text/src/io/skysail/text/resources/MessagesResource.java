package io.skysail.text.resources;

import java.util.Collections;
import java.util.List;

import io.skysail.core.app.SkysailApplication;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.text.BundleMessages;

public class MessagesResource extends ListServerResource<BundleMessages> {

    private SkysailApplication app;

    public MessagesResource() {
        app = getApplication();
    }

    @Override
    public List<BundleMessages> getEntity() {
        return Collections.emptyList();
    }


}
