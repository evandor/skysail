package $basePackageName$;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.Designate;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory
import io.skysail.ext.oauth2.config.OAuth2ConfigDescriptor

@Component(
  immediate = true,
  configurationPolicy = ConfigurationPolicy.OPTIONAL,
  configurationPid = Array(TemplateApplication.APP_NAME),
  service = Array(classOf[TemplateConfiguration]))
class TemplateConfiguration {
  var config: OAuth2ConfigDescriptor = null
  @Activate def activate(config: OAuth2ConfigDescriptor) = this.config = config
  @Deactivate def deactivate(ctxt: ComponentContext) = config = null
}