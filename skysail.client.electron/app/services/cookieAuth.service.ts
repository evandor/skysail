import {Injectable, EventEmitter} from "angular2/core";
import {WindowService} from './window.service';
import {Http, Headers} from 'angular2/http'

@Injectable()
export class CookieAuthService {

    private serverUrl: string;
    private username: string;
    private password: string;
    
     private authenticated:boolean = false;

    constructor(private windows: WindowService, private http: Http) {
        http.get('server.json')
            .map(res => res.json())
            .subscribe((config: any) => {
                this.serverUrl = config.serverUrl;
                this.username = config.username;
                this.password = config.password;
            })
    }

    public doLogin() {
        this.authenticated = true;
        console.log('Logged in...');
    }    
    
    public doLogout() {
        this.authenticated = false;
        console.log('Session has been cleared');
    }

/*    private emitAuthStatus(success:boolean) {
        this.locationWatcher.emit({success: success, authenticated: this.authenticated, token: this.token, expires: this.expires});
    }

    public getSession() {
        return {authenticated: this.authenticated, token: this.token, expires: this.expires};
    }

    private fetchUserInfo() {
        if (this.token != null) {
            var headers = new Headers();
            headers.append('Authorization', `Bearer ${this.token}`);
            this.http.get(this.oAuthUserUrl, {headers: headers})
                .map(res => res.json())
                .subscribe(info => {
                    this.userInfo = info;
                }, err => {
                    console.error("Failed to fetch user info:", err);
                });
        }
    }

    public getUserInfo() {
        return this.userInfo;
    }*/

    public getUserName() {
        return this.username;
    }

    public isAuthenticated() {
        return this.authenticated;
    }
}