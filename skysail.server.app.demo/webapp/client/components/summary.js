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
var angular2_1 = require('angular2/angular2');
var Summary = (function () {
    function Summary() {
    }
    Summary.prototype.isNegative = function () {
        if (!this.stock || this.stock.change >= 0) {
            return false;
        }
        return true;
    };
    Summary.prototype.isPositive = function () {
        if (!this.stock || this.stock.change <= 0) {
            return false;
        }
        return true;
    };
    Summary = __decorate([
        angular2_1.Component({
            selector: 'summary',
            properties: ['stock: symbol']
        }),
        angular2_1.View({
            directives: [angular2_1.NgIf, angular2_1.NgClass],
            template: "\n<div class=\"mdl-card stock-card mdl-shadow--2dp\" [ng-class]=\"{increase: isPositive(), decrease: isNegative()}\" style=\"width: 100%;\">\n  <span *ng-if=\"stock\">\n    <div class=\"mdl-card__title\">\n      <h4 style=\"color: #fff; margin: 0\">\n        {{stock.symbol.toUpperCase()}}<br />\n        {{stock.lastTradePriceOnly | currency:'USD':true:'.2'}}<br />\n        {{stock.change | currency:'USD':true:'.2'}} ({{stock.changeInPercent | percent}})\n      </h4>\n    </div>\n  </span>\n</div>\n"
        }), 
        __metadata('design:paramtypes', [])
    ], Summary);
    return Summary;
})();
exports.Summary = Summary;

//# sourceMappingURL=../components/summary.js.map