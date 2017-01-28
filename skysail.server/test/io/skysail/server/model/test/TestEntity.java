package io.skysail.server.model.test;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;
import io.skysail.domain.html.Reference;
import io.skysail.server.forms.ListView;
import io.skysail.server.forms.PostView;
import io.skysail.server.forms.Visibility;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestEntity implements Identifiable {

    /**
     * This field is ignored by skysails ResourceModel
     */
    private String fieldWithoutAnnotation;

    @Field
    private String stringField;

    @Field(inputType = InputType.TEXTAREA)
    private String stringField_Textarea;

    @Field(inputType = InputType.RANGE)
    @Min(0)
    @Max(100)
    @ListView(hide=true)
    private Integer importance;

    @Reference() // , selectionProvider = ListSelectionProvider.class)
    @PostView(visibility = Visibility.SHOW_IF_NULL)
    @ListView(hide = true)
    //@ValidListId
    private String parent;

    @Override
    public String getId() {
        return null;
    }

}
