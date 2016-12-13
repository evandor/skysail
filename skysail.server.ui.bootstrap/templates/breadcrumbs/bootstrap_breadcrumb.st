bootstrap_breadcrumb(model) ::= <<
  <ol class="breadcrumb">
    $model.breadcrumbs:{ breadcrumb | $breadcrumbs/bootstrap_handleSingleBreadcrumb(breadcrumb,i)$}$
  </ol>
>>