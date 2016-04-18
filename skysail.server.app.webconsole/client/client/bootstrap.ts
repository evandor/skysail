import {bootstrap, FORM_PROVIDERS} from 'angular2/angular2';
import {ROUTER_PROVIDERS} from 'angular2/router';
import {HTTP_PROVIDERS} from 'angular2/http';

import {App} from './components/app';

bootstrap(App, [HTTP_PROVIDERS, ROUTER_PROVIDERS, FORM_PROVIDERS]);
