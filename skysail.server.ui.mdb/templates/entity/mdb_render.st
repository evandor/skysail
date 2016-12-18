mdb_render(user, model) ::= <<

    <sky-entity fields='$model.formfieldsWrapper.asJson$' data='$model.dataWrapper.asJson$'></sky-entity>

  <table class="table">
    $model.formfields: {field | $entity/mdb_renderRow(model, field, first(model.data))$}$
  </table>
>>