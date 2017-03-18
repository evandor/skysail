package $basePackageName$;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.restlet.data.Protocol;
import io.skysail.core.app.SkysailApplication
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import io.skysail.core.app.ApplicationProvider
import io.skysail.server.menus.MenuItemProvider
import io.skysail.core.app.ApiVersion
import org.osgi.service.component.annotations.Activate
import org.osgi.service.component.ComponentContext
import io.skysail.core.app.ApplicationConfiguration
import org.restlet.data.Protocol
import io.skysail.server.restlet.RouteBuilder
import io.skysail.server.menus.MenuItem
import java.util.Arrays

object TemplateApplication {
  final val APP_NAME = "templateApp"
}

@Component(
  immediate = true, 
  configurationPolicy = ConfigurationPolicy.OPTIONAL, 
  service = Array(classOf[ApplicationProvider], classOf[MenuItemProvider])
)
class TemplateApplication extends SkysailApplication(
    TemplateApplication.APP_NAME, 
    new ApiVersion(int2Integer(1))
) with MenuItemProvider {

  setDescription("template app scala")
  getConnectorService().getClientProtocols().add(Protocol.HTTPS)

  @Activate
  override def activate(appConfig: ApplicationConfiguration, componentContext: ComponentContext) = {
    super.activate(appConfig, componentContext);
  }

}