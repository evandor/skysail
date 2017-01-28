package io.skysail.server.db.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import io.skysail.domain.Entity;
import io.skysail.domain.core.ApplicationModel;
import io.skysail.domain.core.EntityModel;
import io.skysail.domain.core.EntityRelation;
import io.skysail.server.domain.jvm.SkysailEntityModel;
import io.skysail.server.domain.jvm.SkysailFieldModel;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Persister {

    protected OrientGraph db;
    protected List<String> edges;
    protected EdgeHandler edgeHandler;

    private ObjectMapper mapper = new ObjectMapper();
    private ApplicationModel applicationModel;

    @Deprecated
    public Persister(OrientGraph db, String[] edges) {
        this.edges = Arrays.asList(edges);
        this.db = db;
        edgeHandler = new EdgeHandler((identifiable) -> execute(identifiable), db);
    }

    public Persister(OrientGraph db, ApplicationModel applicationModel) {
        this.applicationModel = applicationModel;
        this.edges = Collections.emptyList();
        this.db = db;
        edgeHandler = new EdgeHandler((identifiable) -> execute(identifiable), db);
    }

    public <T extends Entity> OrientVertex persist(T entity) {
        return runInTransaction(entity);
    }

    protected OrientVertex execute(@NonNull Entity entity) {
        OrientVertex vertex = determineVertex(entity);
        try {
            //Object removeRelationData = AnnotationUtils.removeRelationData(entity);
            @SuppressWarnings("unchecked")
            Map<String, Object> props = mapper.convertValue(entity, Map.class);
            props.keySet().stream().forEach(setPropertyOrCreateEdge(entity, vertex, props));
            return vertex;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Problem when persisting entity", e);
        }
    }


    protected Consumer<? super String> setPropertyOrCreateEdge(Entity entity, Vertex vertex,
            Map<String, Object> properties) {
        return key -> {
            if ("id".equals(key)) {
                return;
            }
            if (isProperty(entity, key)) {
                //System.out.println(entity.getClass() + ": " + key + ":= \""+properties.get(key)+"\"");
                if (properties.get(key) != null && !("class".equals(key))) {
                    setProperty(entity, vertex, key);
                }
            } else {
                System.out.println(entity.getClass() + ": " + key + ":= [EDGE]");
                try {
                    edgeHandler.handleEdges(entity, vertex, properties, key);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        };
    }

    protected OrientVertex determineVertex(Entity entity) {
        OrientVertex vertex;
        if (entity.getId() != null) {
            vertex = db.getVertex(entity.getId());
        } else {
            vertex = db.addVertex("class:" + entity.getClass().getName().replace(".", "_"));
        }
        return vertex;
    }

    private void setProperty(Object entity, Vertex vertex, String key) {
        try {
            if (isOfBooleanType(entity, key)) {
                setVertexProperty("is", entity, vertex, key);
            } else {
                setVertexProperty("get", entity, vertex, key);
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
    }

    private boolean isOfBooleanType(Object entity, String key) throws NoSuchFieldException {
        Class<?> type = entity.getClass().getDeclaredField(key).getType();
        return type.isAssignableFrom(boolean.class) || type.isAssignableFrom(Boolean.class);
    }

    protected void setVertexProperty(String prefix, Object entity, Vertex vertex, String key)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = entity.getClass().getMethod(getMethodName(prefix, key));
        Object result = method.invoke(entity);
        vertex.setProperty(key, result);
    }

    private static String getMethodName(String prefix, String key) {
        return new StringBuilder(prefix).append(key.substring(0, 1).toUpperCase()).append(key.substring(1)).toString();
    }

    private <T> OrientVertex runInTransaction(Entity entity) {
        try {
            OrientVertex result = execute(entity);
            db.commit();
            return result;
        } catch (Exception e) {
            db.rollback();
            throw new RuntimeException("Database Problem, rolled back transaction", e);
        } finally {
            db.shutdown();
        }
    }

    private boolean isProperty(Entity entity, String key) {
        if (applicationModel == null) {
            return !edges.contains(key);
        }
        EntityModel entityModel = applicationModel.getEntity(entity.getClass().getName());
        if (entityModel == null) {
            return true;
        }
        List<EntityRelation> relations = entityModel.getRelations();
        boolean relationExists = relations.stream()
                .map(EntityRelation::getName)
                .filter(n -> n.equals(key))
                .findFirst().isPresent();

        if (relationExists) {
        	return false;
        }
        if (entityModel instanceof SkysailEntityModel) {
            SkysailEntityModel sem = (SkysailEntityModel)entityModel;
            SkysailFieldModel field = (SkysailFieldModel) sem.getField(entity.getClass().getName() + "|" + key);
            if (field == null) {
            	log.warn("could not determine field for id '{}'", entity.getClass().getName() + "|" + key);
            	return true;
            }
            if (field.getEntityType() != null) {
                return false;
            }
        }
        return true;
    }



}
