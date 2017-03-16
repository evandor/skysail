package io.skysail.server.restlet.response;

import java.util.List;

import org.restlet.Response;

import io.skysail.api.responses.ConstraintViolationsResponse;
import io.skysail.domain.Entity;
import lombok.*;

public class ListResponseWrapper extends AbstractListResponseWrapper {

    @Getter
    private List<?> entity;

    public ListResponseWrapper(List<?> entity) {
        this.entity = entity;
    }

    public ListResponseWrapper() {
    }

    public ListResponseWrapper(Response response) {
        this.response = response;
    }

//    @Override
//    public void setConstraintViolationResponse(ConstraintViolationsResponse cvr) {
//        this.constraintViolationsResponse = cvr;
//    }
//
//    public ConstraintViolationsResponse<T> getConstraintViolationsResponse() {
//        return constraintViolationsResponse;
//    }

    @Override
    public void setEntity(Object entity) {
        this.entity = (List<?>) entity;
    }

}
