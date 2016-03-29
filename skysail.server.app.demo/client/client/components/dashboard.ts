import {Component, View, Directive, NgIf, NgFor} from 'angular2/angular2';

import {Summary} from './summary';
//import {StocksService, StockInterface} from '../services/stocks';
import {CoursesService, CourseInterface} from '../services/courses';

@Component({
  selector: 'dashboard',
  viewBindings: [CoursesService]
})
@View({
  directives: [NgIf, NgFor, Summary],
  template: `
    <div class="mdl-grid">
      <div class="mdl-cell mdl-cell--12-col" *ng-if="!courses" style="text-align: center;">
        Loading II
      </div>
      <div class="mdl-cell mdl-cell--3-col" *ng-for="#course of courses">
        ...
      </div>
    </div>
  `
})
export class Dashboard {
  courses: Array<CourseInterface>;
  symbols: Array<string>;

  constructor(service: CoursesService) {
    //this.symbols = service.get();

    service.load()
    .subscribe(courses => this.courses = courses);
  }
}
