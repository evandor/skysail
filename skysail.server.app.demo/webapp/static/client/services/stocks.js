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
var stocks = ['AAPL', 'GOOG', 'FB', 'AMZN', 'TWTR'];
var StocksService = (function () {
    function StocksService(Http) {
        this.http = Http;
    }
    StocksService.prototype.get = function () {
        return stocks;
    };
    StocksService.prototype.add = function (stock) {
        stocks.push(stock);
        return this.get();
    };
    StocksService.prototype.remove = function (stock) {
        stocks.splice(stocks.indexOf(stock), 1);
        return this.get();
    };
    StocksService.prototype.load = function (symbols) {
        if (symbols) {
            return this.http.get('/api/snapshot?symbols=' + symbols.join())
                .map(function (res) { return res.json(); });
        }
    };
    StocksService = __decorate([
        __param(0, angular2_1.Inject(http_1.Http)), 
        __metadata('design:paramtypes', [Object])
    ], StocksService);
    return StocksService;
})();
exports.StocksService = StocksService;

//# sourceMappingURL=../services/stocks.js.map