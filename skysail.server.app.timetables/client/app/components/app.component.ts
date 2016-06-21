import {Component} from 'angular2/core';
import {RouteConfig, RouterOutlet, RouterLink} from 'angular2/router';

import {CoursesComponent} from './courses.component';
import {IndexComponent} from './index.component';
import {FooterComponent} from './footer/footer.component';

import {Navbar} from './navbar/navbar.component';

@RouteConfig([
    { path: '/courses', as: 'Courses', component: CoursesComponent },
    { path: '/', as: 'Index', component: IndexComponent }
])
@Component({
    selector: 'my-app',
    templateUrl: 'app/html/app.template.html',
    providers: [Navbar],
    directives: [RouterOutlet,RouterLink, FooterComponent,Navbar]
})
export class AppComponent { }