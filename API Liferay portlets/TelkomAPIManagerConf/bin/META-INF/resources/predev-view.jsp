<%---- /*  Name 		: APIs GatewayManager Page
 *  Description : Admin can view/update API gatewaymanager details
 *  Middleware Provider : MW Database
 *  Urls invoked :: 
 * 				view    : http://10.138.30.11:9846/GatewayBridgePlugin/rest/BridgeController/getApimgrData
 *				Modify 	: http://10.138.30.11:9846/GatewayBridgePlugin/rest/BridgeController/edit
 *	Developer  	: Lipsita Mohanty
 *	Created Date: 7th Oct 2017
 *	Modified Date: 
 *	Modified by  : 

*/ --%>


<%@ include file="/init.jsp" %>

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

<style>
.apimanager_title {
	color: #000000;
}
.manage-api {
	margin-top: 20px;
}

.apimanager_title h4 {
	font-size: 24px;
	font-weight: bold;
}

.apimanager_details {
	margin-top: 20px;
}

#apimanager_details p {
	font-size: 15px;
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
	background-color: rgb(0, 0, 0);
	background-color: rgba(0, 0, 0, 0.4);
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

.close:hover, .close:focus {
	color: #000;
	text-decoration: none;
	cursor: pointer;
}

.descriptionbtn {
	margin-top: 20px;
}

label {
	display: inline-block;
	max-width: 100%;
	margin-bottom: 7px;
	font-weight: 700;
	margin-top: 7px;
}

input[type=checkbox], input[type=radio] {
	margin: 4px 0 0;
	line-height: normal;
	float: left;
}
.table>thead:first-child>tr:first-child>th {
    border-top: 0;
    color: white;
</style>
<script>	
	$(document).ready(function(){
		
		function showdata(){
			$.ajax({
					method : "get",
					url : "http://wso2-predev-website.2f96.telkom.openshiftapps.com/GatewayBridgePlugin/rest/BridgeController/getApimgrData",
					type : 'json',
				}).done(function(msg) {
				//alert(msg.data.length);
					$("#api_mngr_details tbody").remove();
				var datacontent = "<tbody>";
				for (var i = 0; i < msg.data.length; i++) {
				var enabled = msg.data[i].API_MANAGER_STATUS == 1 ?"Enabled":"Disabled";
				datacontent +="<tr><td class='mgr_name'>"+msg.data[i].API_MNGR_NAME
				+"</td><td class='publish_url'>"+msg.data[i].PUBLISHER_URL
				+"</td><td class='store_url'>"+msg.data[i].STORE_URL
				+"</td><td class='user_id'>"+msg.data[i].USER_ID
				+"</td><td class='password'>"+msg.data[i].PASSWORD
				+"</td><td class='token_url'>"+msg.data[i].TOKEN_URL
				+"</td><td class='manager_status'>"+enabled
				+"</td><td><button class='btn btn-primary edit_manager'><i class='fa fa-pencil'></i>"
				+"</button><input id ='manager_id' type='hidden' name='manager_id' value='"+msg.data[i].API_MNGR_ID+"'></input></td></tr>"
				
				}
                datacontent += "</tbody>";
				$(datacontent).insertAfter("#api_mngr_details thead"); 
				$('.edit_manager').on('click', function(){
					$('#myModal input').val("");
					var parent = $(this).parent();
					var manager_id = $(this).siblings("#manager_id").val();
					var mgr_name = $(parent).siblings(".mgr_name").html();
					var publish_url= $(parent).siblings(".publish_url").html();
					var store_url = $(parent).siblings(".store_url").html();
					var user_id= $(parent).siblings(".user_id").html();
					var password= $(parent).siblings(".password").html();
					var token_url = $(parent).siblings(".token_url").html();
					var manager_status = $(parent).parent().siblings(".manager_status").html();
					var selected = manager_status == "Enabled" ? true:false;
					$('#myModal').find('#managerid').val(manager_id);
					$('#myModal').find('#maangername').val(mgr_name);
					$('#myModal').find('#publishurl').val(publish_url);
					$('#myModal').find('#storeurl').val(store_url);
					$('#myModal').find('#userid').val(user_id);
					$('#myModal').find('#password').val(password);
					$('#myModal').find('#tokenurl').val(token_url);
					$('#myModal').find('.manager_status').prop('checked',selected);
					//$('#myModal .descriptionbtn button').html("Update");
					$('#myModal .popup_title h4.popuplabel').html("Update API Manager");
					$('#myModal').toggle();
					<!--Update code starts -->
				});
				});
			}
		$('#add').on('click', function(){
			console.log(manager_id);
			var request = '';
			$.ajax({
			type: "POST",
			contentType: 'application/json',
			url: "http://wso2-predev-website.2f96.telkom.openshiftapps.com/GatewayBridgePlugin/rest/BridgeController/edit",
			data:'{"datatype":"TelkomApiManager","id":"'+$("#managerid").val()+'","data":{"PUBLISHER_URL":"'+$("#publishurl").val()+'","STORE_URL":"'+$("#storeurl").val()+'","USER_ID":"'+$("#userid").val()+'","PASSWORD":"'+$("#password").val()+'","TOKEN_URL":"'+$("#tokenurl").val()+'","API_MANAGER_STATUS":"'+$("input.manager_status").prop('checked')+'","API_MNGR_ID":" '+$("#managerid").val()+'"}}',
				
			dataType: "json",
			success: function(result) {
				$('#myModal').toggle();
				showdata();
			alert('Success');
			},
			error: function(result) {
			console.log(result);
			alert('Error ! Tryagain');
			}
			});

			});
				<!--Update code ends -->
				showdata();
				$('.edit_manager,#myModal .close').on('click', function(){
				$('#myModal input').val("");
				$('#myModal .descriptionbtn button').html("Add");
				$('#myModal').toggle();
				$('#myModal .popup_title h4.popuplabel').html("Add Category");
			});	
			
			
			<!-- code for Update popup starts -------------------------------------------------------------      -->
				
   			
	});
</script>
<div class="row">
	<div class="col-sm-12">
		<div id="api_manager" class="manage-api">
			<div class="apimanager_title">
				<h4>API Manager</h4>
			</div>
			<div class="apimanager_details">
				<div>
					<table id="api_mngr_details" class="table table-bordered">
						<thead>
							<tr bgcolor="#e62037">
								<th>API Manager Name</th>
								<th>Publisher URL</th>
								<th>Store URL</th>
								<th>USER ID</th>
								<th>Password</th>
								<th>Token URL</th>
								<th>API Manager Status</th>
								<th>Actions</th>
							</tr>
						</thead>
					</table>
				</div>


			</div>
			<!--    ---------------Popup code starts------------------------------------------------------------------------------- -->
			<div id="myModal" class="modal">
				<div class="modal-content">
					<div class="">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
					</div>
					<div class="popup_title">
						<h4 class="popuplabel">Add Category</h4>
					</div>
					<div class="row category_popup_details">
						<div class="col-sm-6">
							<div class="name">
								<input type="hidden" id="managerid" class="form-control"
									placeholder="managerid" name="manage-id" required></input>
							</div>
							<div class="description">
								<label class="category_label">API Manager Name</label> <input
									id="maangername" class="form-control" placeholder="Description"
									name="category-description" required disabled>
							</div>
							<div class="description">
								<label class="category_label">Publisher URL</label> <input
									id="publishurl" class="form-control" placeholder="Description"
									name="category-description" required>
							</div>

							<div class="description">
								<label class="category_label">Store URL</label> <input
									id="storeurl" class="form-control" placeholder="Description"
									name="category-description" required>
							</div>

						</div>
						<div class="col-sm-6">
							<div class="description">
								<label class="category_label">USER ID</label> <input id="userid"
									class="form-control" placeholder="Description"
									name="category-description" required>
							</div>
							<div class="description">
								<label class="category_label">Password</label> <input
									id="password" class="form-control" placeholder="Description"
									name="category-description" required>
							</div>
							<div class="description">
								<label class="category_label">Token URL</label> <input
									id="tokenurl" class="form-control" placeholder="Description"
									name="category-description" required>
							</div>
							<div class="description">
								<label class="category_label">API Manager Status</label>
								<div class="status">
									Enabled <input type="checkbox" id="form-control"
										class="manager_status" name="category-description" required>
								</div>
							</div>
						</div>


					</div>
					<div class="row">
						<div class="col-sm-12">
							<div class="descriptionbtn">
								<button class="btn btn-danger" value="Add" id="add">Update</button>
								<!-- pop up add button -->
							</div>
						</div>
					</div>
				</div>

				<!-- ----------------Popup code Ends---------------------------------------------------------------------------------------------- -->

			</div>

		</div>

	</div>