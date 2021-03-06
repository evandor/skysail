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
import io.skysail.ext.oauth2.OAuth2ClientParameters
import io.skysail.ext.oauth2.OAuth2ServerParameters
import io.skysail.ext.oauth2.OAuth2Proxy
import io.skysail.core.resources.SkysailServerResource

object TemplateApplication {
  final val APP_NAME = "github"
  final val TEMPLATE_AUTH_STATE = "template_auth_state"
}

@Component(
  immediate = true,
  configurationPolicy = ConfigurationPolicy.OPTIONAL,
  service = Array(classOf[ApplicationProvider], classOf[MenuItemProvider]))
class TemplateApplication extends SkysailApplication(
  TemplateApplication.APP_NAME,
  new ApiVersion(int2Integer(3))) with MenuItemProvider {

  setDescription("github api application")
  getConnectorService().getClientProtocols().add(Protocol.HTTPS)

  @Reference
  var config: TemplateConfiguration = null

  @Reference
  var githubApi: ApiServices = null

  @Activate
  override def activate(appConfig: ApplicationConfiguration, componentContext: ComponentContext) = {
    super.activate(appConfig, componentContext);
  }

  override def attach() = {
    router.attach(new RouteBuilder("", oauth2ProxyFor(classOf[UserResource])));
    router.attach(new RouteBuilder("/callback", classOf[OAuth2CallbackResource]));
    createStaticDirectory();
  }

  def oauth2ProxyFor(cls: Class[_ <: SkysailServerResource[_]]): OAuth2Proxy = {
    val clientParams = new OAuth2ClientParameters(config.config);
    val serverParams = new OAuth2ServerParameters(config.config);
    new OAuth2Proxy(getApplication(), clientParams, serverParams, cls);
  }

  def getApi(): ApiServices = githubApi



}