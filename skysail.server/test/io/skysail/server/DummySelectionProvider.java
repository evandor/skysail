package io.skysail.server;

import java.util.HashMap;
import java.util.Map;

import io.skysail.domain.html.SelectionProvider;

public class DummySelectionProvider implements SelectionProvider {

    public static DummySelectionProvider getInstance() {
        return new DummySelectionProvider();
    }

    @Override
    public Map<String, String> getSelections() {
        Map<String, String> result = new HashMap<>();
        result.put("A", "1");
        return result;
    }

    @Override
    public void setConfiguration(Object osgiServicesProvider) {
    }

}