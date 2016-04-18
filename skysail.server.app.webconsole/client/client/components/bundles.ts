import {Component, View, NgFor} from 'angular2/angular2';
import {FORM_DIRECTIVES, FormBuilder} from 'angular2/angular2';

import {BundlesService} from '../services/bundles';

@Component({
  selector: 'bundles',
  viewBindings: [BundlesService]
})

@View({
  directives: [NgFor, FORM_DIRECTIVES],
  template: `
  <div class="demo-grid-1 mdl-grid" style="border: 1px solid grey;">
    <div class="mdl-cell mdl-cell--4-col"></div>
    <div class="mdl-cell mdl-cell--4-col">
      <form [ng-form-model]="stockForm" style="margin-bottom: 5px;" (submit)="add()">
        <input ng-control="stock" class="mdl-textfield__input" type="text" placeholder="Add Stock" />
      </form>
      <table class="mdl-data-table mdl-data-table--selectable mdl-shadow--2dp" style="width: 100%;">
        <tbody>
          <tr *ng-for="#symbol of symbols">
            <td class="mdl-data-table__cell--non-numeric">{{symbol}}</td>
            <td style="padding-top: 6px;">
              <button class="mdl-button" (click)="remove(symbol)">Remove</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="mdl-cell mdl-cell--4-col"></div>
  </div>
`
})
export class Bundles {
  symbols: Array<string>;
  service: BundlesService;
  stockForm: any;

  constructor(service: BundlesService) {
    this.service = service;
    this.symbols = service.get();

    let builder = new FormBuilder();
    this.stockForm = builder.group({
      stock: ['']
    });
  }

  add() {
    this.symbols.push(this.stockForm.value.stock.toUpperCase());
  }

  remove(symbol) {
    this.symbols = this.service.remove(symbol);
  }

}
