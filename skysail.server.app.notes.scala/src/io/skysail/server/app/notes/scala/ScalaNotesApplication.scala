package io.skysail.server.app.notes.scala

import io.skysail.domain.core.Repositories
import org.osgi.service.component.annotations.Reference
import org.osgi.service.component.annotations.Activate
import io.skysail.server.security.config.SecurityConfigBuilder
import org.osgi.service.component.annotations.ReferenceCardinality
import org.osgi.service.component.annotations.ReferencePolicy
import io.skysail.domain.core.Repositories
import io.skysail.server.app.ApplicationConfiguration
import io.skysail.server.app.SkysailApplication
import io.skysail.server.app.ApplicationProvider
import io.skysail.server.menus.MenuItemProvider
import org.osgi.service.component.ComponentContext


class ScalaNotesApplication extends SkysailApplication("scalanotes")
    with ApplicationProvider with MenuItemProvider {

  setDescription("scala version of notes application")

  @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
  override def setRepositories(repos: Repositories) {
    super.setRepositories(repos);
  }

  def unsetRepositories(repo: Repositories) {
    super.setRepositories(null);
  }

  @Activate
  override def activate(appConfig: ApplicationConfiguration, componentContext: ComponentContext) {
    super.activate(appConfig, componentContext);
  }

  override def defineSecurityConfig(securityConfigBuilder: SecurityConfigBuilder) {
    securityConfigBuilder
      .authorizeRequests().startsWithMatcher("").authenticated();
  }

  override def attach() {
    super.attach();

    //        router.attach(new RouteBuilder("/Bookmarks/{id}", BookmarkResource.class));
    //        router.attach(new RouteBuilder("/Bookmarks/", PostBookmarkResource.class));
    //        router.attach(new RouteBuilder("/Bookmarks/{id}/", PutBookmarkResource.class));
    //        router.attach(new RouteBuilder("/Bookmarks", BookmarksResource.class));
    //        router.attach(new RouteBuilder("", BookmarksResource.class));
  }
}