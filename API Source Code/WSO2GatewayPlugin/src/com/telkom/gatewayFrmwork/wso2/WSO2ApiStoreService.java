package com.telkom.gatewayFrmwork.wso2;

import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@Path("/WSO2StoreService")
public class WSO2ApiStoreService {
	@SuppressWarnings("unchecked")
	@GET
	@Path("/getAllMethodsList")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllMethodsList() {
		JSONObject json = new JSONObject();
		Method[] methods = WSO2ApiStoreService.class.getMethods();
		String annotationsData;
		for (Method method : methods) {
			if (method.getReturnType() == Response.class) {
				annotationsData = "Parameters : ";
				for (Parameter annotation : method.getParameters()) {
					if (annotation.getAnnotation(QueryParam.class) != null)
						annotationsData = annotationsData + annotation.getAnnotation(QueryParam.class).value() + " ";
				}
				if (method.getAnnotation(POST.class) != null)
					json.put("POST : " + method.getAnnotation(Path.class).value(), annotationsData);
				else {
					json.put("GET : " + method.getAnnotation(Path.class).value(), annotationsData);
				}
			}
		}
		return Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();
	}

	@GET
	@Path("/getAllApis")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllApis() throws Exception {
		Wso2Integration obj = new Wso2Integration();
		JSONObject json = obj.getAllApis(Wso2Constants.GETAPI_STORE_BASE_URL, "Store");
		Response response = Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();

		return response;
	}

	@SuppressWarnings("unchecked")
	@POST
	@Path("/userSignup")
	@Produces(MediaType.APPLICATION_JSON)
	public Response userSignup(@QueryParam("username") String userName, @QueryParam("password") String password,
			@QueryParam("firstName") String firstName, @QueryParam("lastName") String lastName,
			@QueryParam("email") String email) throws Exception {
		JSONObject json = new JSONObject();
		if (userName == null || password == null) {
			json.put("error", "true");
			json.put("message", "Null is not supported");
		} else {
			Wso2Integration obj = new Wso2Integration();
			json = obj.userSignup(Wso2Constants.GETAPI_STORE_BASE_URL, userName, password, firstName, lastName, email);
		}
		Response response = Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();

		return response;
	}

	@SuppressWarnings("unchecked")
	@POST
	@Path("/userSignupbyApi")
	@Produces(MediaType.APPLICATION_JSON)
	public Response userSignupbyApi(@QueryParam("accesscode") String accesscode,
			@QueryParam("password") String password, @QueryParam("firstName") String firstName,
			@QueryParam("lastName") String lastName, @QueryParam("email") String email) throws Exception {
		JSONObject json = new JSONObject();
		if (email == null || password == null) {
			json.put("error", "true");
			json.put("message", "Null is not supported");
		} else {
			Wso2Integration obj = new Wso2Integration();
			json = obj.userSignupbyApi(accesscode, password, firstName, email);
		}
		Response response = Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();

		return response;
	}

	@POST
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	public Response logout(@QueryParam("userid") String userid) throws Exception {
		JSONObject json = new JSONObject();
		Wso2Integration obj = new Wso2Integration();
		json = obj.logout("Store");
		Response response = Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();

		return response;
	}

	@POST
	@Path("/getAllApplications")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllApplications(@QueryParam("userid") String userid, @QueryParam("password") String password)
			throws Exception {
		JSONObject json = new JSONObject();
		Wso2Integration obj = new Wso2Integration();
		json = obj.getApplications(userid, password);
		Response response = Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();

		return response;
	}

	@POST
	@Path("/getAllSubcriptions")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllSubcriptions(@QueryParam("userid") String userid, @QueryParam("password") String password)
			throws Exception {
		JSONObject json = new JSONObject();
		Wso2Integration obj = new Wso2Integration();
		json = obj.getAllSubcriptions(userid, password);
		Response response = Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();

		return response;
	}

	@POST
	@Path("/removeSubcriptions")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeSubcriptions(@QueryParam("apiName") String apiName,
			@QueryParam("apiVersion") String apiVersion, @QueryParam("applicationId") String applicationId,
			@QueryParam("userid") String userid, @QueryParam("password") String password) throws Exception {
		JSONObject json = new JSONObject();
		Wso2Integration obj = new Wso2Integration();
		json = obj.removeSubcriptions(apiName, apiVersion, applicationId, userid, password);
		Response response = Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();

		return response;
	}

	@POST
	@Path("/addSubcriptions")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addSubcriptions(@QueryParam("apiName") String apiName, @QueryParam("apiVersion") String apiVersion,
			@QueryParam("applicationId") String applicationId, @QueryParam("tier") String tier,
			@QueryParam("userid") String userid, @QueryParam("password") String password) throws Exception {
		JSONObject json = new JSONObject();
		Wso2Integration obj = new Wso2Integration();
		json = obj.addSubcriptions(apiName, apiVersion, applicationId, tier, userid, password);
		Response response = Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();
		return response;
	}

	/*
	 * action generateApplicationKey application DefaultApplication keytype
	 * PRODUCTION callbackUrl authorizedDomains ALL validityTime 360000
	 */

	@POST
	@Path("/generateApplicationKey")
	@Produces(MediaType.APPLICATION_JSON)
	public Response generateApplicationKey(@QueryParam("keytype") String keytype, @QueryParam("callbackUrl") String callbackUrl,
			@QueryParam("authorizedDomains") String authorizedDomains, @QueryParam("validityTime") String validityTime,
			@QueryParam("userid") String userid, @QueryParam("password") String password) throws Exception {
		JSONObject json = new JSONObject();
		Wso2Integration obj = new Wso2Integration();
		json = obj.generateApplicationKey(keytype, callbackUrl, authorizedDomains, userid, password);
		Response response = Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();
		return response;
	}

	@POST
	@Path("/getPricingData")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPricingData(@QueryParam("apiName") String apiName, @QueryParam("version") String version,
			@QueryParam("accesscode") String accesscode) throws Exception {
		JSONArray json = new JSONArray();
		String provider = "master@mainapi.net@carbon.super";
		Wso2Integration obj = new Wso2Integration();
		json = obj.getPricingData(provider, apiName, version, accesscode);
		Response response = Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();
		return response;
	}

	public static void main(String[] args) throws Exception {
		WSO2ApiStoreService store = new WSO2ApiStoreService();
		System.out.println(
				store.getPricingData("SMSNotification", "1.0.0", "cb5d814980d44121ab29b2cf31a8a844").getEntity());
	}
}
