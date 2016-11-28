//package io.skysail.domain.core;
//
//import java.text.DecimalFormat;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//import org.osgi.service.component.annotations.Component;
//import org.osgi.service.component.annotations.Reference;
//import org.osgi.service.component.annotations.ReferenceCardinality;
//import org.osgi.service.component.annotations.ReferencePolicy;
//
//import io.skysail.domain.core.repos.DbRepository;
//import io.skysail.domain.core.repos.InMemoryRepository;
//import io.skysail.domain.core.repos.Repository;
//import lombok.NonNull;
//import lombok.ToString;
//import lombok.extern.slf4j.Slf4j;
//
//@Component(immediate = true, service = Repositories.class)
//@Slf4j
//@ToString
//@Deprecated // repositories don't have to be exposed to all other bundles
//public class Repositories {
//
//    private volatile Map<String, Repository> repositories = new ConcurrentHashMap<>(); // NOSONAR
//
//    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
//    public synchronized void setDbRepository(@NonNull DbRepository repo) {
//        addToReposMap(repo, "new DB Repository");
//    }
//
//    public synchronized void unsetDbRepository(DbRepository repo) {
//        String identifier = repo.getRootEntity().getName();
//        repositories.remove(identifier);
//        log.info("(- Repository)  name '{}', count is {} now", identifier, formatSize(repositories.keySet()));
//    }
//
//    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
//    public synchronized void setInMemoryRepository(@NonNull InMemoryRepository repo) {
//        addToReposMap(repo, "new In-Memory Repository");
//    }
//
//    public synchronized void unsetInMemoryRepository(InMemoryRepository repo) {
//        String identifier = repo.getRootEntity().getName();
//        repositories.remove(identifier);
//        log.info("(- Repository)  name '{}', count is {} now", identifier, formatSize(repositories.keySet()));
//    }
//
//    public synchronized Map<String, Repository> getRepositories() {
//        return Collections.unmodifiableMap(repositories);
//    }
//
//    public Repository get(String identifier) {
//        return repositories.get(identifier);
//    }
//
//    public Collection<String> getRepositoryIdentifiers() {
//        return repositories.keySet();
//    }
//
//    private static String formatSize(@NonNull Collection<?> list) {
//        return new DecimalFormat("00").format(list.size());
//    }
//
//    private void addToReposMap(Repository repo, String msg) {
//        log.info("(+ Repository) about to add a " + msg);
//        if (repo.getRootEntity() == null) {
//            log.warn("trying to set repository without root entity");
//            return;
//        }
//        String identifier = repo.getRootEntity().getName();
//        if (identifier == null) {
//            throw new IllegalStateException("cannot set repository, name is missing");
//        }
//        repositories.put(identifier, repo);
//        log.info("(+ Repository) (#{}) with name '{}'", formatSize(repositories.keySet()),identifier);
//    }
//
//
//}
