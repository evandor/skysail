bst_renderRow(model, field, data) ::= <<
  <tr><th>$field.id$</th><td>&nbsp;<td>$data.(field.id)$</td><td>$bst_handleDates(model, data,field)$</td></tr>
>>