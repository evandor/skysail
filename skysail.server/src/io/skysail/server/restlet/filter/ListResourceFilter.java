//package io.skysail.server.restlet.filter;
//
//import io.skysail.domain.Entity;
//import io.skysail.server.restlet.resources.ListServerResource;
//import io.skysail.server.restlet.response.ResponseWrapper;
//
//import org.restlet.Request;
//
//public interface ListResourceFilter<T extends Entity> {
//
//    public FilterResult beforeHandle(ListServerResource<T> resource, Request request, ResponseWrapper<T> response);
//
//    public FilterResult doHandle(ListServerResource<T> resource, Request request, ResponseWrapper<T> response);
//
//    public void afterHandle(ListServerResource<T> resource, Request request, ResponseWrapper<T> response);
//
//}
