package com.telkom.gatewayFrmwork.wso2;

import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;

@Path("/WSO2StoreService")
public class WSO2ApiStoreService {

	@GET
	@Path("/getAllApis")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllApis() throws Exception {
		Wso2Integration obj = new Wso2Integration();
		JSONObject json = obj.getAllApis(Wso2Constants.GETAPI_STORE_BASE_URL,"Store");
		Response response = Response.status(200).
                entity(json).
                header("Access-Control-Allow-Origin", "*").build();

        return response;
	}
	@SuppressWarnings("unchecked")
	@POST
	@Path("/userSignup")
	@Produces(MediaType.APPLICATION_JSON)
	public Response userSignup(@QueryParam("username") String userName,@QueryParam("password") String password,@QueryParam("firstName") String firstName,@QueryParam("lastName") String lastName,@QueryParam("email") String email) throws Exception {
		JSONObject json = new JSONObject();
		if(userName == null || password == null){
			json.put("error", "true");
			json.put("message", "Null is not supported");
		}else{
		Wso2Integration obj = new Wso2Integration();
		json = obj.userSignup(Wso2Constants.GETAPI_STORE_BASE_URL,userName,password,firstName,lastName,email);
		}
		Response response = Response.status(200).
                entity(json).
                header("Access-Control-Allow-Origin", "*").build();

        return response;
	}
	
	@POST
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	public Response logout() throws Exception {
		JSONObject json = new JSONObject();
		Wso2Integration obj = new Wso2Integration();
		json = obj.logout("Store");
		Response response = Response.status(200).
                entity(json).
                header("Access-Control-Allow-Origin", "*").build();

        return response;
	}

}
