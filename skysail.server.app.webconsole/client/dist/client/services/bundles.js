var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") return Reflect.decorate(decorators, target, key, desc);
    switch (arguments.length) {
        case 2: return decorators.reduceRight(function(o, d) { return (d && d(o)) || o; }, target);
        case 3: return decorators.reduceRight(function(o, d) { return (d && d(target, key)), void 0; }, void 0);
        case 4: return decorators.reduceRight(function(o, d) { return (d && d(target, key, o)) || o; }, desc);
    }
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};
var angular2_1 = require('angular2/angular2');
var http_1 = require('angular2/http');
var bundles = ['Felix', 'GOOG', 'FB', 'AMZN', 'TWTR'];
var BundlesService = (function () {
    function BundlesService(Http) {
        this.http = Http;
    }
    BundlesService.prototype.get = function () {
        return bundles;
    };
    BundlesService.prototype.add = function (stock) {
        bundles.push(stock);
        return this.get();
    };
    BundlesService.prototype.remove = function (stock) {
        bundles.splice(bundles.indexOf(stock), 1);
        return this.get();
    };
    BundlesService.prototype.load = function () {
        return this.http.get('http://localhost:2015/webconsole/v1?media=json')
            .map(function (res) { return res.json(); });
    };
    BundlesService = __decorate([
        __param(0, angular2_1.Inject(http_1.Http)), 
        __metadata('design:paramtypes', [Object])
    ], BundlesService);
    return BundlesService;
})();
exports.BundlesService = BundlesService;

//# sourceMappingURL=../services/bundles.js.map