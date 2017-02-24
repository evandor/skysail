//package io.skysail.server.security.config;
//
//import org.restlet.Context;
//import org.restlet.Request;
//import org.restlet.Response;
//import org.restlet.data.Status;
//import org.restlet.security.Authenticator;
//
//public class AnonymousAuthenticator extends Authenticator {
//
//	public AnonymousAuthenticator(Context context) {
//		super(context);
//	}
//
//	@Override
//	protected boolean authenticate(Request request, Response response) {
//		response.setStatus(Status.CLIENT_ERROR_FORBIDDEN);
//		return false;
//	}
//
//}
