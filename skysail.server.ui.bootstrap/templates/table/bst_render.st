bst_render(converter, messages, model) ::= <<

  $table/bst_table(model.formfieldsWrapper, model.dataWrapper)$

  $model.pagination$

  <table class="table table-striped table-condensed table-hover ROWCLICK">
    <tr>$table/bst_renderHeader(model.formfields, converter)$<th style="text-align: right;">Actions</th></tr>
    $model.data:{ row | $table/bst_renderRow(model.formfields,row,i)$}$
  </table>
>>
