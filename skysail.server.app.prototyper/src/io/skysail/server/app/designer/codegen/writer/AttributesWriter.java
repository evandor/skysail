package io.skysail.server.app.designer.codegen.writer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.stream.Collectors;

import lombok.NonNull;

public class AttributesWriter {

    private String name;
    private Map<String, String> versions = new LinkedHashMap<>();

    public AttributesWriter(@NonNull String name) {
        this.name = name;
    }

    public AttributesWriter addVersion(String key, String version) {
        versions.put(key, version);
        return this;
    }

    public Attributes.Name getAttributesName() {
        return new Attributes.Name(name);
    }

    public String getAttributesValue() {
        return versions.keySet().stream()
            .map(key -> {
                if (versions.get(key) == null) {
                    return key;
                }
                return key + ";version=\"" + versions.get(key) + "\"";
            }).collect(Collectors.joining(","));
    }

}
