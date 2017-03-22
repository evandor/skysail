package io.skysail.app.facebook

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.Designate;

import io.skysail.ext.oauth2.config.OAuth2ConfigDescriptor
import org.slf4j.LoggerFactory

@Component(
  immediate = true,
  configurationPolicy = ConfigurationPolicy.OPTIONAL,
  configurationPid = Array(FacebookApplication.APP_NAME),
  service = Array(classOf[FacebookConfiguration]))
class FacebookConfiguration {
  var config: OAuth2ConfigDescriptor = null
  @Activate def activate(config: OAuth2ConfigDescriptor) = this.config = config
  @Deactivate def deactivate(ctxt: ComponentContext) = config = null
}