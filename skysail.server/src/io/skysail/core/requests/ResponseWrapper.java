package io.skysail.core.requests;

import org.restlet.Response;

public class ResponseWrapper extends AbstractResponseWrapper {

    private Object entity;

    public ResponseWrapper(Object entity) {
        this.entity = entity;
    }

    public ResponseWrapper() {
    }

    public ResponseWrapper(Response response) {
        this.response = response;
    }

    @Override
    public Object getEntity() {
        return entity;
    }

//    @Override
//    public void setEntity(Object entity) {
//        this.entity = entity;
//    }

//    @Override
//    public void setConstraintViolationResponse(ConstraintViolationsResponse<T> cvr) {
//        this.constraintViolationsResponse = cvr;
//    }
//
//    public ConstraintViolationsResponse<T> getConstraintViolationsResponse() {
//        return constraintViolationsResponse;
//    }

    @Override
    public String toString() {
        return "ResponseWrapper for: " + entity;
    }

    @Override
    public void setEntity(Object entity) {
        this.entity = entity;
    }

}
