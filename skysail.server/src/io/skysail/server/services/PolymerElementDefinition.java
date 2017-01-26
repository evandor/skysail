package io.skysail.server.services;

import java.util.Map;

import org.restlet.Request;

import io.skysail.api.text.Translation;
import io.skysail.server.model.STFormFieldsWrapper.FormFieldAdapter;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class PolymerElementDefinition {

    private FormFieldAdapter formFieldAdapter;

    private Map<String, Translation> messages;

    private Request request;

    private String label;

    public abstract String render();

}
