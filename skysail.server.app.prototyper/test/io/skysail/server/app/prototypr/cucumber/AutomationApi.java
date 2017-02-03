package io.skysail.server.app.prototypr.cucumber;

public interface AutomationApi {

    void addStepDefClass(Object applicationsStepDefs);

    Object getStepDef(Class<?> cls);
}
