package io.skysail.app.instagram.config

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.Designate;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import io.skysail.app.instagram.InstagramApplication
import io.skysail.ext.oauth2.config.OAuth2ConfigDescriptor

@Component(
  immediate = true,
  configurationPolicy = ConfigurationPolicy.OPTIONAL,
  configurationPid = Array(InstagramApplication.APP_NAME),
  service = Array(classOf[InstagramConfiguration]))
//@Designate(ocd = classOf[OAuth2ConfigDescriptor])
class InstagramConfiguration {

  var config: OAuth2ConfigDescriptor = null

  @Activate def activate(config: OAuth2ConfigDescriptor) = this.config = config
  @Deactivate def deactivate(ctxt: ComponentContext) = config = null

}