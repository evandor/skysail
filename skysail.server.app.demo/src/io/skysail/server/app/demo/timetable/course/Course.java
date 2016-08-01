package io.skysail.server.app.demo.timetable.course;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.HtmlPolicy;
import io.skysail.domain.html.InputType;
import io.skysail.domain.html.Relation;
import io.skysail.server.app.demo.timetable.notifications.Notification;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="id")
public class Course implements Identifiable, Serializable {

	private static final long serialVersionUID = 7758474668442580679L;

	@Id
    private String id;

	@Field(inputType = InputType.TEXT, htmlPolicy = HtmlPolicy.NO_HTML)
	@Size(min=2)
	@NotNull
	private String coursename;

    @Field(inputType = InputType.TIME, htmlPolicy = HtmlPolicy.NO_HTML)
    private Date timefrom;


    @Field(inputType = InputType.TIME, htmlPolicy = HtmlPolicy.NO_HTML)
    private Date timeto;

    @Field(inputType = InputType.TEXT, htmlPolicy = HtmlPolicy.NO_HTML)
    private String room;

    @Field(inputType = InputType.TEXT, htmlPolicy = HtmlPolicy.NO_HTML)
    private String dayofweek;

    @Field(inputType = InputType.TEXT, htmlPolicy = HtmlPolicy.NO_HTML)
    private String trainer;

    @Field
    private boolean hasMessage = true;

    @Relation
    @JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
    private List<Notification> notifications = new ArrayList<>();

    public void addNotification(Notification notification) {
        notifications.add(notification);
    }




}