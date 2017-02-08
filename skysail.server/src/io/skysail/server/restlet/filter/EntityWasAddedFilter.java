package io.skysail.server.restlet.filter;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.skysail.core.app.SkysailApplication;
import io.skysail.domain.Entity;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.restlet.response.Wrapper;
import io.skysail.server.services.MessageQueueHandler;
import io.skysail.server.services.MessageQueueProvider;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class EntityWasAddedFilter<R extends SkysailServerResource<T>, T extends Entity>
        extends AbstractResourceFilter<R, T> {

    private SkysailApplication application;

    private static ObjectMapper mapper = new ObjectMapper();

    public EntityWasAddedFilter(SkysailApplication skysailApplication) {
        this.application = skysailApplication;
    }

    @Override
    public FilterResult doHandle(R resource, Wrapper<T> responseWrapper) {
        log.debug("entering {}#doHandle", this.getClass().getSimpleName());
        String infoMessage = resource.getClass().getSimpleName() + ".saved.success";
        responseWrapper.addInfo(infoMessage);

        if (application instanceof MessageQueueProvider) {
            MessageQueueHandler messageQueueHandler = ((MessageQueueProvider) application)
                    .getMessageQueueHandler();
            if (messageQueueHandler != null) {
                Object currentEntity = resource.getCurrentEntity();
                try {
                    String serialized = mapper.writeValueAsString(currentEntity);
                    messageQueueHandler.send("topic://entity." + currentEntity.getClass().getName().replace(".", "_") + ".post", serialized);
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        super.doHandle(resource, responseWrapper);
        return FilterResult.CONTINUE;
    }
}
