import {Injectable} from '@angular/core'
import {Http, Headers, RequestOptions} from '@angular/http'
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';

@Injectable()
export class BackendServices {

    private _baseUrl = '';

    constructor(private _http: Http, private _window: Window) { //, private _config: ConfigService) {
        var hostname = this._window.location.hostname;
        this._baseUrl = "http://localhost:2018";// + this._config.host + ":" + this._config.port + "/";//baseUrl;
        console.log("base url set to '" + this._baseUrl + "'");
    }

    get(path) {
        var headers = new Headers();
        //headers.append('Authorization', 'Basic YWRtaW46c2t5c2FpbA==');
        return this._http.get(this._baseUrl + path, { headers: headers })
            .map(res => res.json());
    }

    getFramework(): Observable<any> {
        return this._http.get(this._baseUrl + 'backend/framework')
            .map(res => res.json());
    }

}