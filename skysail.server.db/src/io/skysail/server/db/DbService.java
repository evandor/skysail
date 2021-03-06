package io.skysail.server.db;

import java.util.List;
import java.util.Map;

import org.osgi.annotation.versioning.ProviderType;

import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.impl.ODocument;

import io.skysail.domain.Entity;
import io.skysail.domain.core.ApplicationModel;

@ProviderType
public interface DbService {

    // --- general ---
    Class<?> getRegisteredClass(String classname);
    void createUniqueIndex(Class<?> cls, String... columnNames);
    void createWithSuperClass(String superClass, String... vertices);
    void createEdges(String... vertices);
    void register(Class<?>... classes);
    void createProperty(String simpleName, String string, OType date);

    Object update(Entity entity, ApplicationModel applicationModel);

    // --- object api ---
    <T> List<T> findObjects(String sql);
    <T> List<T> findObjects(String sql, Map<String, Object> params);

    // --- graph api ---
    Object persist(Entity entity, ApplicationModel applicationModel);
    <T> List<T> findGraphs(Class<T> cls, String sql);
    <T> List<T> findGraphs(Class<T> cls, String sql, Map<String, Object> params);
    void deleteVertex(String id);
    <T> T findById2(Class<?> cls, String id);

    @Deprecated
    void delete(Class<?> cls, String id);
    void delete2(Class<?> cls, String id);
    Object executeUpdate(String sql, Map<String, Object> params);
    void update(ODocument doc);
    Object executeUpdateVertex(String sql, Map<String, Object> params);

    //void update(Map<String, Object> space);
    long getCount(String sql, Map<String, Object> params);

}
