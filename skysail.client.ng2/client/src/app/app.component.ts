import {Component, OnInit} from "@angular/core";
import {Routes, ROUTER_DIRECTIVES} from "@angular/router";

import {TaskListComponent} from "./todo/components/task-list.component";
import {AboutComponent} from "./about/components/about.components";

import {Navbar} from '../app/components/navbar.component';

@Component({
    selector: "app",
    templateUrl: "./app/html/app.template.html",
    directives: [TaskListComponent, AboutComponent, Navbar, ROUTER_DIRECTIVES]
})
@Routes([
    {path: '/tasks', component: TaskListComponent},
    {path: '/about', component: AboutComponent}
])
export class AppComponent implements OnInit {
    ngOnInit() {
        console.log("Application component initialized ...");
    }
}