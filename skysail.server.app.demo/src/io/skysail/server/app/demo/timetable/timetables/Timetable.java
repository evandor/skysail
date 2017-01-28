package io.skysail.server.app.demo.timetable.timetables;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.HtmlPolicy;
import io.skysail.domain.html.InputType;
import io.skysail.domain.html.Relation;
import io.skysail.server.app.demo.timetable.course.Course;
import io.skysail.server.app.demo.timetable.course.resources.CoursesResource;
import io.skysail.server.forms.ListView;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="id")
@ToString
@Setter
@Getter
public class Timetable implements Entity, Serializable {

    @Id
    private String id;

    @Field(inputType = InputType.TEXT, htmlPolicy = HtmlPolicy.NO_HTML)
    @ListView(link = CoursesResource.class)
    private String name;

    @Field(inputType = InputType.TEXT, htmlPolicy = HtmlPolicy.NO_HTML)
    @ListView(link = CoursesResource.class)
    private String description;

    @Field(inputType = InputType.DATE, htmlPolicy = HtmlPolicy.NO_HTML)
    @ListView(link = CoursesResource.class)
    private Date start;

    @Field(inputType = InputType.DATE, htmlPolicy = HtmlPolicy.NO_HTML)
    @ListView(link = CoursesResource.class)
    private Date end;

    @Relation
    @JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
    private List<Course> courses = new ArrayList<>();

    public void updateCourse(Course course) {
        /*Optional<Course> existingCourse = courses.stream().filter(c -> c.getId().equals(course.getId())).findFirst();
        if (existingCourse.isPresent()) {
            courses.remove(existingCourse.get());
            courses.add(course);
        }*/
    }

    public Course getCourse(String id) {
        String targetId = "#" + id;
        return getCourses().stream()
                .filter(c -> c.getId().equals(targetId))
                .findFirst().orElse(null);
    }

}