//package io.skysail.server.services;
//
//import java.util.List;
//
//import org.osgi.annotation.versioning.ProviderType;
//import org.restlet.Request;
//
//import io.skysail.domain.Entity;
//
///**
// * The Syncing interface provides a common scheme to sync entities between different systems.
// *
// * Syncing means that 1) an entity is serialized somehow (writeEntity), then 2.) made known to the syncing mechanism
// * (add), and, finally, 3.) committed (commit).
// *
// */
//@ProviderType
//public interface Syncing {
//
//    void writeEntity(Request request, Entity entity, String principal);
//
//    void writeEntity(String entityname, Entity entity, String principal);
//
//    void updateEntity(Request request, Entity entity, String principal);
//
//    void deleteEntity(Request request, String principal);
//
//    void add(String principal);
//
//    void commit(String principal);
//
//    <T> List<T> getEntities(Class<T> entityClass, String username, String string);
//
//    // TODO rethink
//    String getSyncInfo(String principal);
//
//}
