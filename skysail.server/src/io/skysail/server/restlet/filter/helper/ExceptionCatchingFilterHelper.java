package io.skysail.server.restlet.filter.helper;

import org.restlet.Response;
import org.restlet.data.Status;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.response.Wrapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExceptionCatchingFilterHelper {

    private ExceptionCatchingFilterHelper() {
    }

    public static void handleError(Exception e, SkysailApplication application, Wrapper<?> responseWrapper, Class<?> cls) {
        log.error(e.getMessage(), e);

        String genericErrorMessageForGui = cls.getSimpleName() + ".saved.failure";
        responseWrapper.addError(genericErrorMessageForGui);

        Response response = responseWrapper.getResponse();
        response.setStatus(Status.SERVER_ERROR_INTERNAL);

        responseWrapper.addInfo(e.getMessage());

        if (application == null) {
            return;
        }
    }

}
