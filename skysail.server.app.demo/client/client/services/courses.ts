import { Inject } from 'angular2/angular2';
import { Http } from 'angular2/http';

let courses: Array<CourseInterface> = [];

export interface CourseInterface {
  id: string;
  timeinmillis: string;
}

export class CoursesService {
  http: Http;
  constructor(@Inject(Http) Http) {
      this.http = Http;
  }

  get() {
    return courses;
  }

  add(course) {
    courses.push(course);
    return this.get();
  }

  remove(course) {
    courses.splice(courses.indexOf(course), 1);
    return this.get();
  }

  load() {
    //if (symbols) {
      return this.http.get('http://localhost:2015/demoapp/v1/unprotected/times?media=json')
        // .toRx()
        .map(res => res.json())
        ;
    //}
  }
}
