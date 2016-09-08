package io.skysail.server.ext.weaving.methodinvocations;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.felix.service.command.CommandProcessor;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

import io.skysail.server.ext.weaving.methodinvocations.impl.MethodInvocationsCountDataProvider;

@Component(property = {
        CommandProcessor.COMMAND_SCOPE + ":String=skysail",
        CommandProcessor.COMMAND_FUNCTION + ":String=invocations",
}, service = Object.class)
public class MethodInvocationCountCommand { // NOSONAR

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private MethodInvocationsCountDataProvider data;

    /**
     * Gogo command "invocations".
     */
    public void invocations() {
        System.out.println("=== method invocations sorted by count ==="); // NOSONAR
        Map<String, Long> methodInvocations = data.getMethodInvocations();
        LinkedHashMap<String, Long> result = methodInvocations.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
        result.entrySet().stream()
            .forEach(e -> System.out.println(e.getKey() + ": #" + e.getValue())); // NOSONAR
    }

}
