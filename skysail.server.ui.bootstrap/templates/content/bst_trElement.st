bst_trElement(ff,row) ::= <<
  <tr>
    $ff: { field | <td>$row.(field.name)$</td>}$<td>$row.("_links")$</td>
  </tr>
>>