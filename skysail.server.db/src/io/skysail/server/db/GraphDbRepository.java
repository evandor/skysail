package io.skysail.server.db;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.restlet.engine.util.StringUtils;

import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import io.skysail.core.utils.ReflectionUtils;
import io.skysail.domain.Entity;
import io.skysail.domain.core.ApplicationModel;
import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.queryfilter.filtering.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.queryfilter.sorting.Sorting;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GraphDbRepository<T extends Entity> implements DbRepository {

    protected DbService dbService;

    private Class<T> entityType;

    @SuppressWarnings("unchecked")
    public GraphDbRepository() {
        entityType = (Class<T>) ReflectionUtils.getParameterizedType(getClass());
    }

    public void activate(Class<?>... classes) {
        log.debug("activating repository for class(es) {}",
                Arrays.stream(classes).map(Class::getName).collect(Collectors.joining(",")));
        Arrays.stream(classes).forEach(cls -> {
            try {
                dbService.createWithSuperClass("V", DbClassName.of(cls));
                dbService.register(cls);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    public void setDbService(DbService dbService2) {
    	log.info("setting dbService {} in GraphDbRepository {}", dbService2, this.getClass().getName());
    	this.dbService = dbService2;
	}

	public void unsetDbService(DbService dbService2) {
	   	log.info("unsetting dbService in GraphDbRepository {}", dbService2, this.getClass().getName());
	   	this.dbService = null;
	}

    @SuppressWarnings("unchecked")
    @Override
    public Class<Entity> getRootEntity() {
        return (Class<Entity>) entityType;
    }

    @Override
    public OrientVertex save(Entity entity, ApplicationModel applicationModel) {
        return (OrientVertex) dbService.persist(entity, applicationModel);
    }

    @Override
    public Object update(Entity entity, ApplicationModel model) {
        return dbService.update(entity, model);
    }

    public void delete(String id) {
        dbService.delete2(entityType, id);
    }

    public void deleteVertex(String id) {
        dbService.deleteVertex(id);
    }

    @Override
    public void delete(Entity identifiable) {
    }

    @Override
    public T findOne(String id) {
        return dbService.findById2(entityType, id);
    }


    @Override
    public Optional<Entity> findOne(String identifierKey, String id) {
         List<T> result = dbService.findGraphs(entityType,
                 "SELECT * from " + DbClassName.of(entityType) + " WHERE " + identifierKey + "="  +id);
         if (result.size() > 1) {
             throw new IllegalAccessError();
         } else if (result.size() == 1) {
             return Optional.of(result.get(0));
         } else {
             return Optional.empty();
         }

    }

    public Object getVertexById(String id) {
        return dbService.findGraphs(entityType.getClass(),
                "SELECT FROM " + DbClassName.of(entityType) + " WHERE @rid=" + id);
    }

    public List<T> find(Filter filter) {
        return find(filter, new Pagination());
    }

    public long count(Filter filter) {
        String sql = "SELECT count(*) as count from " + DbClassName.of(entityType)
                + (!StringUtils.isNullOrEmpty(filter.getPreparedStatement()) ? " WHERE " + filter.getPreparedStatement()
                        : "");
        return dbService.getCount(sql, filter.getParams());
    }

    public long count(Class<? extends Entity> cls, String countSql, Filter filter) {
        return dbService.getCount(countSql, filter.getParams());
    }

    public List<T> find(Filter filter, Pagination pagination) {
        String sql = "SELECT * from " + DbClassName.of(entityType)
                + (!StringUtils.isNullOrEmpty(filter.getPreparedStatement()) ? " WHERE " + filter.getPreparedStatement()
                        : "")
                + " " + limitClause(pagination);
        pagination.setEntityCount(count(filter));
        return dbService.findGraphs(entityType, sql, filter.getParams());
    }

    public <S extends Entity> List<S> find(Class<S> cls, String whereSql, Map<String, Object> params) {
        String sql = "select * from " + DbClassName.of(cls) + " where " + whereSql;
        return dbService.findGraphs(cls, sql, params);
    }

    public <S extends Entity> List<S> find(Class<S> cls, String whereSql, Filter filter, Sorting sorting, Pagination pagination) {



        String applyFilters = !StringUtils.isNullOrEmpty(filter.getPreparedStatement()) ? " AND (" + filter.getPreparedStatement() + ")": "";
        String orderBy = sorting.getOrderBy();

        String countSql = "select count(*) as count from " + DbClassName.of(cls) + " where " + whereSql + applyFilters;
        String sql = "select * from " + DbClassName.of(cls) + " where " + whereSql + applyFilters + orderBy;

        pagination.setEntityCount(count(cls, countSql, filter));

        sql = sql
                //+ applyFilters
                + " " + limitClause(pagination);
        return dbService.findGraphs(cls, sql, filter.getParams());
    }

    public List<?> execute(Class<? extends Entity> class1, String sql) {
        return dbService.findGraphs(class1, sql);
    }

    protected String limitClause(Pagination pagination) {
        if (pagination == null) {
            return "";
        }
        long linesPerPage = pagination.getLinesPerPage();
        long page = pagination.getPage();
        if (linesPerPage <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder("SKIP " + linesPerPage * (page - 1) + " LIMIT " + linesPerPage);
        return sb.toString();
    }

}
