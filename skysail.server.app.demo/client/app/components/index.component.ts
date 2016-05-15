import {Component} from 'angular2/core';

import {Navbar} from './navbar/navbar.component';

@Component({
    templateUrl: '/app/html/index.template.html',
    directives: [Navbar],
    providers: [Navbar]
})
export class IndexComponent {
}