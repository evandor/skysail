package io.skysail.server.ext.weaving.methodinvocations;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.felix.service.command.CommandProcessor;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

import io.skysail.api.weaving.MethodInvocationsCountDataProvider;
import io.skysail.server.ext.weaving.methodinvocations.impl.InvocationCounterWeavingHook;


@Component(
    property = {
        CommandProcessor.COMMAND_SCOPE + ":String=skysail",
        CommandProcessor.COMMAND_FUNCTION + ":String=invocations",
    },
    service = Object.class,
    immediate = true
)
public class MethodInvocationCountCommand { // NOSONAR

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private MethodInvocationsCountDataProvider dataProvider;

    /**
     * Gogo command "invocations".
     */
    public void invocations(String param) {
        if (param.equals("clear")) {
            System.out.println("=== Clearing MethodInvocationCount Data ==="); // NOSONAR
            dataProvider.clearData();
            return;
        }
        System.out.println("=== method invocations sorted by count ==="); // NOSONAR
        if (InvocationCounterWeavingHook.getConfigData().size() == 0) {
            System.out.println("NO CONFIG FOUND!");
            System.out.println("Create a file called 'invocationcounter.cfg' in the config folder");
            System.out.println("with a line like");
            System.out.println("packagePattern = io.skysail.server");
        } else {
            System.out.println("config was set to: " + InvocationCounterWeavingHook.getConfigData());
        }
        System.out.println("==========================================");
        Map<String, Long> methodInvocations = dataProvider.getMethodInvocations();
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
