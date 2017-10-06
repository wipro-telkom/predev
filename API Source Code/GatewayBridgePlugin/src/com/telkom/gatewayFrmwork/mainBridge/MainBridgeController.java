package com.telkom.gatewayFrmwork.mainBridge;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.telkom.apiDatabaseInterface.pojo.TelkomAPI_Categories;
import com.telkom.apiDatabaseInterface.pojo.TelkomApiData;
import com.telkom.apiDatabaseInterface.pojo.TelkomApiManager;
import com.telkom.gatewayFrmwork.databaseUtil.BridgeDatabaseService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@SuppressWarnings("unchecked")
@Path("/BridgeController")
public class MainBridgeController {
	@GET
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addData(@QueryParam("datatype") String datatype, @QueryParam("data") String data) throws Exception {
		System.out.println("Running ADD");
		JSONObject json = new JSONObject();
		if (data != null && datatype != null) {
			JSONObject jsonData = (JSONObject) new JSONParser().parse(data.replace("[", "").replace("]", ""));
			json.put("error", "false");
			BridgeDatabaseService database = new BridgeDatabaseService();
			Class<?> classData = Class
					.forName("com.telkom.apiDatabaseInterface.pojo." + datatype.toString());
			Iterator<?> keys = ((JSONObject) new JSONParser().parse(jsonData.toString())).keySet().iterator();
			Boolean flag = true;
			while (keys.hasNext()) {
				try {
					classData.getMethod("get" + keys.next().toString());
				} catch (Exception e) {
					flag = false;
					json.put("error", "true");
					e.printStackTrace();
				}
			}
			if (flag) {
				Object obj = Class.forName("com.telkom.apiDatabaseInterface.pojo." + datatype.toString())
						.getConstructor().newInstance();
				Gson gson = new GsonBuilder().create();
				obj = gson.fromJson(jsonData.toString(), obj.getClass());
				database.add(obj);
			}
		} else {
			json.put("message", "Please follow correct syntax");
		}
		Response response = Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();

		return response;
	}

	@POST
	@Path("/edit")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response editData(JSONObject data) throws Exception {
		BridgeDatabaseService database = new BridgeDatabaseService();
		Object obj = Class.forName("com.telkom.apiDatabaseInterface.pojo." + data.get("datatype").toString())
				.getConstructor().newInstance();
		Gson gson = new GsonBuilder().create();
		obj = gson.fromJson(data.get("data").toString(), obj.getClass());
		database.saveOrUpdate(obj);
		Response response = Response.status(200).entity(data).header("Access-Control-Allow-Origin", "*").build();

		return response;
	}

	@GET
	@Path("/getApiData")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getApiData(@QueryParam("api") String api, @QueryParam("cache") String cache,
			@QueryParam("role") String role) throws Exception {
		JSONObject json = new JSONObject();
		if (api != null) {
			if (cache == null || cache.equals("true")) {
				BridgeDatabaseService database = new BridgeDatabaseService();
				List<TelkomApiManager> apimanager = database.loadBykey(TelkomApiManager.class, "API_MNGR_NAME", api,
						null);
				if (!apimanager.isEmpty()) {
					List<TelkomApiData> data = database.loadBykey(TelkomApiData.class, "API_Mgr_ID",
							apimanager.get(0).getAPI_MNGR_ID(), role);
					JSONObject jsonData = (JSONObject) new JSONParser()
							.parse(new GsonBuilder().create().toJson(data.get(0)));
					json.put("error", "false");
					json.put("data", jsonData);
				} else {
					json.put("error", "true");
					json.put("message", "Data not found");
				}
			} else if (cache.equals("false")) {
				if (api.toLowerCase().equals("wso2")) {
					json.put("error", "false");
					json.put("data", getWso2AllApi());
				} else {
					json.put("error", "true");
					json.put("message", "Invalid API Manager Selection");
				}
			}
		} else {
			json.put("error", "false");
			json.put("message", "Please follow correct syntax");
		}
		Response response = Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();

		return response;
	}

	/*
	 * * Get WSO2 All API data.
	 */
	private JSONObject getWso2AllApi() {
		HttpURLConnection c = null;
		JSONObject json = null;
		try {
			URL u = new URL("http://10.138.30.11:9846/WSO2GatewayPlugin/rest/WSO2PublisherService/getAllApis");
			c = (HttpURLConnection) u.openConnection();
			c.setRequestMethod("GET");
			c.setRequestProperty("Content-length", "0");
			c.setUseCaches(false);
			c.setAllowUserInteraction(false);
			c.connect();
			int status = c.getResponseCode();

			switch (status) {
			case 200:
			case 201:
				BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();
				json = (JSONObject) new JSONParser().parse(sb.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	@GET
	@Path("/getApimgrData")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getApimgrData() throws Exception {
		JSONObject json = new JSONObject();
		BridgeDatabaseService database = new BridgeDatabaseService();
		List<TelkomApiManager> data = database.getAll(TelkomApiManager.class);
		JSONObject[] jsondata = new JSONObject[data.size()];
		for (int i = 0; i < data.size(); i++) {
			System.out.println((JSONObject) new JSONParser().parse(new GsonBuilder().create().toJson(data.get(i))));
			jsondata[i] = (JSONObject) new JSONParser().parse(new GsonBuilder().create().toJson(data.get(i)));
		}
		json.put("error", "false");
		json.put("data", jsondata);
		Response response = Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();

		return response;
	}

	@GET
	@Path("/getApicategoryData")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getApicategoryData() {
		JSONObject json = new JSONObject();
		try {
			BridgeDatabaseService database = new BridgeDatabaseService();
			List<TelkomAPI_Categories> data = database.getAll(TelkomAPI_Categories.class);
			JSONObject[] jsondata = new JSONObject[data.size()];
			for (int i = 0; i < data.size(); i++) {
				System.out.println((JSONObject) new JSONParser().parse(new GsonBuilder().create().toJson(data.get(i))));
				jsondata[i] = (JSONObject) new JSONParser().parse(new GsonBuilder().create().toJson(data.get(i)));
			}
			json.put("error", "false");
			json.put("data", jsondata);
		} catch (Exception e) {
			json.put("error", "true");
			json.put("message", "Error in getApicategoryData()");
			e.printStackTrace();
		}
		Response response = Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();
		return response;
	}

	@GET
	@Path("/delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@QueryParam("datatype") String datatype, @QueryParam("id") String id) throws Exception {
		BridgeDatabaseService database = new BridgeDatabaseService();
		JSONObject json = new JSONObject();
		try {
			Object obj = database.load(Class.forName("com.telkom.apiDatabaseInterface.pojo." + datatype.toString()),
					Integer.parseInt(id));
			database.delete(obj);
			json.put("error", "false");
			json.put("message", "Row number " + id + " Deleted");
		} catch (Exception e) {
			json.put("error", "true");
			json.put("message", "Invalid Data");
			e.printStackTrace();
		}
		Response response = Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();

		return response;
	}

	public static void main(String[] args) throws Exception {
		String data = "{'CAT_NAME': df,'CAT_DESC': gff}";
		System.out.println(data);
		JSONObject json = (JSONObject) new JSONParser().parse(data);
		System.out.println(json);
	}

}
