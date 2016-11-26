package io.skysail.server.app.notes;

import javax.persistence.Id;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Note implements Identifiable {

	@Id
    private String id;

	private String uuid;

    @Field
    private String title;

    @Field
    private String content;


}