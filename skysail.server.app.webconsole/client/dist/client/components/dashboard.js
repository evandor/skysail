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
var summary_1 = require('./summary');
var stocks_1 = require('../services/stocks');
var bundles_1 = require('../services/bundles');
var Dashboard = (function () {
    function Dashboard(service, bundlesService, elementRef) {
        var _this = this;
        this.symbols = service.get();
        this.elementRef = elementRef;
        service.load(this.symbols)
            .subscribe(function (stocks) { return _this.stocks = stocks; });
    }
    Dashboard.prototype.onInit = function () {
        console.log("hier");
        this.ngOnInit();
    };
    Dashboard.prototype.ngOnInit = function () {
        console.log(jQuery(this.elementRef.nativeElement).find('#example'));
        jQuery(this.elementRef.nativeElement).find('#example').DataTable({ "ajax": 'http://localhost:2015/webconsole/v1?media=data' });
    };
    Dashboard = __decorate([
        angular2_1.Component({
            selector: 'dashboard',
            viewBindings: [stocks_1.StocksService, bundles_1.BundlesService]
        }),
        angular2_1.View({
            directives: [angular2_1.NgIf, angular2_1.NgFor, summary_1.Summary],
            templateUrl: 'client/html/dashboard.html',
            styleUrls: ['client/css/dashboard.css']
        }),
        __param(2, angular2_1.Inject(angular2_1.ElementRef)), 
        __metadata('design:paramtypes', [stocks_1.StocksService, bundles_1.BundlesService, angular2_1.ElementRef])
    ], Dashboard);
    return Dashboard;
})();
exports.Dashboard = Dashboard;

//# sourceMappingURL=../components/dashboard.js.map