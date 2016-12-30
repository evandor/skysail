package io.skysail.server.app.plugins;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.felix.bundlerepository.Repository;
import org.apache.felix.bundlerepository.RepositoryAdmin;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import io.skysail.server.app.plugins.obr.ObrRepository;
import io.skysail.server.app.plugins.obr.ObrResource;

@Component(immediate = true, configurationPid = "obr", service = ObrService.class)
public class ObrService {

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
    private RepositoryAdmin repositoryAdmin;

    private List<String> urls = new ArrayList<>();

    @Activate
    public void activate(ObrConfig appConfig, ComponentContext componentContext) throws ConfigurationException {
        urls = Arrays.asList(appConfig.urls().split(","));
        urls.stream().forEach(url -> {
            try {
                this.repositoryAdmin.addRepository(url);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

    }

    public List<ObrRepository> getReposList() {
        Repository[] repos = repositoryAdmin.listRepositories();
        return Arrays.stream(repos).map(ObrRepository::new).collect(Collectors.toList());
    }

    public List<ObrResource> getResources(String id) {
        List<ObrResource> result = new ArrayList<>();
        getReposList().stream().filter(r -> r.getId().equals(id)).findFirst().ifPresent(repo -> {
            result.addAll(repo.getResources());
        });
        return result;
    }

}
