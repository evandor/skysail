package io.skysail.server.app.timetables.timetable;

import java.io.Serializable;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.*;

import java.util.*;
import io.skysail.server.db.DbClassName;
import io.skysail.domain.Identifiable;
import io.skysail.domain.html.*;
import io.skysail.server.forms.*;
import lombok.Getter;
import lombok.Setter;
import io.skysail.server.app.timetables.timetable.*;
import io.skysail.server.app.timetables.timetable.resources.*;
import io.skysail.server.app.timetables.course.*;
import io.skysail.server.app.timetables.course.resources.*;


import org.apache.commons.lang3.StringUtils;

/**
 * generated from javafile.stg
 */
@SuppressWarnings("serial")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="id")
@Setter
@Getter
public class Timetable implements Identifiable, Serializable {

    @Id
    private String id;

    @Field(inputType = InputType.TEXT, htmlPolicy = HtmlPolicy.NO_HTML)
    @ListView(link = CoursesResourceGen.class)
    private String name;

    @Field(inputType = InputType.TEXT, htmlPolicy = HtmlPolicy.NO_HTML)
    @ListView(link = CoursesResourceGen.class)
    private String description;

    @Field(inputType = InputType.DATE, htmlPolicy = HtmlPolicy.NO_HTML)
    @ListView(link = CoursesResourceGen.class)
    private Date start;

    @Field(inputType = InputType.DATE, htmlPolicy = HtmlPolicy.NO_HTML)
    @ListView(link = CoursesResourceGen.class)
    private Date end;

    @Relation
    @JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
    private List<Course> courses = new ArrayList<>();

}