package com.telkom.gatewayFrmwork.wso2;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;

@Path("/WSO2TokenService")
public class WSO2ApiTokenService {

	@SuppressWarnings("unchecked")
	@GET
	@Path("/getAllMethodsList")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllMethodsList() {
		JSONObject json = new JSONObject();
		Method[] methods = WSO2ApiTokenService.class.getMethods();
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

	@POST
	@Path("/getAccessCode")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAccessCode(@QueryParam("clientId") String clientId,
			@QueryParam("clientSecret") String clientSecret, @QueryParam("userid") String userid,
			@QueryParam("password") String password, @QueryParam("validity_period") String validity_period)
			throws Exception {
		Wso2Integration obj = new Wso2Integration();
		JSONObject json = obj.getAccessCode(Wso2Constants.GETAPI_TOKEN_BASE_URL, clientId, clientSecret, userid,
				password, validity_period);
		Response response = Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();

		return response;
	}

}
