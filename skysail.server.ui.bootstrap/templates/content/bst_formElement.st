bst_formElement(model,converter,ff) ::= <<
  $if(ff.polymerInputType)$
    <div class='form-group'>
      <label for='$ff.id$' class='col-sm-2 control-label'><sky-content identifier="$ff.name$" content="$ff.label$" $if(converter.edit)$ editable$endif$></sky-content></label>
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
        info-msg="$ff.infoMsg$"
  		placeholder="$ff.placeholder$"
  		css-class="$ff.cssClass$"
        css-style="$ff.cssStyle$"
  		$if(ff.mandatory)$ required$endif$
  		$if(converter.edit)$ editable$endif$>
  	</sky-form-element>
  $endif$
>>