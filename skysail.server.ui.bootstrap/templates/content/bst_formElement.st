bst_formElement(model,converter,ff) ::= <<
  $if(ff.polymerInputType)$
    <div class='form-group'>
      <label for='$ff.id$' class='col-sm-4 control-label'><sky-content identifier="$ff.name$" content="$ff.label$" edit-base-url='$converter.editBaseUrl$'></sky-content></label>
      <div class='col-sm-12'>
        $ff.renderedPolymer$
      </div>
    </div>
  $else$
    <!-- $ff.name$: $first(model.data)$ -->
    <sky-form-element 
        label="$ff.label$"
        name="$ff.name$"
        id="$ff.id$"
        input-type="$ff.inputType$"
        value="$first(model.data).(ff.name)$"
        $if(ff.fieldRelation.path)$relation-path="$ff.fieldRelation.path$" $endif$
        $if(ff.helpMsg)$help-msg="$ff.helpMsg$" $endif$
        $if(ff.infoMsg)$info-msg="$ff.infoMsg$" $endif$
        $if(ff.violationMessage)$error-msg="$ff.violationMessage$" $endif$
        $if(ff.placeholder)$placeholder="$ff.placeholder$" $endif$
        $if(ff.cssClass)$css-class="$ff.cssClass$" $endif$
        $if(ff.cssStyle)$css-style="$ff.cssStyle$" $endif$
        $if(ff.mandatory)$ required $endif$
        edit-base-url='$converter.editBaseUrl$'>
    </sky-form-element>
  $endif$
>>