package io.skysail.app.facebook

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.Designate;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import io.skysail.ext.oauth2.config.OAuth2ConfigDescriptor
import org.slf4j.LoggerFactory

@Component(
  immediate = true,
  configurationPolicy = ConfigurationPolicy.OPTIONAL,
  configurationPid = Array(FacebookApplication.APP_NAME),
  service = Array(classOf[FacebookConfiguration]))
class FacebookConfiguration {
  val log = LoggerFactory.getLogger(classOf[FacebookConfiguration])
  var config: OAuth2ConfigDescriptor = null
  @Activate def activate(config: OAuth2ConfigDescriptor) = {
    log.info("Facebook Configuration set to {}", config)
    this.config = config
  }
  @Deactivate def deactivate(ctxt: ComponentContext) = {
    log.info("Facebook Configuration unset")
    config = null
  }
}