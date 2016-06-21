/* Avoid: 'error TS2304: Cannot find name <type>' during compilation */
///<reference path="../../typings/index.d.ts"/>
System.register(["./app.component", "@angular/platform-browser-dynamic", "@angular/core", "@angular/common", "@angular/router"], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var app_component_1, platform_browser_dynamic_1, core_1, common_1, router_1;
    return {
        setters:[
            function (app_component_1_1) {
                app_component_1 = app_component_1_1;
            },
            function (platform_browser_dynamic_1_1) {
                platform_browser_dynamic_1 = platform_browser_dynamic_1_1;
            },
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (common_1_1) {
                common_1 = common_1_1;
            },
            function (router_1_1) {
                router_1 = router_1_1;
            }],
        execute: function() {
            platform_browser_dynamic_1.bootstrap(app_component_1.AppComponent, [
                router_1.ROUTER_PROVIDERS,
                core_1.provide(common_1.LocationStrategy, { useClass: common_1.HashLocationStrategy })
            ]);
        }
    }
});
//bootstrap(AppComponent,[CookieService, AuthService, WindowService, ROUTER_PROVIDERS, HTTP_PROVIDERS, provide(Window, {useValue: window})]); 

//# sourceMappingURL=main.js.map
