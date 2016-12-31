package $basePackageName$;

import java.io.Serializable;
import java.net.URL;

import javax.persistence.Id;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;
import $basePackageName$.resources.BookmarkResource;
import io.skysail.server.forms.ListView;
import io.skysail.server.forms.PostView;
import lombok.Getter;
import lombok.NonNull;
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
    @PostView(tab = "optional")
    private String name;

    @Field(inputType = InputType.URL)
    @PostView(tab = "optional")
    @ListView(truncate = 20, link = BookmarkResource.class, prefix = "urlPrefix")
    @NonNull
    private URL url;

    private String favicon;

    private String metaDescription;
    
    @Field
    @PostView(tab = "bookmark")
    private String bookmarkUrl;


}