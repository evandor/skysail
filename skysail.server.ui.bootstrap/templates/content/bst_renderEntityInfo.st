bst_renderEntityInfo(fields, messages, model) ::= <<
  $if (model.postEntityServerResource)$

  $elseif (model.putEntityServerResource)$
    <form action="$model.deleteFormTarget$?method=DELETE" method="POST" class="form-horizontal" id="delete-form" name="delete-form">
      <div class="form-group">
          <button type="submit" class="btn btn-warning">Delete</button>
      </div>
    </form>
    <hr>
    $fields:{ field | $bst_handleReadonlyInputField(model, first(model.data), first(model.rawData), field, messages)$}$
  $endif$
  
>>