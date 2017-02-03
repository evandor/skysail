package io.skysail.server.app.designer.repo;

import java.util.Collections;
import java.util.List;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import io.skysail.domain.Entity;
import io.skysail.domain.core.ApplicationModel;
import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.fields.DbEntityDateField;
import io.skysail.server.app.designer.fields.DbEntityDateTimeField;
import io.skysail.server.app.designer.fields.DbEntityField;
import io.skysail.server.app.designer.fields.DbEntityTextField;
import io.skysail.server.app.designer.fields.DbEntityTextareaField;
import io.skysail.server.app.designer.fields.DbEntityTimeField;
import io.skysail.server.app.designer.fields.DbEntityTrixeditorField;
import io.skysail.server.app.designer.fields.DbEntityUrlField;
import io.skysail.server.app.designer.relations.DbRelation;
import io.skysail.server.app.designer.valueobjects.DbValueObject;
import io.skysail.server.app.designer.valueobjects.DbValueObjectElement;
import io.skysail.server.db.DbClassName;
import io.skysail.server.db.DbService;
import io.skysail.server.db.GraphDbRepository;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true, property = "name=DesignerRepository")
@Slf4j
public class DesignerRepository extends GraphDbRepository<DbApplication> implements DbRepository {

    @Activate
    public void activate() {
        dbService.createWithSuperClass("V",
                DbClassName.of(DbApplication.class),
                DbClassName.of(DbValueObject.class),
                DbClassName.of(DbValueObjectElement.class),
                DbClassName.of(DbRelation.class),
                DbClassName.of(DbEntity.class),
                DbClassName.of(DbEntityDateField.class),
                DbClassName.of(DbEntityTimeField.class),
                DbClassName.of(DbEntityDateTimeField.class),
                DbClassName.of(DbEntityTextField.class),
                DbClassName.of(DbEntityTextareaField.class),
                DbClassName.of(DbEntityTrixeditorField.class),
                DbClassName.of(DbEntityUrlField.class));

        dbService.register(
                DbApplication.class,
                DbValueObject.class,
                DbValueObjectElement.class,
                DbRelation.class,
                DbEntity.class,
                DbEntityDateField.class,
                DbEntityTimeField.class,
                DbEntityDateTimeField.class,
                DbEntityTextField.class,
                DbEntityTextareaField.class,
                DbEntityTrixeditorField.class,
                DbEntityUrlField.class);

        dbService.createEdges("entities", "fields", "oneToManyRelations", "dbValueObjects");
    }

    @Override
    @Reference
    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    @Override
    public void unsetDbService(DbService dbService) {
        this.dbService = null;
    }

    @Deprecated
    public <T> List<T> findAll(Class<T> cls) {
        try {

            return dbService.findGraphs(cls, "select from " + DbClassName.of(cls));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<DbApplication> findApplications(String sql) {
        return dbService.findGraphs(DbApplication.class, sql);
    }

    public List<DbEntity> findEntities(String sql) {
        return dbService.findGraphs(DbEntity.class, sql);
    }

    public List<DbValueObject> findValueObjects(String sql) {
        return dbService.findGraphs(DbValueObject.class, sql);
    }

    public List<DbValueObjectElement> findValueObjectElements(String sql) {
        return dbService.findGraphs(DbValueObjectElement.class, sql);
    }

    public OrientVertex add(Entity entity, ApplicationModel applicationModel) {
        return (OrientVertex) dbService.persist(entity, applicationModel);
    }


    @Deprecated // ?
    public <T> T getById(Class<?> cls, String id) {
        return dbService.findById2(cls, id);
    }

    public void update(DbEntityField field, ApplicationModel applicationModel) {
        dbService.update(field, applicationModel);
    }

    @Override
    public Object update(Entity entity, ApplicationModel applicationModel) {
        return dbService.update(entity, applicationModel);
    }


    public void register(Class<?>... classes) {
        dbService.register(classes);
    }

    public void delete(Class<?> cls, String id) {
        dbService.delete2(cls, id);
    }

    public void createWithSuperClass(String superClassName, String entityClassName) {
        dbService.createWithSuperClass(superClassName, entityClassName);
    }

    public Object getVertexById(Class<DbApplication> cls, String id) {
        return dbService.findGraphs(cls.getClass(), "SELECT FROM " + cls.getSimpleName() + " WHERE @rid=" + id);
    }

//    @Override
//    public Object save(Identifiable identifiable, ApplicationModel applicationModel) {
//        return (OrientVertex) dbService.persist(identifiable, applicationModel);
//    }
//
//    @Override
//    public Identifiable findOne(String id) {
//        return dbService.findById2(DbApplication.class, id);
//    }

    public DbEntity findEntity(String id) {
        return dbService.findById2(DbEntity.class, id);
    }

    public DbValueObject findValueObject(String id) {
        return dbService.findById2(DbValueObject.class, id);
    }

    @Override
    public void delete(Entity identifiable) {
        dbService.delete2(identifiable.getClass(), identifiable.getId());
    }

}
