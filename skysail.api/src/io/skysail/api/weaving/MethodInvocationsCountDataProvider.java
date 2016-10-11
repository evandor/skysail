package io.skysail.api.weaving;

import java.util.Map;

public interface MethodInvocationsCountDataProvider {

    Map<String, Long> getMethodInvocations();

    void clearData();
}
