package io.skysail.core.requests;

import org.restlet.Response;
import org.restlet.data.Status;

import io.skysail.core.app.SkysailApplication;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExceptionCatchingFilterHelper {

    private ExceptionCatchingFilterHelper() {
    }

    public static void handleError(Exception e, SkysailApplication application, Wrapper2 responseWrapper, Class<?> cls) {
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
