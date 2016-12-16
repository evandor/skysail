mdb_renderEntity(user, model) ::= <<

    <sky-entity ></sky-entity>

  <table class="table">
    $model.formfields: {field | $entity/mdb_renderRow(model, field, first(model.data))$}$
  </table>
>>