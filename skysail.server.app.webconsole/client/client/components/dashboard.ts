import {Component, View, Directive, NgIf, NgFor, ElementRef, Inject, OnInit} from 'angular2/angular2';

import {Summary} from './summary';

import {StocksService, StockInterface} from '../services/stocks';
import {BundlesService, BundlesInterface} from '../services/bundles';

declare var jQuery:any;

@Component({
  selector: 'dashboard',
  viewBindings: [StocksService, BundlesService]
})

@View({
  directives: [NgIf, NgFor, Summary],
  templateUrl: 'client/html/dashboard.html',
  styleUrls:  ['client/css/dashboard.css']
})

export class Dashboard implements OnInit {

  stocks: Array<StockInterface>;
  bs: Array<BundlesInterface>;
  symbols: Array<string>;
  elementRef: ElementRef;

  constructor(service: StocksService, bundlesService: BundlesService, @Inject(ElementRef) elementRef: ElementRef) {
    this.symbols = service.get();
    this.elementRef = elementRef;

    service.load(this.symbols)
        .subscribe(stocks => this.stocks = stocks);
  }

  onInit() {
    console.log("hier");
    this.ngOnInit();
  }

  ngOnInit() {
      console.log(jQuery(this.elementRef.nativeElement).find('#example'));
      jQuery(this.elementRef.nativeElement).find('#example').DataTable({	
          "ajax": 'http://localhost:2015/webconsole/v1?media=data',
          "columnDefs": [
            {
                "render": function ( data, type, row ) {
                    return '<a href="bundles/' + data + '">' + data + '</a>';
                },
                "targets": 0
            }
        ]
        	
      });
  }

}
