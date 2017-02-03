package io.skysail.server.app.prototypr.cucumber;

import java.util.HashMap;
import java.util.Map;

public class DomainAutomationApi implements AutomationApi {

    private Map<String, Object> stepDefinitions = new HashMap<>();

    @Override
    public void addStepDefClass(Object def) {
        this.stepDefinitions.put(def.getClass().getName(), def);
    }

    @Override
    public Object getStepDef(Class<?> cls) {
        return this.stepDefinitions.get(cls.getName());
    }

}
