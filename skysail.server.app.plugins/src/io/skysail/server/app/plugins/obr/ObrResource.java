package io.skysail.server.app.plugins.obr;

import org.apache.felix.bundlerepository.Resource;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import lombok.Getter;

@Getter
public class ObrResource implements Entity {

    @Field
    private String searchFor;

    //@Field
    private String presentationName;

    @Field
    private String symbolicName;

    @Field
    private String version;

    @Field
    private Long size;

    @Field
    private String id;

    public ObrResource() {
    }

    public ObrResource(String searchFor) {
        this.searchFor = searchFor;
    }

    public ObrResource(Resource resource) {
        presentationName = resource.getPresentationName();
        symbolicName = resource.getSymbolicName();
        version = resource.getVersion().toString();
        size = resource.getSize();
        id = resource.getId();
    }

    public String getSearchFor() {
        return searchFor;
    }

    public void setSearchFor(String searchFor) {
        this.searchFor = searchFor;
    }

}
