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
var core_1 = require('angular2/core');
var JqueryIntegration = (function () {
    function JqueryIntegration(elementRef) {
        this.elementRef = elementRef;
    }
    JqueryIntegration.prototype.ngOnInit = function () {
        console.log("ngOnInit in jquery.integration");
        jQuery(this.elementRef.nativeElement).find('#example').DataTable();
    };
    JqueryIntegration = __decorate([
        core_1.Component({
            selector: 'jquery-integration',
            templateUrl: './client/html/jquery-integration.html'
        }),
        __param(0, core_1.Inject(core_1.ElementRef)), 
        __metadata('design:paramtypes', [core_1.ElementRef])
    ], JqueryIntegration);
    return JqueryIntegration;
})();
exports.JqueryIntegration = JqueryIntegration;

//# sourceMappingURL=../components/jquery.integration.js.map