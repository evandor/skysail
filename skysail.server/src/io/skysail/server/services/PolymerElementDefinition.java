package io.skysail.server.services;

import java.util.Map;

import io.skysail.api.text.Translation;
import io.skysail.server.model.STFormFieldsWrapper.FormFieldAdapter;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class PolymerElementDefinition {

    private FormFieldAdapter FormFieldAdapter;

    private Map<String, Translation> messages;

    private String label;

    public abstract String render();

}
