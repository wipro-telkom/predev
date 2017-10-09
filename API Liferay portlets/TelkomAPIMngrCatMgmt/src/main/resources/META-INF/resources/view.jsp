/*  Name 		: API Category Management Page
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

*/

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
 	color : #ffffff;
 	background-color : #e62037;
}
.manage_api{
	margin-top : 20px;
}
.category_title h4{
	font-size : 24px;
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
</style>
<div  id="loading" align="center" style="margin-top:200px; margin-left:100px;display:none;"><img src="Ripple.gif"> Loading...</img></div>
	
<div class="row">
	<div class="col-sm-12">
		<div id="manageapi" class="manage_api">
				<div class="category_title">
				<h4>API Category Details</h4>
				</div>
				<div class="manage-category_details">
					<table id="api_manageapi" class="table table-bordered">
						<thead>
							<tr>
								<th>Category Name</th>
								<th>Category Description</th>
								<th></th>
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
									alert('error in delete');
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
				alert('Error');
			  }
			});
		}		
		<!-- code for Add category  ends -------------------------------------------------------------      -->	
		<!-- code for Update category starts -------------------------------------------------------------      -->	
		function UpdateAPiCategory(catid){	
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
			  },
			  error: function(result) {
			  console.log(result);
				alert('Error');
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
var $loading = $('#loading').hide();
$(document).ajaxStart(function () {
    $loading.show();
  }).ajaxStop(function () {
    $loading.hide();
  });
</script>