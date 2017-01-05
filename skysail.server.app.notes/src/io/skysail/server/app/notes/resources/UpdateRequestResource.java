package io.skysail.server.app.notes.resources;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.notes.NotesApplication;
import io.skysail.server.app.notes.domain.AwsNote;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class UpdateRequestResource extends PostEntityServerResource<AwsNote> {

    private NotesApplication app;

    public UpdateRequestResource() {
        addToContext(ResourceContextId.LINK_TITLE, "update from AWS");
    }

    @Override
    protected void doInit() {
        app = (NotesApplication) getApplication();
    }

    @Override
    public AwsNote createEntityTemplate() {
        return new AwsNote();
    }

    @Override
    public void addEntity(AwsNote entity) {
        app.getSyncService().updateFromAws();
    }

}
