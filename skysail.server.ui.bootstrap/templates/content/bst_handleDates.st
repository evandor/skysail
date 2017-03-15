bst_handleDates(model, data, field) ::= <<
    $if(field.dateType)$
    <script>document.write(moment("$data.(field.id)$", "$model.dateFormat$").fromNow());</script>
    $endif$
>>