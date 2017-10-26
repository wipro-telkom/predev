<%--*  Name 		: API Category Management Page
 *  Description : Admin can add/delete/modify API categories
 *  Middleware Provider : MW Database
 *  Urls invoked :: 
 * 				Add    : http://10.138.30.11:9846/GatewayBridgePlugin/rest/BridgeController/add
 *				Delete : http://10.138.30.11:9846/GatewayBridgePlugin/rest/BridgeController/delete
 *				Modify : http://10.138.30.11:9846/GatewayBridgePlugin/rest/BridgeController/edit
 *	Developer  	: Suman Kumar Das
 *	Created Date: 4th Oct 2017
 *	Modified Date: 
 *	Modified by  : 

*--%>

<%@ include file="/init.jsp" %>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<style>
.category_title{
 	color : black;
}
.manage_api{
	margin-top : 20px;
}
.category_title h4{
	font-size : 21px;
    font-weight : bold;
}
.manage-category_details{
	margin-top : 20px;
}
.add_Category_popup h4{
	font-size : 24px;
    font-weight : bold;
}

.popup_title{
	font-size : 19px;
	font-weight : bold;
	color : #e62037;
	text-align : center;
} 
.category_popup_details{
	margin-top : 20px;
}
.modal {
    display: none;   
    position: fixed; 
    z-index: 1; 
    padding-top: 100px; 
    left: 0;
    top: 0;
    width: 100%;
    height: 100%; 
    overflow: auto; 
    background-color: rgb(0,0,0); 
    background-color: rgba(0,0,0,0.4); 
}
.modal-content {
    background-color: #fefefe;
    margin: auto;
    padding: 20px;
    border: 1px solid #888;
    width: 80%;
}
.close {
    color: #aaaaaa;
    float: right;
    font-size: 28px;
    font-weight: bold;
}
.close:hover,
.close:focus {
    color: #000;
    text-decoration: none;
    cursor: pointer;
}
.category_name{
	width : 100%;
	height: 40px;
}
.category_label{
	color : black;
	font-size : 14px;
	font-weight : 500;
}
.category_description{
	color : black;
	font-size : 14px;
}
.category_description{
	width : 100%;
	height: 40px;
}
.modal-content{
	width: 40%;
    height: 315px;
}
.description{
	    margin-top: 24px;
}
.descriptionbtn .btn-danger{
	    margin-top: 24px;
}
thead{
	background-color: #e62037;
    border-bottom: 2px solid #ee3124;
    }
.table>thead:first-child>tr:first-child>th {
    border-top: 0;
    color: white;
    }
.btn-danger {
    color: #fff;
    background-color: #e62037;
    border-color: #e62037;
}
.h5, h5 {
    font-size: 16px;
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
	-webkit-animation-duration: 5s;
	-webkit-animation-iteration-count: infinite;
	-webkit-animation-timing-function: linear;

	-moz-animation-name: linearRotate;
	-moz-animation-duration: 5s;
	-moz-animation-iteration-count: infinite;
	-moz-animation-timing-function: linear;

	-o-animation-name: linearRotate;
	-o-animation-duration: 5s;
	-o-animation-iteration-count: infinite;
	-o-animation-timing-function: linear;

	animation-name: linearRotate;
	animation-duration: 5s;
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


</style>

<div  id="loadingimage" class="loading"></div>
	
<div class="row">
	<div class="col-sm-12">
		<div id="manageapi" class="manage_api">
				<div class="category_title">
				<h4>Manage Category</h4>
				</div>
				<div class="manage-category_details">
					<table id="api_manageapi" class="table table-bordered">
						<thead>
							<tr>
								<th><h5>Category Name</h5></th>
								<th><h5>Category Description</h5></th>
								<th style="width: 10%;"></th>
							</tr>
						</thead>
						
					</table>
					<button class="btn btn-danger add_category" value="Manage">Add</button>
					</div>
					
<!--    ---------------Popup code starts------------------------------------------------------------------------------- -->			
				<div id="myModal" class="modal">
  					<div class="modal-content">
    					<span class="close">&times;</span>
				<div class="popup_title">
				<h4 class="popuplabel">Add Category</h4>
				</div>
				<div class="category_popup_details">		
					<div class="name">	
						<label class="category_label">Category Name</label>
						<input id="catname" class="category_name" placeholder="Category Name" name="category-name" required>
						<input type="hidden" id="categoryid" name="category-name" value="">
					</div>
					<div class="description">
						<label class="category_label">Description</label>
						<input id="catdesc" class="category_description" placeholder="Description" name="category-description" required>
					</div>
					<div class="descriptionbtn">
						<button class="btn btn-danger" value="Add" id="add">Add</button><!-- pop up add button -->
					</div>
				</div>
  					</div>
  				</div>	
<!-- ----------------Popup code Ends---------------------------------------------------------------------------------------------- -->		
		</div>
	</div>
</div>
<script>
	$(document).ready(function(){
	//alert(1);
	//Getting all available categories from webservice----------		
	function populateAPiCategory(){	
		$.ajax({
			method : "get",
			url : "http://10.138.30.11:9846/GatewayBridgePlugin/rest/BridgeController/getApicategoryData",
			type : 'json',
		}).done(
			function(message){
				//alert(message.data.length);
				$("#api_manageapi tbody").remove(); 
				var datacontent = "<tbody>";
				if(message.error == "false")
				{
				//alert("if");
					for (var i = 0; i < message.data.length; i++) {
					
					datacontent +="<tr><td class='cat_name'>"+message.data[i].CAT_NAME+"</td><td class='cat_desc'>"+message.data[i].CAT_DESC
								+"</td><td><button class='btn btn-primary edit_category'><i class='fa fa-pencil'></i>"
								+"</button><button class='btn btn-danger categorydelete'><i class='fa fa-trash-o'></i>"
								+"</button> <input id ='cat_id' type='hidden' name='catid' value='"+message.data[i].CAT_ID+"'></td><tr>"
								 
					}
				}else
				{
				//alert("else");
				  datacontent +="<tr><td colspan='3'> Error from Server ...</td><tr>";
				}
				datacontent += "</tbody>";
				$(datacontent).insertAfter("#api_manageapi thead"); 
				
		<!-- ---------------code for delete category starts------------------------------------>
				$(".btn.categorydelete").click(function(deletemsg) {
					//alert('delete');
					//alert($(this).siblings("#cat_id").val());
					 if (confirm('Are you sure to delete '+$(this).parent().siblings(".cat_name").html()+'!') == true) {
							$.ajax({
								  type: "get",
								  url: "http://10.138.30.11:9846/GatewayBridgePlugin/rest/BridgeController/delete",
								  data: {
									"datatype":"TelkomAPI_Categories",
									"id": $(this).siblings("#cat_id").val()
								  },
								  success: function(result) {
									alert('Deleted Successfully');
									 location.reload();
								  },
								  error: function(result) {
									alert('Some error in delete,Please try after sometime');
								  }
								});
						} //end of if for  confirmation
					
						 
			
				});
		<!-- code for delete category ends------------------------------------------------------------ -->	
	
		<!-- code for Update popup starts -------------------------------------------------------------      -->
			$('.edit_category').on('click', function(){
				$('#myModal input').val("");
				var catid = $(this).siblings("#cat_id").val();
				var name= $(this).parent().siblings(".cat_name").html();
				var desc = $(this).parent().siblings(".cat_desc").html();
				$('#myModal').find('#catname').val(name);
				$('#myModal').find('#catdesc').val(desc);
				//$('#myModal').find('#categoryid').val(catid);
				
				$('#myModal .descriptionbtn button').html("Update");
				$('#myModal .popup_title h4.popuplabel').html("Update Category");
				$('#add').val("Update");
				$('#categoryid').val(catid);
				$('#myModal').toggle();
				
	});	

		<!-- code for Update popup ends----------------------------------------------------------- ------------>		
			}
		); }
		populateAPiCategory();

		<!-- code for Add category starts -------------------------------------------------------------      -->		
		function addAPiCategory(){	 
		$.ajax({
			  type: "POST",
			  url: "http://10.138.30.11:9846/GatewayBridgePlugin/rest/BridgeController/add",
			  contentType: 'application/json',
			  data: '{"datatype":"TelkomAPI_Categories","data":{"CAT_DESC":"'+$("#catdesc").val()+'","CAT_NAME":"'+$("#catname").val()+'"}}',
			  dataType: "json",
			  beforeSend: function(request) {
				    request.setRequestHeader("Access-Control-Allow-Origin", "*");
				  }, 
			  success: function(result) {
				 location.reload();
				  $("#myModal").hide();
			  },
			  error: function(result) {
			  console.log(result);
				alert('Error in adding category,Please try later');
			  }
			});
		}		
		<!-- code for Add category  ends -------------------------------------------------------------      -->	
		<!-- code for Update category starts -------------------------------------------------------------      -->	
		function UpdateAPiCategory(catid){	
			//alert('UpdateAPiCategory');
              var request = '{"datatype":"TelkomAPI_Categories","data":{"CAT_DESC":"'+$("#catdesc").val()+'","CAT_NAME":"'+$("#catname").val()+'"},"id":"'+catid+'"}';
             console.log(request); 			  
		$.ajax({
			  type: "POST",
			  url: "http://10.138.30.11:9846/GatewayBridgePlugin/rest/BridgeController/edit",
			  contentType: 'application/json',
			  data: request,
			  dataType: "json",
			  beforeSend: function(request) {
				    request.setRequestHeader("Access-Control-Allow-Origin", "*");
				  }, 
			  success: function(result) {
				 location.reload();
				  $("#myModal").hide();
				  alert('update successfully');
			  },
			  error: function(result) {
			  console.log(result);
				alert('Error in update');
			  }
			});
		}
	<!-- code for Update category  ends -------------------------------------------------------------      -->		
	<!-- code for popup add/update button click starts-------------------------------------------------------------      -->		
		 $("#add").click(function(addmsg) {
		 addmsg.preventDefault();
		 if($(this).val() == 'Add'){
		  addAPiCategory();
		 }else
		 {
		  var catid = $('#categoryid').val();
		 UpdateAPiCategory(catid);
		 }
		});		  
	<!-- code for popup add/update button click ends-------------------------------------------------------------      -->		
	<!-- code for popup delete button click starts-------------------------------------------------------------      -->		  
		//$(".btn.categorydelete").click(function(deletemsg) {					
		 //addUpdateAPiCategory();

		 // });
	<!-- code for popup delete button click ends-------------------------------------------------------------      -->		
	<!-- code for Add popup starts -------------------------------------------------------------      -->							
	
	<!-- code for Add popup starts -------------------------------------------------------------      -->
	$('.add_category,#myModal .close').on('click', function(){
	$('#myModal input').val("");
	$('#myModal .descriptionbtn button').html("Add");
	$('#add').val("Add");
		$('#myModal').toggle();
		$('#myModal .popup_title h4.popuplabel').html("Add Category");
		<!-- code for Add popup ends -------------------------------------------------------------      -->
	});	
});
</script>
<script>
<!-- code for loading data --->
var $loading = $('#loadingimage').hide();
$(document).ajaxStart(function () {
    $loading.show();
  }).ajaxStop(function () {
    $loading.hide();
  });
  
</script>