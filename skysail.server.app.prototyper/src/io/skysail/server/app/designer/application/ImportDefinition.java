package io.skysail.server.app.designer.application;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.HtmlPolicy;
import io.skysail.domain.html.InputType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImportDefinition implements Entity {

    private String id;

    @Field(inputType = InputType.TEXTAREA, htmlPolicy = HtmlPolicy.DEFAULT_HTML)
    private String yamlImport;
}
