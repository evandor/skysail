mdb_renderRow(fields, row,i) ::= <<
  <tr>$fields: { field | <td class="renderedTableCell">$row.(field.id)$</td>}$<td style="text-align: right;">$row.("_links")$</td></tr>
>>