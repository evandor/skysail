bst_renderHeader(fields, converter) ::= <<
  $if(converter.edit)$
    $fields: { field | <th contenteditable="true" data-url="/i18n/v1/messages/$field.nameKey$/msg/" class="editable">$util/bst_helpmessage(messages, field.nameKey)$</th>}$
  $else$
    $fields: { field | <th style="text-align: right;"><a href='$field.toggleSortLink$'>$util/bst_helpmessage(messages, field.nameKey)$</a>$field.sortIndicator$</th>}$
  $endif$
>>
