<%---- /*  Name 		: Manage APIs Details Page
 *  Description : Admin can view Api details from Gatewaymanagers
                  Admin can categorize,assign roles,eneble/disable APIs
 *  Middleware Provider : MW Database and WSO2
 *  Urls invoked :: 
 * 				view    : http://10.138.30.11:9846/GatewayBridgePlugin/rest/BridgeController/getApimgrData
 *				Modify 	: http://10.138.30.11:9846/GatewayBridgePlugin/rest/BridgeController/edit
 *	Developer  	: Suman Kumar Das 
  *	Created Date: 7th Oct 2017
 *	Modified Date: 
 *	Modified by  : 

*/ --%>


<%@page import="com.liferay.portal.kernel.util.WebKeys"%>
<%@page import="com.liferay.portal.kernel.theme.ThemeDisplay"%>
<%@page import="com.liferay.portal.kernel.service.RoleLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.model.Role"%>
<%@page import="java.util.List"%>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

<script>
	$(document).ready(function(){	
	$.ajax({
					method : "get",
					url : "http://wso2-predev-website.2f96.telkom.openshiftapps.com/GatewayBridgePlugin/rest/BridgeController/getApimgrData",
					type : 'json',
					 success: function(result) {
				alert('ok');
			  },
			  error: function(result) {
				alert('error');
				$('#api_service_apidetails').html("Error from Server");
			  }
				}).done(function(msg) {
				var data = "";
				for (var i = 0; i < msg.data.length; i++) {
					data+="<option value="+msg.data[i].API_MNGR_NAME+">"+msg.data[i].API_MNGR_NAME+"</option>";
					}
				$('#api-mgr-select').html($('#api-mgr-select').html()+data);
				});	
				
		$("#api-mgr-select").on("change",
			function() {			
		var option = this.value;
		var api_call_url;
		var api_details_call_url;
		if(option == "_none"){
			return false;			
		}
		if(option == "wso2"){
		   
			api_call_url = "http://wso2-predev-website.2f96.telkom.openshiftapps.com/WSO2GatewayPlugin/rest/WSO2PublisherService/getAllApis";
			api_details_call_url = "http://wso2-predev-website.2f96.telkom.openshiftapps.com/GatewayBridgePlugin/rest/BridgeController/getApiData";
		}else if(option == "apigee"){ 
			api_call_url = "http://wso2-predev-website.2f96.telkom.openshiftapps.com/WSO2GatewayPlugin/rest/WSO2PublisherService/getAllApis";
			api_details_call_url = "http://wso2-predev-website.2f96.telkom.openshiftapps.com/GatewayBridgePlugin/rest/BridgeController/getApiData";
		}else {
		
			api_call_url = "http://wso2-predev-website.2f96.telkom.openshiftapps.com/WSO2GatewayPlugin/rest/WSO2PublisherService/getAllApis";
			api_details_call_url = "http://wso2-predev-website.2f96.telkom.openshiftapps.com/GatewayBridgePlugin/rest/BridgeController/getApiData";
		}
				$.ajax({
					method : "get",
					url : api_call_url,
					type : 'json',
				}).done(
						function(msg) {
							var data = "";
							for (var i = 0; i < msg.apis.length; i++) {
								data += "<div class='sideOrder'><ul class='list-group sidenavlist'><li class='list-group-item'><a href='#' class='tab-link' data-tab='tab-1_1_1'>"+msg.apis[i].name+"</a><div class='version' style='display:none'>"+msg.apis[i].version+"</div></li></ul></div>"
							}
							$('#api_service_list').html(data);
							
							$("#api_service_list a").on("click",function(){							
									var clickedAPI = $(this).html();
									$.ajax({
										method : "get",
										contentType: 'application/json',
										//url : api_details_call_url+"?API_NAME="+clickedAPI+"&version="+$(this).parent().find(".version").html()+"&provider=master@mainapi.net",
										url : api_details_call_url+"?api="+clickedAPI+"&version="+$(this).parent().find(".version").html()+"&cache=false",		
										type : 'json',
									}).done(
											function(msg) {
												alert(clickedAPI+' Selected ..');
												var data = "";
												$.each(msg.data, function(i, field){
										                data += "<div class='row'><div class='col-sm-3'><b><h5>"+i+"</h5></b></div><div class='col-sm-9'>	<div class='centrecontent'><p class='para'>"+field+"</p></div></div></div></div>";
										            });
												$('#api_service_apidetails').html(data);
												$('.api-detail-container').show();
												//$('.user_heading .heading h4').html(clickedAPI);
													$('.api-detail-container .detailapipage h1.api-name').html(clickedAPI);
													$('.api-detail-container .detailapipage .apititle').html(data);
													if (typeof msg.CAT_IDS == "undefined") {
														alert('cat undefined');
														populateAPiCategory("");
														}
													else{
														alert('cat defined');
														alert(msg.CAT_IDS);
														populateAPiCategory(msg.CAT_IDS);
														}
											//Roles			
														if (typeof msg.ROLE_IDS == "undefined") {
														alert(' Role undefined');
														CheckAPiRoles("");
														}
													else{
														alert('Role defined');
														alert(msg.ROLE_IDS);
														CheckAPiRoles(msg.ROLE_IDS);
														}
													//populateAPiCategory();
													});
											  
											<!--category webservice call -->
											
												<!-------------------------------------->
									return false;
								});
						});
		});
		
		$(".menu-details").on("click",function(){
			var apiName = $(this).attr("value");
			$(".api_details").hide();
			$(".menu-details").removeClass("active");
			$("#"+apiName).show();
			$(this).addClass("active");
		});
		$("#add").click(function() {
		    $("#form").show();
		    return false;
		});
});		

function CheckAPiRoles(roleids){

$('#userroles input').prop('checked', '');


	if (roleids !="")
		{
		var rolearray = roleids.split(',');
		for (var i = 0; i < rolearray.length; i++) {
			alert('zxczxczxczx'+rolearray[i]);
			alert('#role'+rolearray[i]);
			$('#role'+rolearray[i]).prop('checked', 'checked');
			
			}
		}
	}

function populateAPiCategory(categoryids){	
		$.ajax({
			method : "get",
			url : "http://wso2-predev-website.2f96.telkom.openshiftapps.com/GatewayBridgePlugin/rest/BridgeController/getApicategoryData",
			type : 'json',
		}).done(
			function(message){
				//$("#api_manageapi tbody").remove(); 
				var datacontent = "";
				if(message.error == "false")
				{
				alert("if");
					for (var i = 0; i < message.data.length; i++) {
					
					datacontent +="<label><input id='cat"+message.data[i].CAT_ID+"' class='checkebox' type='checkbox' value='"+message.data[i].CAT_ID+"'>"+message.data[i].CAT_NAME+"</label>";
								 
					}
				}else
				{
				alert("else");
				  datacontent +="Error from Server ...";
				}
				datacontent += "";
				$("#APIcategories").html(datacontent); 
				
				if (categoryids !="")
				{
				var catarray = categoryids.split(',');
				for (var i = 0; i < catarray.length; i++) {
					alert('zxczxczxczx'+catarray[i]);
					alert('#cat'+catarray[i]);
					$('#cat'+catarray[i]).prop('checked', 'checked');
					
					}
				}
			}); 
			
			
			
				}		
			
			
</script>

<%@page import="com.liferay.portal.kernel.json.JSONObject"%>
<Style>
#sidebarnav {
    height: auto;
    width: 100%;
    overflow-x: hidden;
    color: red;
    padding: 0 15px;
    border-right: 2px solid red;
    border-left: 2px solid red;
    border-bottom: 2px solid red;
    border-top: 2px solid red;
}

.sidebarTitle {
	font-size: 1.3em;
	text-align: left;
	font-weight: 500;
	padding: 5px;
	background-color: red;
	color: white;
}

.sideOrder {
	margin-top: 10px;
	margin-bottom: 10px;
}

.sidenavlist {
	border: none;
	background: none;
	padding: 2px 15px;
	text-transform: capitalize;
}

.sidesubtitle {
	text-transform: uppercase;
}

.sidenavlist {
	border-bottom: 2px solid darkgrey;
	margin-bottom: 10px;
}

.sidenavlist li a {
	color: black;
	font-size: 13px;
}

.sideSetting .sidenavlist {
	border-bottom: none;
}

/*--iphone6 plus--*/
@media only screen and (min-device-width : 414px) and (max-device-width
	: 767px) and (orientation : portrait) {
	#sidebarnav {
		width: 50%;
	}
}
/* iPads (portrait) ----------- */
@media only screen and (min-width : 768px) and (max-width : 1024px) and
	(orientation : portrait) {
	#sidebarnav {
		width: 25%;
	}
}

.api_list {
	margin-top: 35px;
}

.api_services {
	margin-top: 20px;
}

.user_heading {
	border-top: 2px solid #E62037;
	border-bottom: 2px solid #E62037;
	background-color: #E62037;
	height: 30px;
}

.heading {
	padding-top: 2px;
	padding-left: 10px;
	color: white;
}

.api_section {
	margin-top: 35px;
}

.api_details {
	margin-top: 10px;
}

.section_details {
	margin-top: 20px;
}

.menu-bar li {
	display: inline-block;
	background: red;
	color: white;
}

.menu-bar a {
	color: white;
}

.menu-details {
	border: 2px solid red;
	font-size: 20px;
	padding: 0px 27px 0px 32px;
	margin-left: -5px;
}

.edit {
	margin-left: 20px;
}

.btn {
	margin-left: 0px !important;
}

.btn-danger {
	background-color: #E62037;
}

th {
	background-color: #E62037;
	color: white;
}

.tabs {
	width: 100%;
	display: inline-block;
}

#categorydetails .btn-danger {
	margin-top: 20px;
}

.menu-details {
	background-color: #E62037;
}

.nav-tabs .section_details {
	color: white;
}

#pricedetails .price_details {
	margin-bottom: 25px;
}

.form-control {
	margin-bottom: 15px;
}

.checkbox, .radio {
	margin-left: 15px;
}

.category {
	margin-top: 20px;
}
.checkbox-inline {
	width: 20%
}

.apititle p {
	word-wrap: break-word;
}

body .loading:after {
	/* with no content, nothing is rendered */
	content: "";
	position: fixed;
	/* element stretched to cover during rotation an aspect ratio up to 1/10 */
	top: -500%;
	left: -500%;
	right: -500%;
	bottom: -500%;
	z-index: 9999;
	pointer-events: none; /* to block content use: all */
	/* background */
	background-color: rgba(0,0,0,0.6);
	background-image: url(data:image/png;base64,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);
	background-repeat: no-repeat;
	background-position: center center;
	background-size: 100px 100px;

	/* animation */
	-webkit-animation-name: linearRotate;
	-webkit-animation-duration: 1s;
	-webkit-animation-iteration-count: infinite;
	-webkit-animation-timing-function: linear;

	-moz-animation-name: linearRotate;
	-moz-animation-duration: 1s;
	-moz-animation-iteration-count: infinite;
	-moz-animation-timing-function: linear;

	-o-animation-name: linearRotate;
	-o-animation-duration: 1s;
	-o-animation-iteration-count: infinite;
	-o-animation-timing-function: linear;

	animation-name: linearRotate;
	animation-duration: 1s;
	animation-iteration-count: infinite;
	animation-timing-function: linear;

}

@-webkit-keyframes linearRotate {
	from {
		-webkit-transform: rotate(0deg);
		-moz-transform: rotate(0deg);
		-o-transform:rotate(0deg);
		transform: rotate(0deg);
	}
	to {
		-webkit-transform: rotate(360deg);
		-moz-transform: rotate(360deg);
		-o-transform: rotate(360deg);
		transform: rotate(360deg);
	}
}

.row {
	margin-right: 0px;
	margin-left: 0px;
}

.panel-collapse {
	padding-left: 15px;
}

input[type=checkbox], input[type=radio] {
	margin: 5px 5px 0;
}

.checkbox input[type=checkbox], .checkbox-inline input[type=checkbox],
	.radio input[type=radio], .radio-inline input[type=radio] {
	left: -5px;
}

.checkbox input[type="checkbox"], .checkbox-inline input[type="checkbox"]
	{
	top: 33%!;
}
.btn {
    border: 2px solid;
    color: black;
    padding: 2px 19px;
    font-size: 15px;
    cursor: pointer;
    border-color: #E62037;
    font-weight: bold;
}
.danger {
    background-color: white;
    height: 37px;
}
.danger:hover {
    background: #E62037;
}
.btn.focus, .btn:focus, .btn:hover {
    color: #fff;
    text-decoration: none;
}
.h1, h1 {
    font-size: 34px;
    text-align: left;
    color: black;
    padding: 5px;
}
.apititle p {
    word-wrap: break-word;
    font-size: 15px;
}

 .bordertext {
    text-align: center;
    width: 150px;
    background-color: #ff2929;
    padding: 8px 0;}
a {
    color: white;}
.registrationLogin #accordion a, a:focus, a:active {
    outline: 0;
    color: #fff ;
    text-decoration: none !important;
}
.panel-group {
    margin-bottom: 20px !important;
}

.collapse.in {
    display: block;
}
.apititle {
    border: 1px solid red;
	padding: 24px;
	background-color: #efeded;
}
.panel-collapse {
    padding-left: 0px;
}
#APIcategories{
	border: 1px solid red;
	padding: 24px;
	background-color: #efeded;
}
#collapse2{
	border: 1px solid red;
	padding: 24px;
	background-color: #efeded;
}
#APIcategories label {
    width: 33%;
    font-weight: normal !important;
    color: black !important;
}
#collapse2 label {
    font-weight: normal !important;
    color: black !important;
}
 b, strong {
    font-weight: bold !important;
    color: black !important;
}
.apilist{
	    margin-top: 20px;
}
</style>
<div id="loadingimage" class="loading"></div>
<div class="row">
	<div class="col-sm-4">
		<div class="api_list">
			<nav id="sidebarnav">
				<form class="md-form" action="" method="">
					<select class="form-control" id="api-mgr-select">
						<option value="_none">--Select API--</option>
					</select>
				</form>

				<div class="api_services"></div>
				<h2 class="sidebarTitle">API Services</h2>
				<div id="api_service_list"></div>
			</nav>
		</div>
	</div>
	<div class="api-detail-container col-sm-8" style="display: none;">
		<!-- ---------------------------------------------------- -->
		<!-- Header -->
		<div class="pagesection detailapipage">
			<div class="registrationLogin">
				<div class="">
					<div class="clearfix"></div>
					<h1 class="api-name">API Name</h1>
					<div class="apilist">
						<div class="apilistcontenor">
							<div class="panel-group" id="accordion" role="tablist"
								aria-multiselectable="true">

								<div class="panel panel-default">
									<div class="panel-heading" role="tab" id="headingOne">
										<h4 class="panel-title">
										<div class="bordertext">
										<a class="firstlink" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne"> API DETAILS </a>
										</div>
										</h4>
								</div>
									<div id="collapseOne" class="panel-collapse collapse"
										role="tabpanel" aria-labelledby="headingOne">
										<div class="apibody">
											<div class="apititle"></div>
										</div>
									</div>
								</div>

								<div class="category">
									<div class="panel-group">
										<div class="panel panel-default">
											<div class="panel-heading">
												<h4 class="panel-title">
												<div class="bordertext">
													<a data-toggle="collapse" href="#collapse1">CATEGORY</a>
												</div>
												</h4>
											</div>
											<div id="collapse1" class="panel-collapse collapse">
												<div id="APIcategories"></div>

											</div>
										</div>
									</div>
								</div>
								<div class="user role">
									<div class="panel-group">
										<div class="panel panel-default">
											<div class="panel-heading">
												<h4 class="panel-title">
												<div class="bordertext">
													<a data-toggle="collapse" href="#collapse2">USER ROLE</a>
												</div>
												</h4>
											</div>
											<div id="collapse2" class="panel-collapse collapse">
											<div id="userroles">
												<%
													ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
													List<Role> userRoles = RoleLocalServiceUtil.getUserRoles(themeDisplay.getUserId());
													List<Role> userRoles1 = themeDisplay.getUser().getRoles();
													for (Role role : userRoles) {
												%>
												<div class="checkbox-inline">
													<label><input type="checkbox" id="role<%=role.getRoleId()%>"
														value=<%=role.getRoleId()%>><%=role.getName()%></label>
												</div>
												<%
													}
												%>
												</div>
											</div>
										</div>
									</div>
								</div>
								<div id="checkpublish">
									<div class="checkbox">
										<label><input type="checkbox" value="">
											<h5>
												<b>PUBLISH</b>
											</h5></label>
									</div>
								</div>
								<div class="button">
									<button class="btn danger" id="submit">Submit</button>
								</div>





							</div>
							<!-- panel-group -->


						</div>
						<!-- container -->
					</div>
				</div>


			</div>
		</div>

		<!-- ---------------------------------------------------- -->





	</div>
</div>
</div>
<script>
$("#submit").on("click",function(){
	var json = '{"datatype":"TelkomApiData",';
    var data = '{';
    var count = jQuery(".apilist .apititle .row").length;
    jQuery(".apilist .apititle .row").each(function() {
           var id = jQuery(this).find("h5").html();
           var val = jQuery(this).find(".para").html().replace(/\r?\n|\r/g,'\\n');           
           data += '"' + id + '"' + ':"' + val + '"';
           if (count > 1)
                  data += ',';
           count--;
    });
    json += '"data":' + data;
    json += '},"CAT_IDS":"';
    count = jQuery("#APIcategories input:checked").length;
    jQuery("#APIcategories input:checked").each(function() {
           json += jQuery(this).val();
           if (count > 1)
                  json += ',';
           count--;
    });
    json += '","Role_IDS":"';
    count = jQuery(".user.role input:checked").length;
    jQuery(".user.role input:checked").each(function() {
           json += jQuery(this).val();
           if (count > 1)
                  json += ',';
           count--;
    });
   json+='","Published":"'+jQuery("#checkpublish input").prop("checked")+'"';
    json += '}';

    console.log(json);
			
			$.ajax({
			  type: "POST",
			  url: "http://wso2-predev-website.2f96.telkom.openshiftapps.com/GatewayBridgePlugin/rest/BridgeController/add",
			  contentType: 'application/json',
			  data: json,
			  dataType: "json",
			  beforeSend: function(request) {
				    request.setRequestHeader("Access-Control-Allow-Origin", "*");
				  }, 
			  success: function(result) {
				   alert('saved succesfully'); 
			  },
			  error: function(result) {
			  console.log(result);
				alert('Error ! Try Again');
			  }
			});
			});
$(document).ajaxStart(function () {
    $('#loadingimage').addClass("loading");
  }).ajaxStop(function () {
    $('#loadingimage').removeClass("loading");
  });

</script>

