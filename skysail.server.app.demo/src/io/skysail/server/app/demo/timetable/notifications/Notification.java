package io.skysail.server.app.demo.timetable.notifications;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.HtmlPolicy;
import io.skysail.domain.html.InputType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Notification implements Identifiable {

    @Id
    private String id;

    @Field(inputType = InputType.TEXT, htmlPolicy = HtmlPolicy.NO_HTML)
    @Size(min=2)
    @NotNull
    private String notification;


}
