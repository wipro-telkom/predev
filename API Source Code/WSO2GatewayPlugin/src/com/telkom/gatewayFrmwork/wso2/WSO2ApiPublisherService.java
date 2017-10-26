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
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@Path("/WSO2PublisherService")
public class WSO2ApiPublisherService {
	@SuppressWarnings("unchecked")
	@GET
	@Path("/getAllMethodsList")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllMethodsList() {
		JSONObject json = new JSONObject();
		Method[] methods = WSO2ApiPublisherService.class.getMethods();
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
		JSONObject json = obj.getAllApis(Wso2Constants.GETAPI_PUBLISHER_BASE_URL, "Publisher");
		Response response = Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();

		return response;
	}

	@SuppressWarnings("unchecked")
	@POST
	@Path("/getApiDetails")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getApiDetails(@QueryParam("API_NAME") String API_NAME, @QueryParam("version") String version,
			@QueryParam("provider") String provider) throws Exception {
		Wso2Integration obj = new Wso2Integration();
		JSONObject json = (JSONObject) new JSONParser()
				.parse(obj.getApiDetails(API_NAME, version, provider).get("api").toString());
		if (json.containsKey("name")) {
			json.put("error", "false");
		} else {
			json.put("error", "false");
		}
		Response response = Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();
		return response;
	}

	public static void main(String[] args) throws Exception {
		WSO2ApiPublisherService service = new WSO2ApiPublisherService();
		service.getApiDetails("SMSNotification", "1.0.0", "master@mainapi.net");
	}

}
