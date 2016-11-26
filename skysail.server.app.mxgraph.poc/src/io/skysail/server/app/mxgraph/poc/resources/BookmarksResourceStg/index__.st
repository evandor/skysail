index(user, messages, converter, model) ::= <<
<!--
  Copyright (c) 2006-2013, JGraph Ltd
  
  Hello, World! example for mxGraph. This example demonstrates using
  a DOM node to create a graph and adding vertices and edges.
-->
<!DOCTYPE html>
<html lang="en">
  <head>

    $head()$

	<style type="text/css" media="screen">
		div.base {
			position: absolute;
			overflow: hidden;
			font-family: Arial;
			font-size: 8pt;
		}
		div.base#graph {
			border-style: solid;
			border-color: #F2F2F2;
			border-width: 1px;
			background: url('images/grid.gif');
		}
	</style>
	<script type="text/javascript">
		mxBasePath = './v1/examples/editors';
	</script>
	<script type="text/javascript" src="./v1/js/mxClient.js"></script>
	<script type="text/javascript" src="./v1/examples/editors/js/mxApplication.js"></script>
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
</head>

<body onload="mxApplication('config/workfloweditor.xml');">

    $navbar(user, messages, converter, model)$

	<div class="container-fluid" style="padding-top:50px;">
		<table id="splash" width="100%" height="100%"
		style="background:white;position:absolute;top:0px;left:0px;z-index:4;">
		<tr>
			<td align="center" valign="middle">
				<img src="images/loading.gif">
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
	
	$footer(converter)$

    <!-- Bootstrap core JavaScript ================================================== -->
    <!-- Placed at the end of the document so the pages load faster 
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script>window.jQuery || document.write('<script src="../../assets/js/vendor/jquery.min.js"><\/script>')</script>-->

    <script src="/webapp/js/bootstrap.min.js"></script>
    
    $peity()$
    <script type="text/javascript">
        \$(".bar").peity("bar");

        \$(function () {
              \$('[data-toggle="tooltip"]').tooltip()
        });

      \$('.editable').jinplace();
    </script>

  </body>
</html>


>>