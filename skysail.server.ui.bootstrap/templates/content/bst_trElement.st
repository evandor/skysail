bst_trElement(ff,row) ::= <<
  <tr>
    $ff: { field | <td>$row.(field.label)$</td>}$<td>$row.("_links")$</td>
  </tr>
>>