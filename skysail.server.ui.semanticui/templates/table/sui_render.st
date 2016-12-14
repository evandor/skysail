sui_render(converter, messages, model) ::= <<

<table class="ui celled table">
  <thead>
    <tr>
      $table/sui_renderHeader(model.formfields, converter)$<th>Actions</th>
    </tr>
  </thead>
  <tbody>
    $model.data:{ row | $table/sui_renderRow(model.formfields,row,i)$}$
  </tbody>
  <tfoot>
    <tr><th colspan="4">
      <div class="ui right floated pagination menu">
        <a class="icon item">
          <i class="left chevron icon"></i>
        </a>
        <a class="item">1</a>
        <a class="item">2</a>
        <a class="item">3</a>
        <a class="item">4</a>
        <a class="icon item">
          <i class="right chevron icon"></i>
        </a>
      </div>
    </th>
  </tr></tfoot>
</table>

>>
