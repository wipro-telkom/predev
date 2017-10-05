package com.telkom.gatewayFrmwork.wso2;

import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;

@Path("/WSO2PublisherService")
public class WSO2ApiPublisherService {

	@GET
	@Path("/getAllApis")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllApis() throws Exception {
		Wso2Integration obj = new Wso2Integration();
		JSONObject json = obj.getAllApis(Wso2Constants.GETAPI_PUBLISHER_BASE_URL, "Publisher");
		Response response = Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();

		return response;
	}

	@SuppressWarnings("unchecked")
	@POST
	@Path("/getApiDetails")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getApiDetails(@QueryParam("API_NAME") String API_NAME,@QueryParam("version") String API_Version,@QueryParam("provider") String provider) throws Exception {
		JSONObject json = new JSONObject();
		json.put("error", "true");
		Wso2Integration obj = new Wso2Integration();
		json = obj.getApiDetails(API_NAME,API_Version,provider);
		if (json.containsKey("api")) {
			json.put("error", "false");
		}
		Response response = Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();
		return response;
	}

}
