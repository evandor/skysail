contentHeader(converter, messages, model) ::= <<
  $if(converter.edit)$
    <a href='/i18n/v1/messages/$model.resourceClassName$.message/'>$messages.("content.header").value$</a>
  $else$
$messages.("content.header").value$
  $endif$
>>