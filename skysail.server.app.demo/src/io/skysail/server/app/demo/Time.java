package io.skysail.server.app.demo;

import java.util.Date;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Time implements Entity {

	private String id;
	
	@Field
	private String timeinmillis = ""+new Date().getTime();

}
