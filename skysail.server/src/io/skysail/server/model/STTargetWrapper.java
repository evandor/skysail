package io.skysail.server.model;

import lombok.Getter;

import org.restlet.representation.Variant;

import io.skysail.core.app.SkysailApplication;

public class STTargetWrapper {

    @Getter
    private Variant target;

    public STTargetWrapper(Variant target) {
        this.target = target;
    }

    public boolean isTreeForm() {
        return SkysailApplication.SKYSAIL_TREE_FORM.getName().equals(target.getMediaType().getName());
    }

    @Override
    public String toString() {
        return target.toString();
    }
}
