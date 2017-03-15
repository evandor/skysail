bst_renderEntity(user, model) ::= <<
  <table class="table">
    $model.formfields: {field | $bst_renderRow(model, field, first(model.data))$}$
  </table>
>>