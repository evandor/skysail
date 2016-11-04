package io.skysail.server.testsupport;

import java.util.List;
import java.util.Map;

import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.impl.ODocument;

import io.skysail.domain.Identifiable;
import io.skysail.domain.core.ApplicationModel;
import io.skysail.server.db.DbService;

public class TestDb implements DbService {

	protected String getDbUrl() {
		// return "plocal:etc/db";//"remote:localhost/designer";//"memory:test";
		return "memory:test";
	}

	protected String getDbUsername() {
		return "admin";
	}

	protected String getDbPassword() {
		return "admin";
	}

	@Override
	public Class<?> getRegisteredClass(String classname) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createUniqueIndex(Class<?> cls, String... columnNames) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createWithSuperClass(String superClass, String... vertices) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createEdges(String... vertices) {
		// TODO Auto-generated method stub

	}

	@Override
	public void register(Class<?>... classes) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createProperty(String simpleName, String string, OType date) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object update(Identifiable entity, ApplicationModel applicationModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> findObjects(String sql) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> findObjects(String sql, Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object persist(Identifiable entity, ApplicationModel applicationModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> findGraphs(Class<T> cls, String sql) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> findGraphs(Class<T> cls, String sql, Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteVertex(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> T findById2(Class<?> cls, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Class<?> cls, String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete2(Class<?> cls, String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object executeUpdate(String sql, Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(ODocument doc) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object executeUpdateVertex(String sql, Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getCount(String sql, Map<String, Object> params) {
		// TODO Auto-generated method stub
		return 0;
	}
}
