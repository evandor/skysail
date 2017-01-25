contentHeader(converter, messages, model) ::= <<
  $if(converter.edit)$
    <a href='/i18n/v1/messages/$model.resourceClassName$.message/'>$if(messages.("content.header").value)$$messages.("content.header").value$$else$___$endif$</a>
  $else$
$messages.("content.header").value$
  $endif$
>>