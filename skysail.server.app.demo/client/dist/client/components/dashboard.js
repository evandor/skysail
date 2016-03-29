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
var summary_1 = require('./summary');
var courses_1 = require('../services/courses');
var Dashboard = (function () {
    function Dashboard(service) {
        //this.symbols = service.get();
        var _this = this;
        service.load()
            .subscribe(function (courses) { return _this.courses = courses; });
    }
    Dashboard = __decorate([
        angular2_1.Component({
            selector: 'dashboard',
            viewBindings: [courses_1.CoursesService]
        }),
        angular2_1.View({
            directives: [angular2_1.NgIf, angular2_1.NgFor, summary_1.Summary],
            template: "\n    <div class=\"mdl-grid\">\n      <div class=\"mdl-cell mdl-cell--12-col\" *ng-if=\"!courses\" style=\"text-align: center;\">\n        Loading II\n      </div>\n      <div class=\"mdl-cell mdl-cell--3-col\" *ng-for=\"#course of courses\">\n        ...\n      </div>\n    </div>\n  "
        }), 
        __metadata('design:paramtypes', [courses_1.CoursesService])
    ], Dashboard);
    return Dashboard;
})();
exports.Dashboard = Dashboard;

//# sourceMappingURL=../components/dashboard.js.map