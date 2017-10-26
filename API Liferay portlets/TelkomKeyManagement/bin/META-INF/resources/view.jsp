<html><body>
<!-- Telkom Key Managment View
   @author Arnab Chakraborty
   Created on: 20th August 2017  
-->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<!-- CSS Code -->
<style>
.user_heading{
	border-top : 2px solid #E62037;
	border-bottom : 2px solid #E62037;
	background-color: #E62037;
	height: 33px;
}

.heading{
	padding-top:2px;
	padding-left:15px;
	color:white;
	font-size:15px;
}

.keytable{
	margin-top:20px;
}

.order_list{
	margin-top:-6px;
}

.theader{
	font-weight:bold;
	background: #a1d4ff;
}

.thbody{
	background:#c0e2ff;
}

</style>
<div id="loadingimage" class="loading"></div>
<div class="order_list">
	<div class="row">
		<div class="col-sm-12">
			<div class="user_heading">
				<div class="heading">
					<ul>
						<li><h4>Key Management</h4></li>
					</ul>
				</div>
			</div>
		</div>
		<div class="col-sm-12 keytable">
		   <table class="table table-bordered">
				<tbody id="token_info">
				</tbody>
			</table>
		   
		</div>
	</div>
</div>
<script>
	$(document).ready(function(){
	//alert(1);
	var data = "";
	data += "<tr><td class='thbody'>Client Id</td><td class='thbody' id='token_type'>uSvipu7NDya7eIwynLFyyGLnCkIa</td></tr>";
	data += "<tr><td class='thbody'>Client Secret</td><td class='thbody' id='token_type'>UArgfMBm9tvCYjy_a3ZU3lw6zMUa</td></tr>";
		fetchTokenInfo(data);		
			
		});
		function fetchTokenInfo(datacontent)
		{
		$.ajax({		
			method : "get",
			url : "http://wso2-predev-website.2f96.telkom.openshiftapps.com/GatewayBridgePlugin/rest/BridgeController/getAccessCode?clientId=uSvipu7NDya7eIwynLFyyGLnCkIa&clientSecret=UArgfMBm9tvCYjy_a3ZU3lw6zMUa&userid=mediation.hub@mainapi.net",
			type : 'json',
				success: function(result) {
				//alert('Success');
				},
				error: function(result) {
				//alert('error');
				$('#token_info').html("Error from Server");
				}
			}).done(function(message) {
				//var datacontent = message.data.access_token;
				alert(message.data.access_token);
				datacontent += "<tr><td class='thbody'>Access Token</td><td class='thbody' id='token_type'>"+message.data.access_token+"</td></tr>";
				datacontent += "<tr><td class='thbody'>Scope</td><td class='thbody' id='token_type'>"+message.data.scope+"</td></tr>";
				datacontent += "<tr><td class='thbody'>Token Type</td><td class='thbody' id='token_type'>"+message.data.token_type+"</td></tr>";
				datacontent += "<tr><td class='thbody'>Expires In</td><td class='thbody' id='token_type'>"+message.data.expires_in+"</td></tr>";
				//$('#access_token').html($('#access_token').html()+data);
				$('#token_info').html(datacontent);
				});
		}
		
</script>
<script>
$(document).ajaxStart(function () {
    $('#loading').addClass("loading");
  }).ajaxStop(function () {
    $('#loadingimage').removeClass("loading");
  });
		  
   
	   
    </script>  

</body></html>
