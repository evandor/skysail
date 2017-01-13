bst_content(user,converter,messages,model,request)::= <<
<div class="container">
  <!--<div class="row" style="height:30px;">
    <div class="col-md-12">
      $contentHeader(converter, messages, model)$
    </div>
  </div>-->
  <div class="row">
    
      <div class="col-md-10" id="showFormDiv">
        <br>
		<form action="$model.formTarget$" method="POST" class="form-horizontal" role="form" id="form" name="form">
	
		  $model.formfieldsWrapper.formfields: { ff | $content/bst_formElement(ff)$ };separator="\n"$ 

          <div class="form-group">
	        <div class="col-sm-offset-2 col-sm-10">
	          <button type="submit" name="submit" value="submit" class="btn btn-default" >submit</button>&nbsp;
	        </div>
  	      </div>      
    	</form>      
    	
    	
    	
    	<script>

		var placeSearch, autocomplete;
		var componentForm = {
		  street_number: { type: 'short_name', htmlId: 'io_skysail_server_app_crm_addresses_Address_street_number' },
		  route:         { type: 'long_name',  htmlId: 'io_skysail_server_app_crm_addresses_Address_street' },
		  locality:      { type: 'long_name',  htmlId: 'io_skysail_server_app_crm_addresses_Address_city' },
		  administrative_area_level_1: 
		                 { type: 'short_name', htmlId: 'io_skysail_server_app_crm_addresses_Address_state' },
		  country:       { type: 'long_name',  htmlId: 'io_skysail_server_app_crm_addresses_Address_country' },
		  postal_code:   { type: 'short_name', htmlId: 'io_skysail_server_app_crm_addresses_Address_zip' }
		};

		function initAutocomplete() {
		  // Create the autocomplete object, restricting the search to geographical
		  // location types.
		  autocomplete = new google.maps.places.Autocomplete(
		      /** @type {!HTMLInputElement} */(document.getElementById('io_skysail_server_app_crm_addresses_Address_combined')),
		      {types: ['geocode']});
		
		  // When the user selects an address from the dropdown, populate the address
		  // fields in the form.
		  autocomplete.addListener('place_changed', fillInAddress);
		}

		// [START region_fillform]
		function fillInAddress() {
		  // Get the place details from the autocomplete object.
		  var place = autocomplete.getPlace();
		
		  for (var component in componentForm) {
		    document.getElementById(componentForm[component]['htmlId']).value = '';
		    document.getElementById(componentForm[component]['htmlId']).disabled = false;
		  }
		
		  // Get each component of the address from the place details
		  // and fill the corresponding field on the form.
		  for (var i = 0; i < place.address_components.length; i++) {
		    var addressType = place.address_components[i].types[0];
		    if (componentForm[addressType] && componentForm[addressType]['type']) {
		      var val = place.address_components[i][componentForm[addressType]['type']];
		      document.getElementById(componentForm[addressType]['htmlId']).value = val;
		    }
		  }
		}
		// [END region_fillform]

		// [START region_geolocation]
		// Bias the autocomplete object to the user's geographical location,
		// as supplied by the browser's 'navigator.geolocation' object.
		function geolocate() {
		  if (navigator.geolocation) {
		    navigator.geolocation.getCurrentPosition(function(position) {
		      var geolocation = {
		        lat: position.coords.latitude,
		        lng: position.coords.longitude
		      };
		      var circle = new google.maps.Circle({
		        center: geolocation,
		        radius: position.coords.accuracy
		      });
		      autocomplete.setBounds(circle.getBounds());
		    });
		  }
		}
		// [END region_geolocation]

    </script>


    <script src="https://maps.googleapis.com/maps/api/js?key=$first(model.data).googleApiKey$&libraries=places&callback=initAutocomplete" async defer></script>

  
  
    	
    	
    	
    	
    	
    	
      </div>
      <div class="col-md-2" id="renderEntityInfoDiv">
                <br>
                $renderEntityInfo(model.formfields, messages, model)$
      </div>
    
  </div>
  <div class="row">
  	<div class="col-md-12">
    	$menuNav(model.createFormLinks)$
  	</div>
  </div>
</div>
>>