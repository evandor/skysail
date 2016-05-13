package io.skysail.server.app.demo;

import java.io.Serializable;

import javax.persistence.Id;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import io.skysail.server.forms.PostView;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Bookmark implements Identifiable, Serializable {

	private static final long serialVersionUID = 5467749853173838976L;

	@Id
    private String id;

    @Field
    @PostView(tab = "default")
    private String name;
    
    @Field
    @PostView(tab = "notes")
    private String notes;

}