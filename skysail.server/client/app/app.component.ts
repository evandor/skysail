import {Component} from 'angular2/core';
import {RouteConfig, RouterOutlet, RouterLink} from 'angular2/router';

import {CoursesComponent} from './components/courses.component';
import {IndexComponent} from './components/index.component';
import {FooterComponent} from './components/footer.component';
import {GitHubProfileComponent} from './components/git-profile.component';

@RouteConfig([
    { path: '/courses', as: 'Courses', component: CoursesComponent },
    { path: '/index', as: 'Index', component: IndexComponent },
    { path: '/github', as: 'Github', component: GitHubProfileComponent },
])
@Component({
    selector: 'my-app',
    templateUrl: 'app/html/app.template.html',
    directives: [RouterOutlet,RouterLink, FooterComponent]
})
export class AppComponent { }