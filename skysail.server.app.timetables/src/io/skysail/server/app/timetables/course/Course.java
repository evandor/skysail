package io.skysail.server.app.timetables.course;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.HtmlPolicy;
import io.skysail.domain.html.InputType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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


}