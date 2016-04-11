package io.skysail.server.app.demo;

import java.io.Serializable;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Timetable implements Identifiable, Serializable {

	private static final long serialVersionUID = 5467749853173838976L;

	@Id
    private String id;

    @Field
    private String name;

}