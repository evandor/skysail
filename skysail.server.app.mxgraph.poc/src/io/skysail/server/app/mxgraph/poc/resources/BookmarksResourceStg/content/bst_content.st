bst_content(user,converter,messages,model,request)::= <<
  <div class="container">
    <div class="row" style="height:30px;">
      <div class="col-md-12">
        <sky-content identifier="$model.resourceClassName$.message" content="$messages.("content.header").value$" $if(converter.edit)$editable$endif$ />
      </div>
    </div>
  <div class="row">
    $if(model.form)$

      <div class="col-md-10" id="showFormDiv">
        <br>
		<form action="$model.formTarget$" method="POST" class="form-horizontal" role="form" id="form" name="form">
		  $model.formfieldsWrapper.formfields: { ff | $content/bst_formElement(model,converter,ff)$ };separator="\n"$ 
          <div class="form-group">
	        <div class="col-sm-offset-2 col-sm-10">
	          <button type="submit" name="submit" value="submit" class="btn btn-default" >submit</button>&nbsp;
	        </div>
  	      </div>      
    	</form>      
      </div>
      <div class="col-md-2" id="renderEntityInfoDiv">
        <br>
        $renderEntityInfo(model.formfields, messages, model)$
      </div>

    $elseif(model.relationTargetList)$
 
      <div class="col-md-12" id="showRelationTargetFormDiv">
        <br>
        $showRelationTargetForm(model.formfields, messages, model)$
      </div>
 
    $elseif(model.list)$
 
      <div class="col-md-12" id="renderTableDiv">
        ***
        
      </div>
 
    $else$
      <div class="col-md-12" id="renderEntityDiv">
        +++
            <script type="text/javascript">
                mxBasePath = '../mxgraphPOC/v1/';
            </script>
            <script type="text/javascript">
                mxImageBasePath = '/path/to/image/directory';
            </script>
            <script type="text/javascript" src="v1/js/mxClient.js"></script>
            <script type="text/javascript" src="v1/v1/js/mxApplication.js"></script>
            <script type="text/javascript">
                mxGraph.prototype.htmlLabels = true;
            
                mxGraph.prototype.isWrapping = function(cell)
                {
                    return true;
                };
                
                mxConstants.DEFAULT_HOTSPOT = 1;
                
                // Enables guides
                mxGraphHandler.prototype.guidesEnabled = true;
                
                // Alt disables guides
                mxGuide.prototype.isEnabledForEvent = function(evt)
                {
                    return !mxEvent.isAltDown(evt);
                };
                
                // Enables snapping waypoints to terminals
                mxEdgeHandler.prototype.snapToTerminals = true;
                
                window.onbeforeunload = function() { return mxResources.get('changesLost'); };
                                
            </script>

        
        <table id="splash" width="100%" height="100%"
            style="background:white;position:absolute;top:0px;left:0px;z-index:4;">
            <tr>
                <td align="center" valign="middle">
                    <img src="v1/images/loading.gif">
                </td>
            </tr>
        </table>
        <div id="graph" class="base">
            <!-- Graph Here -->
        </div>
        <div id="status" class="base" align="right" style="white-space:nowrap;">
            <!-- Status Here -->
        </div>
        
        
        
        
        
        
        
        
        
        
      </div>
    $endif$
  </div>
</div>
>>