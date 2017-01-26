package io.skysail.server.text.store.bundleresource.impl;

import org.restlet.data.Reference;

import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutMessageResource extends PutEntityServerResource<Message> {

    private I18nApplication app;
    private String msgKey;
    private String store;
    private String redirectTo;

    public PutMessageResource() {
        app = (I18nApplication) getApplication();
    }

    @Override
    protected void doInit() {
        msgKey = getAttribute("key");
        store = getQueryValue("store");
    }

    @Override
    public void updateEntity(Message entity) {
        /*if (!entity.getMsgKey().equals(msgKey)) {
            throw new IllegalStateException("wrong key");
        }*/
    	entity.setMsgKey(msgKey);
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
    public Message getEntity() {
        Message message = app.getMessage(msgKey, store, this);
        if (message.getPreferredRenderer() != null) {
            String rendererHint = message.getPreferredRenderer().getClass().getSimpleName();
            getContext().getAttributes().put(ResourceContextId.RENDERER_HINT.name(), rendererHint);
        }
        message.setRedirectTo(redirectBackToLocal());
        return message;
    }

    @Override
    public String redirectTo() {
        return redirectTo;
    }
}
