package io.skysail.server.ext.weaving.methodinvocations.impl;

import java.util.Map;

public interface MethodInvocationsCountDataProvider {

    Map<String, Long> getMethodInvocations();
}
