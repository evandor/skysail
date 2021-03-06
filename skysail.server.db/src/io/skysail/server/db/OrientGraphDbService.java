package io.skysail.server.db;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.orientechnologies.orient.core.command.OCommandRequest;
import com.orientechnologies.orient.core.command.traverse.OTraverse;
import com.orientechnologies.orient.core.db.ODatabase;
import com.orientechnologies.orient.core.db.ODatabaseDocumentInternal;
import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;
import com.orientechnologies.orient.core.db.OPartitionedDatabasePool;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.db.record.ridbag.ORidBag;
import com.orientechnologies.orient.core.exception.OSerializationException;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OClass.INDEX_TYPE;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.core.sql.OSQLEngine;
import com.orientechnologies.orient.core.sql.filter.OSQLPredicate;
import com.orientechnologies.orient.core.sql.functions.OSQLFunction;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.graph.sql.OGraphCommandExecutorSQLFactory;
import com.orientechnologies.orient.graph.sql.functions.OGraphFunctionFactory;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.orientechnologies.orient.object.metadata.schema.OSchemaProxyObject;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import io.skysail.api.metrics.MetricsCollector;
import io.skysail.api.metrics.NoOpMetricsCollector;
import io.skysail.api.metrics.TimerMetric;
import io.skysail.core.app.SkysailApplicationService;
import io.skysail.core.utils.SkysailBeanUtils;
import io.skysail.domain.Entity;
import io.skysail.domain.core.ApplicationModel;
import io.skysail.server.EventHelper;
import io.skysail.server.db.impl.AbstractOrientDbService;
import io.skysail.server.db.impl.Persister;
import io.skysail.server.db.impl.Updater;
import io.skysail.server.events.EventHandler;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true)
@Slf4j
public class OrientGraphDbService extends AbstractOrientDbService implements DbService {

    private OrientGraphFactory graphDbFactory;

    private Map<String, Entity> beanCache = new HashMap<>();

    private AtomicLong al = new AtomicLong();

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private MetricsCollector metricsCollector = new NoOpMetricsCollector();

    @Reference
    private SkysailApplicationService appService;

	private OObjectDatabaseTx db;

    @Activate
    public void activate() {
        log.debug("activating {}", this.getClass().getName());
        //http://stackoverflow.com/questions/30291359/orient-db-unable-to-open-any-kind-of-graph
        if (getDbUrl().startsWith("memory:")) {
            // https://github.com/orientechnologies/orientdb/issues/5105
            // com.orientechnologies.common.util.OClassLoaderHelper
            new OGraphCommandExecutorSQLFactory();
            new OrientGraphNoTx (getDbUrl());
        }

        graphDbFactory = new OrientGraphFactory(getDbUrl(), getDbUsername(), getDbPassword()).setupPool(1, 10);
        //graphDbFactory.getTx().shutdown();
        //graphDbFactory.setupPool(1, 10);

        OGraphFunctionFactory graphFunctions = new OGraphFunctionFactory();
        Set<String> names = graphFunctions.getFunctionNames();

        for (String name : names) {
            OSQLEngine.getInstance().registerFunction(name, graphFunctions.createFunction(name));
            OSQLFunction function = OSQLEngine.getInstance().getFunction(name);
            if (function != null) {
                log.debug("ODB graph function [{}] is registered: [{}]", name, function.getSyntax());
            } else {
                log.warn("ODB graph function [{}] NOT registered!!!", name);
            }
        }
    }

    @Deactivate
    public void deactivate(ComponentContext context) { // NO_UCD
        log.debug("activating {}", this.getClass().getName());
        stopDb();
        graphDbFactory = null;
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY, target = "(name=default)")
    public void setDbConfigurationProvider(DbConfigurationProvider provider) {
        updated(provider);
    }

    public void unsetDbConfigurationProvider(DbConfigurationProvider provider) {
    }

    @Override
    public OrientVertex persist(Entity entity, ApplicationModel applicationModel) {
        return new Persister(getGraphDb(), applicationModel).persist(entity);
    }

    @Override
    public OrientVertex update(Entity entity, ApplicationModel applicationModel) {
        return new Updater(getGraphDb(), applicationModel).persist(entity);
    }


    @Override
    public <T> List<T> findObjects(String sql) {
        return findObjects(sql, new HashMap<>());
    }

    @Override
    public <T> List<T> findGraphs(Class<T> cls, String sql) {
        return findGraphs(cls, sql, new HashMap<>());
    }

    @Override
    @Deprecated
    public <T> List<T> findObjects(String sql, Map<String, Object> params) {
        OObjectDatabaseTx objectDb = getObjectDb();

        try {
        	List<T> query = objectDb.query(new OSQLSynchQuery<ODocument>(sql), params);

        	List<T> detachedEntities = new ArrayList<>();
        	for (T t : query) {
        		T newOne = objectDb.detachAll(t, true);
        		detachedEntities.add(newOne);
        	}
        	return detachedEntities;
        } catch (OSerializationException ose) {
        	log.error(ose.getMessage());
        	log.info("Registered entities are:");
        	log.info("------------------------");
        	objectDb.getEntityManager().getRegisteredEntities().stream().forEach(e -> log.info(e.getName()));
        	log.info("");
        	throw ose;
        }
    }

    @Override
    public <T> List<T> findGraphs(Class<T> cls, String sql, Map<String, Object> params) {
        TimerMetric timer = metricsCollector.timerFor(this.getClass(), "findGraphs");
        OrientGraph graph = getGraphDb();
        OCommandRequest oCommand = new OCommandSQL(sql);
        Iterable<OrientVertex> execute = graph.command(oCommand).execute(params);

        List<T> result = new ArrayList<>();
        Iterator<OrientVertex> iterator = execute.iterator();
        beanCache .clear();
        while (iterator.hasNext()) {
            OrientVertex next = iterator.next();
            // OrientElement detached = next.detach();
            // detachedEntities.add((T) detached);
            result.add(documentToBean(next.getRecord(), cls));
        }
        timer.stop();
        return result;
    }

    @Override
    public <T> T findById2(Class<?> cls, String id) {
        ODatabaseDocumentInternal db = getObjectDb().getUnderlying();
        ODatabaseRecordThreadLocal.INSTANCE.set(db);
        OTraverse predicate = new OTraverse().target(new ORecordId(id)).fields("out", "int").limit(1)
                .predicate(new OSQLPredicate("$depth <= 3"));
        ODocument document = (ODocument) predicate.iterator().next();
        beanCache.clear();
        return document != null ? documentToBean(document, cls) : null;
    }

    @SuppressWarnings("unchecked")
    private <T extends Entity> T documentToBean(ODocument document, Class<?> beanType) {
        try {
            //System.out.println(al.incrementAndGet() + ": " + document.getIdentity().getIdentity().toString());
            T bean = (T) beanType.newInstance();
            populateProperties(document.toMap(), bean, new SkysailBeanUtils(bean, Locale.getDefault(), appService));
            beanCache.put(bean.getId(), bean);
            populateOutgoingEdges(document, bean);
            //populateIngoingEdge(document, bean);
            return bean;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    private <T extends Entity> void populateOutgoingEdges(ODocument document, T bean) {
        List<String> outFields = getOutgoingFieldNames(document);
        outFields.forEach(edgeName -> {
            ORidBag field = document.field(edgeName);
            field.setAutoConvertToRecord(true);
            field.convertLinks2Records();

            ORidBag edgeIdBag = document.field(edgeName);
            Iterator<OIdentifiable> iterator = edgeIdBag.iterator();
            List<Entity> identifiables = new ArrayList<>();
            while (iterator.hasNext()) {
                ODocument edge = (ODocument) iterator.next();
                if (edge == null) {
                    continue;
                }
                ODocument inDocumentFromEdge = edge.field("in");
                String targetClassName = inDocumentFromEdge.getClassName().substring(
                        inDocumentFromEdge.getClassName().lastIndexOf("_") + 1);
                Class<?> targetClass = getObjectDb().getEntityManager().getEntityClass(targetClassName);
                Entity identifiable = beanCache.get(inDocumentFromEdge.getIdentity().toString());
                if (identifiable != null) {
                    identifiables.add(identifiable);
                } else {
                    identifiables.add(documentToBean(inDocumentFromEdge, targetClass));
                }
            }
            String fieldName = edgeName.replace("out_", "");
            String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            try {
                bean.getClass().getMethod(setterName, List.class).invoke(bean, identifiables);
            } catch (Exception e) {
                log.error(e.getMessage(), e);

            }
        });
    }

    /**
     * assuming for now we have only one ingoing edge (e.g. some kind of parent object)
     */
    private <T extends Entity> void populateIngoingEdge(ODocument document, T bean) {
        List<String> inFields = getIngoingFieldNames(document);
        inFields.forEach(edgeName -> {
            ORidBag field = document.field(edgeName);
            field.setAutoConvertToRecord(true);
            field.convertLinks2Records();

            ORidBag edgeIdBag = document.field(edgeName);
            Iterator<OIdentifiable> iterator = edgeIdBag.iterator();
            Entity identifiable = null;// = new ArrayList<>();
            String targetClassName = null;
            Class<?> targetClass = null;
            if (!iterator.hasNext()) {
                return;
            }
            ODocument edge = (ODocument) iterator.next();
            ODocument outDocumentFromEdge = edge.field("out");
            targetClassName = outDocumentFromEdge.getClassName()
                    .substring(outDocumentFromEdge.getClassName().lastIndexOf("_") + 1);
            targetClass = getObjectDb().getEntityManager().getEntityClass(targetClassName);
            identifiable = beanCache.get(outDocumentFromEdge.getIdentity().toString());
            if (identifiable == null) {
                identifiable = documentToBean(outDocumentFromEdge, targetClass);
            }

            String setterName = "set" + targetClassName.substring(0, 1).toUpperCase() + targetClassName.substring(1);
            try {
                bean.getClass().getMethod(setterName, targetClass).invoke(bean, identifiable);
            } catch (Exception e) { // NOSONAR
                log.debug(e.getMessage());
            }
        });
    }

    private List<String> getOutgoingFieldNames(ODocument document) {
        return Arrays.stream(document.fieldNames()).filter(fieldname -> fieldname.startsWith("out_"))
                .collect(Collectors.toList());
    }

    private List<String> getIngoingFieldNames(ODocument document) {
        return Arrays.stream(document.fieldNames()).filter(fieldname -> fieldname.startsWith("in_"))
                .collect(Collectors.toList());
    }

    private <T extends Entity> void populateProperties(Map<String, Object> entityMap, T bean,
            SkysailBeanUtils beanUtilsBean) throws IllegalAccessException, InvocationTargetException {
        beanUtilsBean.populate(bean, entityMap);
        if (entityMap.get("@rid") != null && bean.getId() == null) {
            Field field;
            try {
                field = bean.getClass().getDeclaredField("id");
                field.setAccessible(true);
                field.set(bean, entityMap.get("@rid").toString());
            } catch (NoSuchFieldException | SecurityException e) {
                log.error(e.getMessage(),e);
            }
        }
    }

    @Override
    public long getCount(String sql, Map<String, Object> params) {
        OObjectDatabaseTx objectDb = getObjectDb();
        List<ODocument> query = objectDb.query(new OSQLSynchQuery<ODocument>(sql), params);
        Object field = query.get(0).field("count");
        if (field instanceof Long) {
            return (Long) field;
        }
        if (field instanceof Integer) {
            return new Long((Integer) field);
        }
        return 0;
    }

    @Override
    public Object executeUpdate(String sql, Map<String, Object> params) {
        OObjectDatabaseTx objectDb = getObjectDb();
        Object executed = objectDb.command(new OCommandSQL(sql)).execute(params);
        objectDb.commit();
        return executed;
    }

    @Override
    public Object executeUpdateVertex(String sql, Map<String, Object> params) {
        OrientGraph graphDb = getGraphDb();
        Object executed = graphDb.command(new OCommandSQL(sql)).execute(params);
        graphDb.commit();
        return executed;
    }

    @Override
    @Deprecated
    public void delete(Class<?> cls, String id) {
        OObjectDatabaseTx objectDb = getObjectDb();
        objectDb.getEntityManager().registerEntityClass(cls);
        ORecordId recordId = new ORecordId(id);
        Object loaded = objectDb.load(recordId);
        objectDb.delete(recordId);
    }

    @Override
    public void delete2(Class<?> cls, String id) {
        OrientGraph graphDb = getGraphDb();
        ORecordId recordId = new ORecordId(id);
        OrientVertex loaded = graphDb.getVertex(recordId);
        if (loaded.getLabel().equals(DbClassName.of(cls))) {
            String sql = "DELETE VERTEX "+DbClassName.of(cls)+" WHERE @rid=" + (id.contains("#") ? id : "#" + id);
            graphDb.command(new OCommandSQL(sql)).execute();
            graphDb.commit();
        } else {
            // TODO
        }
    }

    @Override
    public void deleteVertex(String id) {
        OrientGraph db = getGraphDb();
        ORecordId recordId = new ORecordId(id);
        OrientVertex vertex = db.getVertex(recordId);
        db.removeVertex(vertex);
    }

    @Override
    public synchronized void startDb() {
        if (started) {
            return;
        }
        try {
            log.debug("about to start db");
            //OGlobalConfiguration.STORAGE_KEEP_OPEN.setValue( false );

            createDbIfNeeded();

            OPartitionedDatabasePool opDatabasePool = new OPartitionedDatabasePool(getDbUrl(), getDbUsername(),
                    getDbPassword());
            ODatabaseDocumentTx oDatabaseDocumentTx = opDatabasePool.acquire();
            db = new OObjectDatabaseTx(oDatabaseDocumentTx);

            log.debug("setting lazy loading to false");
            db.setLazyLoading(false);
            started = true;
            if (getDbUrl().startsWith("memory:")) {
                // remark: this might be called without an eventhandler already
                // listening and so the event might be lost
                EventHandler.sendEvent(EventHelper.GUI_MSG,
                        "In-Memory database is being used, all data will be lost when application is shut down",
                        "warning");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    protected synchronized void stopDb() {
        started = false;
        graphDbFactory.close();
        if (getDbUrl().startsWith("memory")) {
        	try {
        		db.drop();
        	} catch (Exception e) {
        		log.error(e.getMessage(),e);
        	}
        }
    }

    private void createDbIfNeeded() {
        String dbUrl = getDbUrl();
        if (dbUrl.startsWith("remote")) {
            log.debug("registering remote engine");
            //Orient.instance().getEngines().a
            //Orient.instance().registerEngine(new OEngineRemote());
        } else if (dbUrl.startsWith("plocal")) {

            OrientGraph graph = new OrientGraph(dbUrl, getDbUsername(), getDbPassword());
            try {
                log.debug("testing graph factory connection");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                graph.shutdown();
            }
        } else if (dbUrl.startsWith("memory:")) {
            OObjectDatabaseTx databaseTx = new OObjectDatabaseTx(dbUrl);
            if (!databaseTx.exists()) {
    			ODatabase<?> create = databaseTx.create();
                log.debug("created new in-memory database {}", create.toString());
            }

            final OrientGraphFactory factory = new OrientGraphFactory(dbUrl, getDbUsername(), getDbPassword())
                    .setupPool(1, 10);
            try {
                log.debug("testing graph factory connection");
                factory.getTx();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                factory.close();
            }
        }

    }

    @Override
    protected void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                stopDb();
            }
        });
    }

    @Override
    public void createWithSuperClass(String superClass, String... vertices) {
        OObjectDatabaseTx objectDb = getObjectDb();
        try {
            Arrays.stream(vertices).forEach(v -> {
                if (objectDb.getMetadata().getSchema().getClass(v) == null) {
                    OClass vertexClass = objectDb.getMetadata().getSchema().getClass(superClass);
                    objectDb.getMetadata().getSchema().createClass(v).setSuperClass(vertexClass);
                }
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void createEdges(String... edges) {
        OObjectDatabaseTx objectDb = getObjectDb();
        try {
            Arrays.stream(edges).forEach(edge -> {
                if (objectDb.getMetadata().getSchema().getClass(edge) == null) {
                    OClass edgeClass = objectDb.getMetadata().getSchema().getClass("E");
                    objectDb.getMetadata().getSchema().createClass(edge).setSuperClass(edgeClass);
                }
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void register(Class<?>... entities) {
        OObjectDatabaseTx db = getObjectDb();
        try {
            Arrays.stream(entities).forEach(entity -> {
                log.debug("registering class '{}' @ orientDB", entity);
                db.getEntityManager().registerEntityClass(entity);
            });
        } finally {
            db.close();
        }
    }

    @Override
    public Class<?> getRegisteredClass(String classname) {
        OObjectDatabaseTx db = getObjectDb();
        try {
            return db.getEntityManager().getEntityClass(classname);
        } finally {
            db.close();
        }
    }

    @Override
    public void createUniqueIndex(Class<?> cls, String... fieldnames) {
        OObjectDatabaseTx db = getObjectDb();
        OClass oClass = db.getMetadata().getSchema().getClass(cls);

        String indexName = "compositeUniqueIndexNameAndOwner";
        boolean indexExists = oClass.getIndexes().stream().filter(i -> {
            return i.getName().equals(indexName);
        }).findFirst().isPresent();
        if (indexExists) {
            return;
        }
        Arrays.stream(fieldnames).forEach(field -> {
            // TODO need to get types reflectively
                createProperty(cls.getSimpleName(), field, OType.STRING);
            });
        oClass.createIndex(indexName, INDEX_TYPE.UNIQUE, fieldnames);

    }

    private OrientGraph getGraphDb() {
        return graphDbFactory.getTx();
    }

    private ODatabaseDocumentTx getDocumentDb() {
        ODatabaseDocumentTx db = new ODatabaseDocumentTx(getDbUrl()).open(getDbUsername(), getDbPassword());
        db.activateOnCurrentThread();
        return db;
    }

    public OObjectDatabaseTx getObjectDb() {
        OPartitionedDatabasePool opDatabasePool = new OPartitionedDatabasePool(getDbUrl(), getDbUsername(),
                getDbPassword());
        ODatabaseDocumentTx oDatabaseDocumentTx = opDatabasePool.acquire();
        return new OObjectDatabaseTx(oDatabaseDocumentTx);
    }

    @Override
    public void createProperty(String iClassName, String iPropertyName, OType type) {
        OSchemaProxyObject schema = getObjectDb().getMetadata().getSchema();
        OClass cls = schema.getClass(iClassName);
        if (cls == null) {
            cls = schema.createClass(iClassName);
        }
        if (cls.getProperty(iPropertyName) == null) {
            cls.createProperty(iPropertyName, type);
        }
    }

    @Override
    public void update(ODocument doc) {
        ODatabaseDocumentTx documentDb = getDocumentDb();
        documentDb.save(doc);
        documentDb.commit();

    }

}
