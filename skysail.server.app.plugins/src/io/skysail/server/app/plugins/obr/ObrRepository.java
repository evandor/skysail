package io.skysail.server.app.plugins.obr;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.felix.bundlerepository.Repository;
import org.apache.felix.bundlerepository.Resource;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import lombok.Getter;

@Getter
public class ObrRepository implements Identifiable {

    @Field
    private String id;

    @Field
    private String uri;

    @Field
    private long lastModified;

    private List<ObrResource> resources;

    public ObrRepository() {
    }

    public ObrRepository(Repository repository) {
        this(repository, false);
    }

    public ObrRepository(Repository repository, boolean addResources) {
        id = repository.getName();
        uri = repository.getURI().toString();
        lastModified = repository.getLastModified();
        resources = map(repository.getResources());
    }

    private List<ObrResource> map(Resource[] resources) {
        return Arrays.stream(resources)
                .map(ObrResource::new)
                .sorted((r1,r2) -> r1.getSymbolicName().compareTo(r2.getSymbolicName()))
                .collect(Collectors.toList());
    }

    @Override
    public void setId(String id) {
    }

}
