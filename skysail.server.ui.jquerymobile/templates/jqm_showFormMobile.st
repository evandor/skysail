jqm_showFormMobile(fields, messages, model) ::= <<

  $if (model.constraintViolationsResponse && model.classlevelViolationMessage)$
    Sorry, there was at least one problem submitting this form:
    <div class="alert alert-danger" role="alert">
      <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
      <span class="sr-only">Error:</span>
        $model.classlevelViolationMessage$
    </div>
  $endif$

  $if (model.postEntityServerResource)$
    <form action="$model.formTarget$" method="POST" class="pure-form pure-form-stacked" role="form" id="form" name="form">
      <fieldset>
        $handleField(first(model.data), first(model.formfields), messages,"1", " autofocus")$
        $rest(model.formfields):{ field | $if(field.mandatory)$$handleField(first(model.data), field, messages,i, "")$$endif$}$
        <div data-role="collapsible" data-mini="true">
            <h4>Details...</h4>
            $rest(model.formfields):{ field | $if(!field.mandatory)$$handleField(first(model.data), field, messages,i, "")$$endif$}$
        </div>
        $if (model.submitButtonNeeded)$
          $submitPost("Submit","2","10",length(model.formfields))$
        $endif$
      </fieldset>
    </form>
  $elseif (model.putEntityServerResource)$
    <form action="$model.formTarget$?method=PUT" method="POST" class="pure-form pure-form-stacked" role="form" id="form" name="form">
      <fieldset>
        $model.formfields:{ field | $handleField(first(model.data), field, messages,i,"")$}$
        $if (model.submitButtonNeeded)$
          $submitPut("Update","1","9",length(model.formfields))$
        $endif$
      </fieldset>
    </form>
  $endif$

>>

submitPost(label, o1,o2,tabindex) ::= <<
      <div class="form-group">
        <div class="col-sm-offset-$o1$ col-sm-$o2$">
          <button type="submit" name="submit" value="submit" class="pure-button pure-button-primary" tabindex="$tabindex$">$label$</button>&nbsp;
         <!--  <button type="submit" name="submit" value="submitAndNew" class="pure-button pure-button-primary" tabindex="$tabindex$">$label$ and new</button>-->
        </div>
      </div>
>>

submitPut(label, o1,o2,tabindex) ::= <<
      <div class="form-group">
        <div class="col-sm-offset-$o1$ col-sm-$o2$">
          <button type="submit" id="submitButton" name="submitButton" class="pure-button pure-button-primary" tabindex="$tabindex$">$label$</button>&nbsp;
        </div>
      </div>
>>

handleField(data, field, messages,i, autofocus) ::= <<
	$if (field.violationMessage)$
        $handleConstraintViolationField(data, field, messages,i,autofocus)$
    $else$
        $handleInputField(data, field, messages,i, autofocus)$
    $endif$
>>

handleConstraintViolationField(formField, messages,i,autofocus) ::= <<

    $if(formField.tagsInputType)$
	    <div class='form-group has-error has-feedback'>
	      $label("s2id_", formField)$
	      <div class='col-sm-10'>
	        <input type = 'hidden' name = '$formField.name$' id = '$formField.name$' value='$formField.value$'  tabindex="$i$"/>
	        <span class='glyphicon glyphicon-remove form-control-feedback' aria-hidden='true'></span>
	        <small style='' class='help-block' data-bv-validator='notEmpty' data-bv-for='title' data-bv-result='INVALID'>
	            $formField.violationMessage$
	        </small>
            $renderDescription(field,converter)$
	      </div>
	    </div>
    $elseif(field.selectionProvider)$
        <div class='form-group'>
          $label("", formField)$
          <div class='col-sm-10'>
            <select name='$field.id$' tabindex="$i$">
                $field.selectionProviderOptions.keys: {key | $options(field.selectionProviderOptions,key)$}$
            </select>
            $renderDescription(field,converter)$
          </div>
        </div>
    $else$
	    <div class='form-group has-error has-feedback'>
	      $label("", formField)$
	      <div class='col-sm-10'>
	        <input type="$formField.inputType$" class='form-control' name='$formField.name$' placeholder='' value='$formField.value$' tabindex="$i$">
	        <span class='glyphicon glyphicon-remove form-control-feedback' aria-hidden='true'></span>
	        <small style='' class='help-block' data-bv-validator='notEmpty' data-bv-for='title' data-bv-result='INVALID'>
	            $formField.violationMessage$
	        </small>
            $renderDescription(field,converter)$
	      </div>
	    </div>
    $endif$
>>

handleInputField(data, field, messages,i,autofocus) ::= <<
  $if(!field.readonlyInputType)$
      $if(field.tagsInputType)$
        <div class='form-group'>
          <label for='s2id_$field.id$' class='col-sm-2 control-label' style="text-align: left;">$helpmessage(messages, field.nameKey)$ $if(converter.edit)$<a href='/i18n/v1/messages/$data.entity.entity.class.name$.$field.id$/'><span class="glyphicon glyphicon-edit" aria-hidden="true"></span></a>$endif$</label>
          <div class='col-sm-10'>
            <input type = 'hidden' name = '$field.id$' id = '$field.id$' value='$data.(field.id)$' tabindex="$i$" $autofocus$/>
            $renderDescription(field,converter)$
          </div>
        </div>
      $elseif(field.textareaInputType)$
        <div class='form-group'>
          <label for='$field.id$' class='col-sm-2 control-label' style="text-align: left;">$helpmessage(messages, field.nameKey)$ $if(converter.edit)$<a href='/i18n/v1/messages/$data.entity.entity.class.name$.$field.id$/'><span class="glyphicon glyphicon-edit" aria-hidden="true"></span></a>$endif$</label>
          <div class='col-sm-10'>
            <textarea class='form-control' rows=3 name='$field.id$' id='$field.id$' tabindex="$i$" $autofocus$>$data.(field.id)$</textarea>
            $renderDescription(field,converter)$
          </div>
        </div>
      $elseif(field.markdownEditorInputType)$
        <div class='form-group'>
          <label for='$field.id$' class='col-sm-2 control-label' style="text-align: left;">$helpmessage(messages, field.nameKey)$  $if(converter.edit)$<a href='/i18n/v1/messages/$data.entity.entity.class.name$.$field.id$/'><span class="glyphicon glyphicon-edit" aria-hidden="true"></span></a>$endif$</label>
          <div class='col-sm-10'>
            <textarea data-uk-htmleditor="{maxsplitsize:600, markdown:true}" name='$field.id$' id='$field.id$' tabindex="$i$" $autofocus$>$data.(field.id)$</textarea>
            $renderDescription(field,converter)$
          </div>
        </div>
      $elseif(field.selectionProvider && !field.submitField)$
        <div class='form-group'>
          <label for='$field.id$' class='col-sm-2 control-label' style="text-align: left;">$helpmessage(messages, field.nameKey)$</label>
          <div class='col-sm-10'>
            <select name='$field.id$' tabindex="$i$"  $autofocus$>
                $field.selectionProviderOptions: {option | $options(option)$}$
            </select>
            $renderDescription(field,converter)$
          </div>
        </div>
      $elseif(field.selectionProvider && field.submitField)$
        <div class='form-group'>
          <label for='$field.id$' class='col-sm-2 control-label' style="text-align: left;">$helpmessage(messages, field.nameKey)$  $if(converter.edit)$<a href='/i18n/v1/messages/$data.entity.entity.class.name$.$field.id$/'><span class="glyphicon glyphicon-edit" aria-hidden="true"></span></a>$endif$</label>
          <div class='col-sm-10'>
            $field.selectionProviderOptions: {option | $buttons(option, field)$}$
            $renderDescription(field,converter)$
          </div>
        </div>
      $elseif(field.checkbox)$
        <div class='form-group'>
          <label for='$field.id$' class='col-sm-2 control-label' style="text-align: left;">$helpmessage(messages, field.nameKey)$  $if(converter.edit)$<a href='/i18n/v1/messages/$data.entity.entity.class.name$.$field.id$/'><span class="glyphicon glyphicon-edit" aria-hidden="true"></span></a>$endif$</label>
          <div class='col-sm-10'>
            <input type="$field.inputType$" class='form-control' name='$field.id$' id='$field.id$' $if(field.value)$checked$endif$ tabindex="$i$" title="$helpmessage(messages, field.placeholderKey)$">
            $renderDescription(field,converter)$
          </div>
        </div>
      $elseif(field.date)$
        <div class='form-group'>
          <label for='$field.id$' class='col-sm-2 control-label' style="text-align: left;">$helpmessage(messages, field.nameKey)$  $if(converter.edit)$<a href='/i18n/v1/messages/$data.entity.entity.class.name$.$field.id$/'><span class="glyphicon glyphicon-edit" aria-hidden="true"></span></a>$endif$</label>
          <div class='col-sm-10'>
            <input type="date" class='form-control' name='$field.id$' id='$field.id$' tabindex="$i$" title="$helpmessage(messages, field.placeholderKey)$" value='$data.(field.id)$' style="width:190px;">
            $renderDescription(field,converter)$
          </div>
        </div>
      $elseif(field.range)$
        <div class='form-group'>
          <label for='$field.id$' class='col-sm-2 control-label' style="text-align: left;">$helpmessage(messages, field.nameKey)$  $if(converter.edit)$<a href='/i18n/v1/messages/$data.entity.entity.class.name$.$field.id$/'><span class="glyphicon glyphicon-edit" aria-hidden="true"></span></a>$endif$</label>
          <div class='col-sm-10'>
            <input type="range" name='$field.id$' id='$field.id$' tabindex="$i$" value='$data.(field.id)$' min=$field.rangeMin$ max=$field.rangeMax$ step=1 data-highlight="true">
            $renderDescription(field,converter)$
          </div>
        </div>
      $else$
          <input type="$field.inputType$" name='$field.id$' id='$field.id$' placeholder='$helpmessage(messages, field.nameKey)$' value='$data.(field.id)$' tabindex="$i$" $autofocus$ title="$helpmessage(messages, field.placeholderKey)$">
          $renderDescription(field,converter)$
      $endif$
  $endif$
>>

renderDescription(field,converter) ::= <<
  $if(messages.(field.messageKey))$
    <small class="help-block">_$helpmessage(messages, field.messageKey)$ $editTextLink(field,converter)$</small>
  $else$
    $editTextLink(field,converter)$
  $endif$
>>

editTextLink(field,converter) ::= <<>>

label(prefix, formField) ::= <<
      <label for='$prefix$$formField.name$' class='col-sm-2 control-label' style="text-align: left;">$helpmessage(messages, field.nameKey)$</label>
>>

helpmessage(messages, identifier) ::= <<$messages.(identifier).translated$>>

options(option) ::= <<
    <option value="$option.key$"$if(option.selected)$ selected$endif$>$option.value$</option>
>>

buttons(option, field) ::= <<
    <input type="submit" name="$field.id$" class="ui-btn $if(option.selected)$ui-btn-active$endif$" value="$option.value$">&nbsp;
>>

title(messages, ident) ::= <<
    $helpmessage(messages, ident).translated$
>>