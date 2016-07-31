import {Component} from "@angular/core";
import {ROUTER_DIRECTIVES, Router} from "@angular/router";
import {BackendServices} from '../services/backend.service';

@Component({
    selector: 'navbar',
    directives: [ROUTER_DIRECTIVES],
    providers: [BackendServices],
    pipes: [],
    templateUrl: 'app/html/navbar/navbar.template.html'
})
export class Navbar {

    constructor(private router: Router, private _backend: BackendServices) {
        
    }

}

