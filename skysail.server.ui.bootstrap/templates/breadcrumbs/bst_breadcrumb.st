bst_breadcrumb(model) ::= <<
  <ol class="breadcrumb">
    $model.breadcrumbs:{ breadcrumb | $breadcrumbs/bst_handleSingleBreadcrumb(breadcrumb,i)$}$
  </ol>
>>