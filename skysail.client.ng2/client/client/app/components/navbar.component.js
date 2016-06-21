System.register(["@angular/core", "@angular/router"], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
        else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    };
    var __metadata = (this && this.__metadata) || function (k, v) {
        if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
    };
    var core_1, router_1;
    var Navbar;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (router_1_1) {
                router_1 = router_1_1;
            }],
        execute: function() {
            //import {BackendServices} from '../../services/backend.service';
            Navbar = (function () {
                function Navbar(router) {
                    //_backend.setBaseUrl('http://localhost:2002/');
                    this.router = router;
                    this.currentMenuItem = "Bundles";
                    /*this.router.subscribe(val => {
                        if (val.startsWith("bundles")) {
                            this.currentMenuItem = "Bundles";
                        } else if (val.startsWith("services")) {
                            this.currentMenuItem = "Services";
                        } else if (val.startsWith("packages")) {
                            this.currentMenuItem = "Packages";
                        } else if (val == "logs") {
                            this.currentMenuItem = "Logs";
                        } else if (val == "help") {
                            this.currentMenuItem = "Help";
                        } else {
                            this.currentMenuItem = "Bundles";
                        }
                    });*/
                }
                Navbar = __decorate([
                    core_1.Component({
                        selector: 'navbar',
                        directives: [router_1.ROUTER_DIRECTIVES],
                        //providers: [BackendServices],
                        pipes: [],
                        templateUrl: 'app/html/navbar/navbar.template.html'
                    }), 
                    __metadata('design:paramtypes', [router_1.Router])
                ], Navbar);
                return Navbar;
            }());
            exports_1("Navbar", Navbar);
        }
    }
});

//# sourceMappingURL=navbar.component.js.map
