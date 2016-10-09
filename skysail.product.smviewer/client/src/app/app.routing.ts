import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
/*import { Frames2 } from './frames';
import { SidebarComponent } from './sidebar/sidebar.component';
import { SidebarsComponent } from './sidebars/sidebars.component';
import { BookmarksComponent } from './bookmarks/bookmarks.component';
import { AboutComponent } from './about/about.component';
import { FaqComponent } from './faq/faq.component';*/

//import { NoContent } from './no-content';

//import { DataResolver } from './app.resolver';

import { OverviewComponent } from './overview/overview.component';


const appRoutes: Routes = [
  //{ path: 'framework',    component: FrameworkComponent },
    //{ path: 'bundles/:id/contents/:file',  component: CodeMirrorComponent },
    //{ path: 'bundles/:id/contents',  component: BundleContentComponent },
    //{ path: 'bundles/:id',  component: BundleComponent },
    { path: 'overview',      component: OverviewComponent },
    //{ path: 'services',     component: ServicesComponent },
    //{ path: 'services/:id', component: ServiceComponent },
    //{ path: 'packages',     component: PackagesComponent },
    //{ path: 'packages/:id', component: PackageComponent },
    //{ path: 'logs',         component: LogsComponent },
    //{ path: 'snapshots',    component: SnapshotsComponent },
    //{ path: 'runtime',      component: RuntimeComponent },
    //{ path: 'config',       component: ConfigComponent },
    //{ path: 'help',         component: HelpComponent },
    { path: '**',           component: OverviewComponent }
];

export const appRoutingProviders: any[] = [

];

export const routing: ModuleWithProviders = RouterModule.forRoot(appRoutes);
