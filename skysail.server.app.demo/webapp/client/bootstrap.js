var angular2_1 = require('angular2/angular2');
var router_1 = require('angular2/router');
var http_1 = require('angular2/http');
var app_1 = require('./components/app');
angular2_1.bootstrap(app_1.App, [http_1.HTTP_PROVIDERS, router_1.ROUTER_PROVIDERS, angular2_1.FORM_PROVIDERS]);

//# sourceMappingURL=bootstrap.js.map