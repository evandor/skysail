sui_renderRow(fields, row, i) ::= <<
  <tr>$fields: { field | <td>$row.(field.id)$</td>}$<td>$row.("_links")$</td></tr>
>>