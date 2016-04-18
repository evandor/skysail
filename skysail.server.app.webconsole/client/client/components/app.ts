import {Component, View} from 'angular2/angular2';
import {RouteConfig, Router, RouterOutlet, RouterLink} from 'angular2/router';

import {Dashboard} from './dashboard';
import {Manage} from './manage';
import {Bundles} from './bundles';

@RouteConfig([
  {path: '/', as: 'Dashboard', component: Dashboard},
  {path: '/manage', as: 'Manage', component: Manage},
  {path: '/bundles', as: 'Bundles', component: Bundles}
])

@Component({
  selector: 'app'
})

@View({
  directives: [RouterOutlet, RouterLink],
  template: `
  <div class="mdl-layout mdl-js-layout mdl-layout--fixed-header">
    <header class="mdl-layout__header">
      <div class="mdl-layout__header-row">
        <span class="mdl-layout-title">OSGi Webconsole</span>
        <div class="mdl-layout-spacer"></div>
        <nav class="mdl-navigation mdl-layout--large-screen-only">
          <a class="mdl-navigation__link" [router-link]="['/Dashboard']">Dashboard</a>
          <a class="mdl-navigation__link" [router-link]="['/Bundles']">Bundles</a>
          <a class="mdl-navigation__link" [router-link]="['/Manage']">Manage</a>
        </nav>
      </div>
    </header>
    <main class="mdl-layout__content" style="padding: 20px;">
      <router-outlet></router-outlet>
    </main>
  </div>
  `
})
export class App {
}
