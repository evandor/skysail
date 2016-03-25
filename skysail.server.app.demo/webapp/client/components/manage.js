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
var angular2_2 = require('angular2/angular2');
var stocks_1 = require('../services/stocks');
var Manage = (function () {
    function Manage(service) {
        this.service = service;
        this.symbols = service.get();
        var builder = new angular2_2.FormBuilder();
        this.stockForm = builder.group({
            stock: ['']
        });
    }
    Manage.prototype.add = function () {
        this.symbols.push(this.stockForm.value.stock.toUpperCase());
    };
    Manage.prototype.remove = function (symbol) {
        this.symbols = this.service.remove(symbol);
    };
    Manage = __decorate([
        angular2_1.Component({
            selector: 'manage',
            viewBindings: [stocks_1.StocksService]
        }),
        angular2_1.View({
            directives: [angular2_1.NgFor, angular2_2.FORM_DIRECTIVES],
            template: "\n  <div class=\"demo-grid-1 mdl-grid\">\n    <div class=\"mdl-cell mdl-cell--4-col\"></div>\n    <div class=\"mdl-cell mdl-cell--4-col\">\n      <form [ng-form-model]=\"stockForm\" style=\"margin-bottom: 5px;\" (submit)=\"add()\">\n        <input ng-control=\"stock\" class=\"mdl-textfield__input\" type=\"text\" placeholder=\"Add Stock\" />\n      </form>\n      <table class=\"mdl-data-table mdl-data-table--selectable mdl-shadow--2dp\" style=\"width: 100%;\">\n        <tbody>\n          <tr *ng-for=\"#symbol of symbols\">\n            <td class=\"mdl-data-table__cell--non-numeric\">{{symbol}}</td>\n            <td style=\"padding-top: 6px;\">\n              <button class=\"mdl-button\" (click)=\"remove(symbol)\">Remove</button>\n            </td>\n          </tr>\n        </tbody>\n      </table>\n    </div>\n    <div class=\"mdl-cell mdl-cell--4-col\"></div>\n  </div>\n"
        }), 
        __metadata('design:paramtypes', [stocks_1.StocksService])
    ], Manage);
    return Manage;
})();
exports.Manage = Manage;

//# sourceMappingURL=../components/manage.js.map