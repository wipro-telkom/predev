<%---- 
/*  Name 		: APIs Summary on landing Page
 *  Description : Users can view enabled Apis based on their roles and categories 
 *  Middleware Provider : MW Database 
 *  Urls invoked :: 
 *				get all category : http://10.138.30.11:9846/GatewayBridgePlugin/rest/BridgeController/getApicategoryData
 *				get all apis	 : http://10.138.30.11:9846/GatewayBridgePlugin/rest/BridgeController/getAllApiData
 * 				
 *	Developer  	: Suman Kumar Das 
  *	Created Date: 11th Oct 2017
 *	Modified Date: 
 *	Modified by  : 

--%>
<%@ include file="/init.jsp"%>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme"%>
<%@ page import="com.liferay.portal.kernel.model.User"%>
<%@ page import="com.liferay.portal.kernel.util.WebKeys"%>
<%@ page import="com.liferay.portal.kernel.theme.ThemeDisplay"%>



<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Telkom | My API</title>

    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="css/dasdezine.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">
	
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Montserrat:500|Raleway" rel="stylesheet">
	<!--<link rel="stylesheet" type="text/css" href="css/flip-container.css">-->

	<!--<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">-->
    <!-- <link href="http://fonts.googleapis.com/css?family=Lato:400,700,400italic,700italic" rel="stylesheet" type="text/css"> -->

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
<style>
.smsmore {
    color: #ffffff;
    background-color: #e74c3c;
    border-color: #e74c3c;
	border-radius: 0;
    width: 90%;
	margin-top: -10px
	}
#details_sms
	{
	height: 140px; 
    background-color: #e5e5e5;
    padding: 14px;
	width: 90%;
	line-height: 150%;
	}

#containersection p{
    margin: 85px;
    font-size: 13px!important;
	margin-top: 25px;
	}
h4{
    padding-left: 86px;
	font-size: 23px;
	color: tomato;
}
.btnforread{
	color: #ffffff;
    background-color: #e74c3c;
    border-color: #e74c3c;
	margin-left: 1px;
    border-radius: 0;
	margin-top: 10px;
}
.ourAPIcontent{
   width: 100%;
    margin-left: 0px;
    padding-bottom: 48px;
    padding-top: 16px;
}
#top_img {
    height: 90px;
    width: 25%;
}
.homeLogo {
    padding-top: 100px;
    width: 90%;
}
p {
    font-size: 21px;
}
#containersection p {
    margin: 84px;
    font-size: 13px!important;
    margin-top: 20px;
}

@media only screen 
and (max-width: 480px) 
and (min-width: 320px) 
and (orientation: landscape){
.img-responsive{float: left;width: 54%;}
.home_box{ margin-left: 72px;}

}
.img-container{
	height: 80px;
	vertical-align: text-bottom ;
	margin-left: 100px;
}
@media only screen 
and (min-width: 320px)
and (orientation : portrait){
.img-container {
	height: 95px;
    width: 25%;
    float: left;
    margin: 0 0 10px 10px;
	margin-left: -3px;
}
#details_sms{width: 60%;margin-left: 95px;}
.smsmore{width: 60%;margin-top: -10px;margin-left: 95px;}
}
@media (device-height : 568px) 
	and (device-width : 320px)
	and (orientation : portrait)   {
#details_sms {width: 60%;margin-left: 70px;}
.smsmore {margin-left: 70px;}
   
   }
@media only screen 
and (device-width : 768px) 
and (device-height : 1024px) 
and(orientation : portrait) {

  #details_sms {padding: 14px!important;}

}
#api-cat-select {
    border-color: #e62037;
    float: left;
    color: black;
    display: block;
    background-color: #fff;
	padding: 8px 5px 8px 4px;
    -webkit-padding-after: 7px;
    -webkit-padding-before: 5px;
    -webkit-padding-start: 5px;
    -webkit-padding-end: 5px;
    border: 1px solid #e62037;
    outline: 0;
	width: 19% !important;
	margin-bottom: 40px
}
.home_box .row{
width:90%;}
.box .btn-danger {
    color: #fff;
    background-color: #e62037;
    border-color: #e62037;
}
</style>
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
					url : "http://wso2-predev-website.2f96.telkom.openshiftapps.com/GatewayBridgePlugin/rest/BridgeController/getApicategoryData",
					type : 'json',					 
				}).done(
				 function(message){
					var categories = "<option value='None'>-Select Category-</option>";
					for (var i = 0; i < message.data.length; i++) {
					categories+="<option value="+message.data[i].CAT_ID+">"+message.data[i].CAT_NAME+"</option>";
					}
					$('#api-cat-select').html(categories);	
					var roleIds= $("#role_ids").val();
					PopulateAPIs(roleIds,"");
					
					

					 $("#api-cat-select").on("change",function() {
						 PopulateAPIs(roleIds,this.value);
				});
				  });
		
		function PopulateAPIs(roleId,categoryId){
			//alert('Role id is hardcoded to 1, change it to respective value in code. selected catid= '+categoryId);
		  	
		  	//alert(roleId);
			$.ajax({
				  type: "get",
				 //url : "http://wso2-predev-website.2f96.telkom.openshiftapps.com/GatewayBridgePlugin/rest/BridgeController/getAllApiData?api=WSO2&cache=true&role=1&category="+categoryId,
				  url : "http://wso2-predev-website.2f96.telkom.openshiftapps.com/GatewayBridgePlugin/rest/BridgeController/getAllApiData?api=WSO2&cache=true&role="+roleId+"&category="+categoryId,
				 contentType: 'application/json',		  
				}).done(
					function(message){
						var datacontent = "";
							for (var i = 0; i < message.data.length; i++) {
														if(message.data[i].API_Short_Desc.length > 90)
															{
															//alert('if');								
															apiShortDesc = (message.data[i].API_Short_Desc).substr(1,90)+"..<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; contd...";
															}
														else
															{
															//alert('else');
															apiShortDesc = message.data[i].API_Short_Desc;
															}
														datacontent +="<div class='col-sm-4' ><div class='box'><div><img id='top_img1' class='img-container' src='/o/TelkomAPISummary/Images/sms1.PNG' ></div><div><p id='details_sms'><strong>"+message.data[i].API_Name+"</strong><br/>"+apiShortDesc+"<br/><div><a href='http://tera-predev-website.2f96.telkom.openshiftapps.com/web/telkom/"+message.data[i].API_Name.toLowerCase()+"'><button type='button' class='btn btn-danger smsmore'>More</button></a></div></p></div></div></div>";
													} 

						$('#api_details_content').html(datacontent);
						
					}
				);
			
		}//PopulateAPIs() close
});
</script>
</head>

<body class="index landingpage">

<% 
//StringBuffer assignedRoleids = null;
String assgnRleIds = "";
long[] roleIds = ((ThemeDisplay)request.getAttribute(WebKeys.THEME_DISPLAY)).getUser().getRoleIds();
//out.println(roleIds.length);

for (int roleIdCnt=0;roleIdCnt<roleIds.length;roleIdCnt++)
{
	//out.println(roleIds[roleIdCnt]);
	
	if (roleIdCnt == (roleIds.length-1))
	{
		assgnRleIds += roleIds[roleIdCnt]+"";
	}
	else{
		assgnRleIds += roleIds[roleIdCnt]+",";
	}

}

//out.println(assgnRleIds);
%>
	<div class="row1">
	<div class="col-sm-12">
		<div class="api_list">
			<!--nav id="sidebarnav"-->
				<form class="md-form" action="" method="">
					<select class="form-control" id="api-cat-select">
						<option value="_none">--Select Category--</option>
					</select>
					<input type="hidden" id="role_ids" name="roleids" value="<%=assgnRleIds%>"/>
				</form>
			<!--/nav-->
		</div>
	</div>
	</div>
	
    <div class="pagesection">
		<section id="indexfirstscreen" class="scroll-section">
			<div class="homeinnersection maxwidth">
				
				
			</div>
	
	<div class="container">
		<div class="home_box">
			<div class ="row">
				<div id="api_details_content">&nbsp;</div>					
			</div>
		</div>
	</div>			
		</section>
		
	</div>


</body>

</html>
