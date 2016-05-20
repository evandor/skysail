System.register(["angular2/core", './window.service', 'angular2/http'], function(exports_1, context_1) {
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
    var core_1, window_service_1, http_1;
    var CookieAuthService;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (window_service_1_1) {
                window_service_1 = window_service_1_1;
            },
            function (http_1_1) {
                http_1 = http_1_1;
            }],
        execute: function() {
            CookieAuthService = (function () {
                function CookieAuthService(windows, http) {
                    var _this = this;
                    this.windows = windows;
                    this.http = http;
                    this.authenticated = false;
                    http.get('server.json')
                        .map(function (res) { return res.json(); })
                        .subscribe(function (config) {
                        _this.serverUrl = config.serverUrl;
                        _this.username = config.username;
                        _this.password = config.password;
                    });
                }
                CookieAuthService.prototype.doLogin = function () {
                    this.authenticated = true;
                    console.log('Logged in...');
                };
                CookieAuthService.prototype.doLogout = function () {
                    this.authenticated = false;
                    console.log('Session has been cleared');
                };
                /*    private emitAuthStatus(success:boolean) {
                        this.locationWatcher.emit({success: success, authenticated: this.authenticated, token: this.token, expires: this.expires});
                    }
                
                    public getSession() {
                        return {authenticated: this.authenticated, token: this.token, expires: this.expires};
                    }
                
                    private fetchUserInfo() {
                        if (this.token != null) {
                            var headers = new Headers();
                            headers.append('Authorization', `Bearer ${this.token}`);
                            this.http.get(this.oAuthUserUrl, {headers: headers})
                                .map(res => res.json())
                                .subscribe(info => {
                                    this.userInfo = info;
                                }, err => {
                                    console.error("Failed to fetch user info:", err);
                                });
                        }
                    }
                
                    public getUserInfo() {
                        return this.userInfo;
                    }*/
                CookieAuthService.prototype.getUserName = function () {
                    return this.username;
                };
                CookieAuthService.prototype.isAuthenticated = function () {
                    return this.authenticated;
                };
                CookieAuthService = __decorate([
                    core_1.Injectable(), 
                    __metadata('design:paramtypes', [window_service_1.WindowService, http_1.Http])
                ], CookieAuthService);
                return CookieAuthService;
            }());
            exports_1("CookieAuthService", CookieAuthService);
        }
    }
});
//# sourceMappingURL=cookieAuth.service.js.map