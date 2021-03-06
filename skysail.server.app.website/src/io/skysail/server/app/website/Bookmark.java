package io.skysail.server.app.website;

import java.io.Serializable;
import java.net.URL;

import javax.persistence.Id;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;
import io.skysail.server.app.website.resources.BookmarkResource;
import io.skysail.server.forms.ListView;
import io.skysail.server.forms.PostView;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Bookmark implements Entity, Serializable {

	private static final long serialVersionUID = 5467749853173838976L;

	@Id
    private String id;

    @Field
    @PostView(tab = "default")
    private String name;

    @Field(inputType = InputType.URL)
    @PostView(tab = "default")
    @ListView(truncate = 20, link = BookmarkResource.class, prefix = "urlPrefix")
    @NonNull
    private URL url;

    private String favicon;

    private String metaDescription;

    @Field
    @PostView(tab = "notes")
    private String notes;


}