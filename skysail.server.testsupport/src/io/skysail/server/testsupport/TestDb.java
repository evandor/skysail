package io.skysail.server.testsupport;

public class TestDb { // extends OrientGraphDbService {

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
}
