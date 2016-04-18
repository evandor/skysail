import { Inject } from 'angular2/angular2';
import { Http } from 'angular2/http';

let bundles: Array<string> = ['Felix', 'GOOG', 'FB', 'AMZN', 'TWTR'];

export interface BundlesInterface {
  id: string;
  symbolicName: string;
}

export class BundlesService {
  http: Http;
  constructor(@Inject(Http) Http) {
      this.http = Http;
  }

  get() {
    return bundles;
  }

  add(stock) {
    bundles.push(stock);
    return this.get();
  }

  remove(stock) {
    bundles.splice(bundles.indexOf(stock), 1);
    return this.get();
  }

  load() {
      return this.http.get('http://localhost:2015/webconsole/v1?media=json')
        // .toRx()
        .map(res => res.json());
  }
}
