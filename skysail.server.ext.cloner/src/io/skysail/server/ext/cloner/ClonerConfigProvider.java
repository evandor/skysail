package io.skysail.server.ext.cloner;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.Designate;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL, configurationPid = "cloner", service=ClonerConfigProvider.class)
@Designate(ocd = ClonerConfig.class)
@Slf4j
public class ClonerConfigProvider {

    @Getter
    private String baseDir;

    @Activate
    public void activate(ClonerConfig clonerConfig) {
        log.debug("Activating {}", this.getClass().getName());
        baseDir = clonerConfig.clonerBaseDir() != "" ? clonerConfig.clonerBaseDir() : null;
    }

    @Deactivate
    public void deactivate(ClonerConfig clonerConfig) {
        log.debug("Deactivating {}", this.getClass().getName());
        baseDir = null;
    }

}
