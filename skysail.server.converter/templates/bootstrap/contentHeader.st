contentHeader(converter, messages, model) ::= <<
  $if(converter.edit)$
    <a href='/i18n/v1/messages/$model.resourceClassName$.message/'>$if(messages.("content.header").translated)$$messages.("content.header").translated$$else$___$endif$</a>
  $else$
    $if(messages.("content.header").translated)$
      $messages.("content.header").translated$
    $else$
      <h3>$model.title$</h3>
    $endif$
  $endif$
>>