package io.skysail.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.osgi.framework.Bundle;

import io.skysail.domain.Entity;
import lombok.ToString;

@ToString(of = {"bundle"})
public class BundleMessages implements Entity {

    private Bundle bundle;
    private ResourceBundle resourceBundle;
    private Map<String, Object> backingMap = new HashMap<>();
    private Map<String, String> messages;

    public BundleMessages(Bundle bundle, ResourceBundle resourceBundle) {
        this.bundle = bundle;
        this.resourceBundle = resourceBundle;
        messages = new HashMap<>();

        ArrayList<String> keys = Collections.list(resourceBundle.getKeys());
        keys.stream().forEach(key -> {
            messages.put(key, resourceBundle.getString(key));
        });
        backingMap.put("messages", messages);
    }

    public String getName() {
        return bundle.getSymbolicName();
    }

    public Map<String, String> getMessages() {
        return messages;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public String getBaseBundleName() {
        return resourceBundle.getBaseBundleName();
    }

    @Override
    public String getId() {
        return null;
    }

}
