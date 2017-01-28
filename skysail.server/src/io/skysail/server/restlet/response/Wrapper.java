package io.skysail.server.restlet.response;

import java.util.List;

import org.restlet.Response;

import io.skysail.api.responses.ConstraintViolationsResponse;
import io.skysail.domain.Entity;

public interface Wrapper<T extends Entity> {

    Response getResponse();

    Object getEntity();

    void setEntity(Object entity);

    void setConstraintViolationResponse(ConstraintViolationsResponse<T> reponse);

    void addError(String msg);
    void addInfo(String msg);
    void addWarning(String msg);

    List<Long> getMessageIds();

}
