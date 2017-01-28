package io.skysail.server.app.esclient.resources;

import java.util.List;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;
import io.skysail.server.app.esclient.domain.EsIndex;
import lombok.Getter;
import lombok.Setter;

@Getter
public class EsIndexListHolder implements Entity {

    @Setter
    private String id = "id";

    @Field
    private String apiUrl;

    @Field(inputType = InputType.TABLE)
    private List<EsIndex> indices;

    public EsIndexListHolder(String apiUrl, List<EsIndex> indices) {
        this.apiUrl = apiUrl;
        this.indices = indices;

    }
}
