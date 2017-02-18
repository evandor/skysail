package io.skysail.text.resources;

import org.restlet.data.Reference;

import io.skysail.server.restlet.resources.PutEntityServerResource;
import io.skysail.text.ContentApplication;
import io.skysail.text.domain.Content;

public class PutContentResource extends PutEntityServerResource<Content> {

    private ContentApplication app;
    private String msgKey;
    private String store;
    private String redirectTo;
    private String type;

    public PutContentResource() {
        app = (ContentApplication) getApplication();
    }

    @Override
    protected void doInit() {
        msgKey = getAttribute("key");
        store = getQueryValue("store");
        type = getQueryValue("type");
    }

    @Override
    public void updateEntity(Content entity) {
    	//entity.setMsgKey(msgKey);
    	redirectTo = entity.getRedirectTo();
        app.setMessage(entity);
    }

    // for stringtemplate
    public boolean isMessageResource() {
        return true;
    }

    @Override
    public String redirectBackTo() {
        return redirectTo;
    }

    public String redirectBackToLocal() {
        Reference referrerRef = getRequest().getReferrerRef();
        return referrerRef != null ? referrerRef.toString() : "/";
    }

    @Override
    public Content getEntity() {
        Content message = app.getMessage(msgKey, store, this);
        /*if (message.getPreferredRenderer() != null) {
            String rendererHint = message.getPreferredRenderer().getClass().getSimpleName();
            getContext().getAttributes().put(ResourceContextId.RENDERER_HINT.name(), rendererHint);
        }*/
        return message.withRedirect(redirectBackToLocal());
    }

    @Override
    public String redirectTo() {
        return redirectTo;
    }
}
