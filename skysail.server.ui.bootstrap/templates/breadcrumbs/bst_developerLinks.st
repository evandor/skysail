bst_developerLinks(model) ::= <<
  <ul class="breadcrumb" style="text-align:right;margin-bottom:0px;padding:5px 20px;">
    <li>
      <select id="representations">
        $model.representations:{ representation | $breadcrumbs/bst_representationType(representation,i)$}$
      </select>&nbsp;<a href='#' onClick="(window.location = representations.options[representations.selectedIndex].value);">Export</a>
    </li>
    <li><a href="$model.rootPath$/raml.html" target="_API">API</a></li>
  </ul>
>>
