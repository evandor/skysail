bst_formElement(model,ff) ::= <<
  $if(ff.polymerInputType)$
    <div class='form-group'>
      <label for='$ff.id$' class='col-sm-2 control-label'>$ff.label$</label>
      <div class='col-sm-10'>
        $ff.renderedPolymer$
      </div>
    </div>
  $else$
    <sky-form-element 
  		label="$ff.label$" 
  		name="$ff.name$" 
  		id="$ff.id$" 
  		input-type="$ff.inputType$" 
  		value="$first(model.data).(ff.name)$" 
  		relation-path="$ff.fieldRelation.path$" 
  		help-msg="$ff.helpMsg$"
  		placeholder="$ff.placeholder$">
  	</sky-form-element>
  $endif$
>>