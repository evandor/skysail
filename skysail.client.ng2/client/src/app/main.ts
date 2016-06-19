/* Avoid: 'error TS2304: Cannot find name <type>' during compilation */
///<reference path="../../typings/index.d.ts"/>

import {AppComponent} from "./app.component";
import {bootstrap} from "@angular/platform-browser-dynamic";
import {provide} from "@angular/core";
import {LocationStrategy, HashLocationStrategy} from "@angular/common";
import {ROUTER_PROVIDERS} from "@angular/router";

bootstrap(AppComponent, [
    ROUTER_PROVIDERS,
    provide(LocationStrategy, {useClass: HashLocationStrategy})
]);

//bootstrap(AppComponent,[CookieService, AuthService, WindowService, ROUTER_PROVIDERS, HTTP_PROVIDERS, provide(Window, {useValue: window})]);