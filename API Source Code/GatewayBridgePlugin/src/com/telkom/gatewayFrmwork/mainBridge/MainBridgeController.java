package com.telkom.gatewayFrmwork.mainBridge;

import com.eviware.soapui.SoapUI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.telkom.apiDatabaseInterface.pojo.TelkomAPI_Categories;
import com.telkom.apiDatabaseInterface.pojo.TelkomApiData;
import com.telkom.apiDatabaseInterface.pojo.TelkomApiManager;
import com.telkom.apiDatabaseInterface.pojo.Telkomapi_Cat_Mapping;
import com.telkom.apiDatabaseInterface.pojo.Telkomapi_Role_Mapping;
import com.telkom.apiDatabaseInterface.pojo.Telkomapi_Users;
import com.telkom.gatewayFrmwork.databaseUtil.BridgeDatabaseService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.persistence.Column;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@SuppressWarnings("unchecked")
@Path("/BridgeController")
public class MainBridgeController {
	HttpClient httpClient = null;
	public static final Proxy PROXY = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy2.wipro.com", 8080));
	private static final String WSO2_BASE_URL = "http://wso2-predev-website.2f96.telkom.openshiftapps.com";
	// private static final String WSO2_BASE_URL = "10.138.30.11:9846";

	public HttpClient getHttpClient() {
		if (httpClient == null) {
			try {
				SSLContextBuilder builder = new SSLContextBuilder();
				builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
				SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(builder.build());
				httpClient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
			} catch (NoSuchAlgorithmException e) {
				SoapUI.logError(e, "Unable to load the trust store");
			} catch (KeyStoreException e) {
				SoapUI.logError(e, "Unable to get the key store instance");
			} catch (KeyManagementException e) {
				SoapUI.logError(e, "Unable to load trust store material");
			}
		}
		return httpClient;
	}

	@GET
	@Path("/getAllMethodsList")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllMethodsList() {
		JSONObject json = new JSONObject();
		Method[] methods = MainBridgeController.class.getMethods();
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
	@Path("/getTokenData")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTokenData(@QueryParam("userid") String userid) throws Exception {
		JSONObject json = new JSONObject();
		BridgeDatabaseService database = null;
		try {
			Map<String, Object> keyMap = new HashMap<String, Object>();
			keyMap.put("User_ID", userid);
			database = new BridgeDatabaseService();
			List<Telkomapi_Users> data = database.loadBykey(Telkomapi_Users.class, keyMap);
			if (!data.isEmpty()) {
				JSONObject jsonData = new JSONObject();
				Telkomapi_Users user = data.get(0);
				jsonData.put("accesstoken", user.getAccess_Code());
				jsonData.put("validity", user.getValidity());
				json.put("error", "false");
				json.put("data", jsonData);
			} else {
				json.put("error", "true");
				json.put("data", "User Not Found");
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("error", "true");
			json.put("message", "Exception Happened in Server, Check Stacktrace");
		} finally {
			if (database != null && database.getSession() != null)
				database.closeSession();
		}
		return Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();
	}

	@POST
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response add(JSONObject data) throws Exception {
		data = (JSONObject) new JSONParser().parse(data.toJSONString());
		JSONObject json = new JSONObject();
		BridgeDatabaseService database = null;
		try {
			if (data != null && data.containsKey("data") && data.containsKey("datatype")) {
				if (!data.get("datatype").toString().equals("TelkomApiData")) {
					Class<?> classData = Class
							.forName("com.telkom.apiDatabaseInterface.pojo." + data.get("datatype").toString());
					JSONObject jsonData = (JSONObject) new JSONParser().parse(data.get("data").toString());
					json.put("error", "false");
					database = new BridgeDatabaseService();
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
						Object obj = classData.getConstructor().newInstance();
						Gson gson = new GsonBuilder().create();
						obj = gson.fromJson(jsonData.toString(), obj.getClass());
						database.add(obj);
					}
				} else {
					String respon = addTelkomApiData(data).toString();
					json.put("error", respon);
					json.put("message", "Addition of API Data : " + json.put("error", respon));
				}
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			json.put("message", "Please follow correct syntax");
			e.printStackTrace();
		} finally {
			if (database != null && database.getSession() != null)
				database.closeSession();
		}
		return Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();
	}

	@SuppressWarnings("rawtypes")
	private Boolean addTelkomApiData(JSONObject data) {
		BridgeDatabaseService database = null;
		try {
			database = new BridgeDatabaseService();
			Gson gson = new GsonBuilder().create();
			TelkomApiData apiData = gson.fromJson(data.get("data").toString(), TelkomApiData.class);
			Map nameMap = new HashMap<String, Object>();
			nameMap.put("API_Name", apiData.getAPI_Name());
			List<TelkomApiData> apiList = database.loadBykey(TelkomApiData.class, nameMap);
			if (data.get("Published") != null && data.get("Published").toString().equalsIgnoreCase("True")) {
				apiData.setAPI_Status("True");
			} else {
				apiData.setAPI_Status("False");
			}
			int api_id = 0;
			if (apiList.size() != 0) {
				JSONObject jsonApi = (JSONObject) new JSONParser().parse(data.get("data").toString());
				TelkomApiData api = apiList.get(0);
				Iterator iterator = jsonApi.keySet().iterator();
				while (iterator.hasNext()) {
					String field = (String) iterator.next();

					Field fieldData = TelkomApiData.class.getDeclaredField(field);
					if (fieldData.getName() != "API_ID") {
						Method method = TelkomApiData.class.getMethod("set" + field, fieldData.getType());
						Class classData = fieldData.getType();
						if (classData == Boolean.class) {
							method.invoke(api, Boolean.parseBoolean(jsonApi.get(field).toString().trim()));
						} else if (classData == int.class) {
							method.invoke(api, Integer.parseInt(jsonApi.get(field).toString().trim()));
						} else if (classData == boolean.class) {
							method.invoke(api, Boolean.parseBoolean(jsonApi.get(field).toString().trim()));
						} else if (classData == byte.class) {
							method.invoke(api, Byte.parseByte(jsonApi.get(field).toString().trim()));
						} else if (classData == char.class) {
							method.invoke(api, jsonApi.get(field).toString().trim().charAt(0));
						} else if (classData == short.class) {
							method.invoke(api, Short.parseShort(jsonApi.get(field).toString().trim()));
						} else if (classData == long.class) {
							method.invoke(api, Long.parseLong(jsonApi.get(field).toString().trim()));
						} else if (classData == float.class) {
							method.invoke(api, Float.parseFloat(jsonApi.get(field).toString().trim()));
						} else if (classData == double.class) {
							method.invoke(api, Double.parseDouble(jsonApi.get(field).toString().trim()));
						} else {
							method.invoke(api, classData.cast(jsonApi.get(field)));
						}
					}
				}
				api.setAPI_Status(apiData.getAPI_Status());
				System.out.println(api);
				api_id = api.getAPI_ID();
				database.update(api);
			} else {
				api_id = (int) database.add(apiData);
			}
			String[] cat_ids = new String[0];
			String[] role_ids = new String[0];
			if (data.get("CAT_IDS") != null && !data.get("CAT_IDS").toString().equals("")) {
				cat_ids = data.get("CAT_IDS").toString().split(",");
			}
			if (data.get("Role_IDS") != null && !data.get("Role_IDS").toString().equals("")) {
				role_ids = data.get("Role_IDS").toString().split(",");
			} else {
				role_ids = "20123".split(","); // Guest ROle ID.
			}
			Map keyMap = new HashMap<String, Object>();
			if (api_id != 0) {
				keyMap.put("API_ID", api_id);
			}
			List<Telkomapi_Cat_Mapping> categories = database.loadBykey(Telkomapi_Cat_Mapping.class, keyMap);
			List<Telkomapi_Role_Mapping> roles = database.loadBykey(Telkomapi_Role_Mapping.class, keyMap);
			// Removing old entries for a particular API.
			for (Telkomapi_Cat_Mapping telkomapi_Cat_Mapping : categories) {
				database.delete(telkomapi_Cat_Mapping);
			}
			for (Telkomapi_Role_Mapping telkomapi_Role_Mapping : roles) {
				database.delete(telkomapi_Role_Mapping);
			}
			for (String cat_id : cat_ids) {
				Telkomapi_Cat_Mapping cat_Mapping = new Telkomapi_Cat_Mapping();
				cat_Mapping.setAPI_ID(api_id);
				cat_Mapping.setCAT_ID(Integer.parseInt(cat_id));
				database.add(cat_Mapping);
			}
			for (String role_id : role_ids) {
				Telkomapi_Role_Mapping role_Mapping = new Telkomapi_Role_Mapping();
				role_Mapping.setAPI_ID(api_id);
				role_Mapping.setROLE_ID(Integer.parseInt(role_id));
				database.add(role_Mapping);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (database != null && database.getSession() != null)
				database.closeSession();
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	@POST
	@Path("/edit")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response edit(JSONObject data) throws Exception {
		data = (JSONObject) new JSONParser().parse(data.toJSONString());
		BridgeDatabaseService database = null;
		JSONObject json = new JSONObject();
		System.out.println("EDIT Running for data : " + data);
		try {
			database = new BridgeDatabaseService();
			JSONObject jsonData = (JSONObject) new JSONParser().parse(data.get("data").toString());
			Object dataObject = database.load(
					Class.forName("com.telkom.apiDatabaseInterface.pojo." + data.get("datatype").toString()),
					Integer.parseInt((String) data.get("id")));
			Iterator<String> keys = jsonData.keySet().iterator();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				Class classData = dataObject.getClass().getMethod("get" + key).getReturnType();
				Method method = dataObject.getClass().getMethod("set" + key, classData);
				if (classData == Boolean.class) {
					method.invoke(dataObject, Boolean.parseBoolean(jsonData.get(key).toString().trim()));
				} else if (classData == int.class) {
					method.invoke(dataObject, Integer.parseInt(jsonData.get(key).toString().trim()));
				} else if (classData == boolean.class) {
					method.invoke(dataObject, Boolean.parseBoolean(jsonData.get(key).toString().trim()));
				} else if (classData == byte.class) {
					method.invoke(dataObject, Byte.parseByte(jsonData.get(key).toString().trim()));
				} else if (classData == char.class) {
					method.invoke(dataObject, jsonData.get(key).toString().trim().charAt(0));
				} else if (classData == short.class) {
					method.invoke(dataObject, Short.parseShort(jsonData.get(key).toString().trim()));
				} else if (classData == long.class) {
					method.invoke(dataObject, Long.parseLong(jsonData.get(key).toString().trim()));
				} else if (classData == float.class) {
					method.invoke(dataObject, Float.parseFloat(jsonData.get(key).toString().trim()));
				} else if (classData == double.class) {
					method.invoke(dataObject, Double.parseDouble(jsonData.get(key).toString().trim()));
				} else {
					method.invoke(dataObject, classData.cast(jsonData.get(key)));
				}
			}
			database.update(dataObject);
			json.put("error", "false");
		} catch (Exception e) {
			e.printStackTrace();
			json.put("error", "true");
		} finally {
			if (database != null && database.getSession() != null)
				database.closeSession();
		}
		return Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();
	}

	@SuppressWarnings("rawtypes")
	@GET
	@Path("/getAllApiData")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllApiData(@QueryParam("api") String api, @QueryParam("cache") String cache,
			@QueryParam("role") String roleId, @QueryParam("category") String categoryId) throws Exception {
		JSONObject json = new JSONObject();
		BridgeDatabaseService database = new BridgeDatabaseService();
		try {
			if (api != null && roleId != null) {
				if (cache == null || cache.equals("true")) {
					String categoryString = "";
					if (categoryId != null) {
						if (!categoryId.equals(""))
							categoryString = "am.CAT_ID IN ('" + categoryId + "') and";
					}
					Session session = database.getSession();
					String queryString = "Select DISTINCT(api.API_ID) FROM TelkomApiData api,Telkomapi_Cat_Mapping am,Telkomapi_Role_Mapping rm where "
							+ categoryString + " rm.role_id IN (" + roleId + ") "
							+ " and am.API_ID=api.API_ID and rm.API_ID=api.API_ID and api.API_Status = 'true'";
					SQLQuery query = session.createSQLQuery(queryString);
					List<Integer> apiIdList = query.list();
					JSONArray jsonArray = new JSONArray();
					for (Integer apiId : apiIdList) {
						TelkomApiData finalData = new TelkomApiData();
						TelkomApiData apiData = (TelkomApiData) database.load(TelkomApiData.class, apiId);
						Class classData = Class.forName("com.telkom.apiDatabaseInterface.pojo.TelkomApiData");
						Field[] fields = TelkomApiData.class.getDeclaredFields();
						for (Field field : fields) {
							String fieldname = field.getAnnotation(Column.class).name();
							if (field.getType() == int.class) {
								classData.getMethod("set" + fieldname, int.class).invoke(finalData,
										classData.getMethod("get" + fieldname).invoke(apiData));
							} else {
								classData.getMethod("set" + fieldname, Class.forName(field.getType().getName()))
										.invoke(finalData, classData.getMethod("get" + fieldname).invoke(apiData));
							}
						}
						jsonArray.add(finalData);
					}
					json.put("error", "false");
					json.put("data", jsonArray);

				} else if (cache.equals("false")) {
					if (api.toLowerCase().equals("wso2")) {
						json = getWso2AllApi();
						json.put("error", "false");
					} else {
						json.put("error", "true");
						json.put("message", "Invalid API Manager Selection");
					}
				}
			} else {
				json.put("error", "false");
				json.put("message", "Please follow correct syntax");
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("error", "true");
			json.put("message", "Exception Happened in Server, Check Stacktrace");
		} finally {
			if (database != null && database.getSession() != null)
				database.closeSession();
		}
		return Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();
	}

	/*
	 * * Get WSO2 All API data.
	 */
	private JSONObject getWso2AllApi() throws Exception {
		HttpURLConnection c = null;
		JSONObject json = new JSONObject();
		JSONParser parser = new JSONParser();
		URL u = new URL(WSO2_BASE_URL + "/WSO2GatewayPlugin/rest/WSO2StoreService/getAllApis");
		c = (HttpURLConnection) u.openConnection();
		c.setRequestMethod("GET");
		c.setRequestProperty("Content-length", "0");
		c.setUseCaches(false);
		c.setAllowUserInteraction(false);
		c.connect();
		int status = c.getResponseCode();
		StringBuilder sb = null;
		switch (status) {
		case 200:
		case 201:
			BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
			sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			br.close();
			JSONArray jsonData = (JSONArray) parser
					.parse(((JSONObject) parser.parse(sb.toString())).get("apis").toString());
			JSONArray finalData = new JSONArray();
			JSONObject jsonElement = null;
			TelkomApiData apiData = new TelkomApiData();

			Gson gson = new Gson();
			for (int i = 0; i < jsonData.size(); i++) {
				jsonElement = (JSONObject) jsonData.get(i);
				apiData.setAPI_Name(jsonElement.get("name").toString());
				apiData.setAPI_Ver(jsonElement.get("version").toString());
				apiData.setAPI_Mgr_ID(1);
				finalData.add(parser.parse(gson.toJson(apiData)));
			}
			json.put("data", finalData);
		}
		return json;
	}

	@GET
	@Path("/getApimgrData")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getApimgrData() throws Exception {
		JSONObject json = new JSONObject();
		BridgeDatabaseService database = null;
		try {
			database = new BridgeDatabaseService();
			List<TelkomApiManager> data = database.getAll(TelkomApiManager.class);
			JSONObject[] jsondata = new JSONObject[data.size()];
			for (int i = 0; i < data.size(); i++) {
				jsondata[i] = (JSONObject) new JSONParser().parse(new GsonBuilder().create().toJson(data.get(i)));
			}
			json.put("error", "false");
			json.put("data", jsondata);
		} catch (Exception e) {
			e.printStackTrace();
			json.put("error", "true");
			json.put("message", "Exception Happened in Server, Check Stacktrace");
		} finally {
			if (database != null && database.getSession() != null)
				database.closeSession();
		}
		return Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();
	}

	@GET
	@Path("/getApicategoryData")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getApicategoryData() {
		JSONObject json = new JSONObject();
		BridgeDatabaseService database = null;
		try {
			database = new BridgeDatabaseService();
			List<TelkomAPI_Categories> data = database.getAll(TelkomAPI_Categories.class);
			JSONObject[] jsondata = new JSONObject[data.size()];
			for (int i = 0; i < data.size(); i++) {
				jsondata[i] = (JSONObject) new JSONParser().parse(new GsonBuilder().create().toJson(data.get(i)));
			}
			json.put("error", "false");
			json.put("data", jsondata);
		} catch (Exception e) {
			json.put("error", "true");
			json.put("message", "Error in getApicategoryData()");
			e.printStackTrace();
		} finally {
			if (database != null && database.getSession() != null)
				database.closeSession();
		}
		return Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();
	}

	@GET
	@Path("/delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@QueryParam("datatype") String datatype, @QueryParam("id") String id) throws Exception {
		BridgeDatabaseService database = null;
		JSONObject json = new JSONObject();
		try {
			database = new BridgeDatabaseService();
			Object obj = database.load(Class.forName("com.telkom.apiDatabaseInterface.pojo." + datatype.toString()),
					Integer.parseInt(id));
			database.delete(obj);
			json.put("error", "false");
			json.put("message", "Row number " + id + " Deleted");
		} catch (Exception e) {
			json.put("error", "true");
			json.put("message", "Invalid Data");
			e.printStackTrace();
		} finally {
			if (database != null && database.getSession() != null)
				database.closeSession();
		}
		return Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();
	}

	@SuppressWarnings("rawtypes")
	@GET
	@Path("/getApiData")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getApiData(@QueryParam("api") String apiName, @QueryParam("version") String version,
			@QueryParam("cache") String cache) {
		JSONObject json = new JSONObject();
		BridgeDatabaseService database = null;
		try {
			if (apiName != null && version != null) {
				if (cache == null || cache.equalsIgnoreCase("true")) {
					database = new BridgeDatabaseService();
					Map<String, Object> keyMap = new HashMap<String, Object>();
					keyMap.put("API_Name", apiName);
					keyMap.put("API_Ver", version);
					List<TelkomApiData> data = database.loadBykey(TelkomApiData.class, keyMap);
					if (!data.isEmpty()) {
						JSONObject jsondata = (JSONObject) new JSONParser().parse(new Gson().toJson(data.get(0)));
						json.put("error", "false");
						json.put("data", jsondata);
					} else {
						json.put("error", "true");
					}
				} else {
					HttpURLConnection conn = null;
					String urlPath = WSO2_BASE_URL
							+ "/WSO2GatewayPlugin/rest/WSO2PublisherService/getApiDetails?API_NAME=" + apiName
							+ "&version=" + version + "&provider=master@mainapi.net";
					URL url = new URL(urlPath);
					conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("POST");
					conn.connect();
					String response = "";
					if (conn.getResponseCode() == HttpsURLConnection.HTTP_OK) {
						String line;
						BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
						while ((line = br.readLine()) != null) {
							response += line;
						}
					}
					if (!response.equals("")) {
						JSONObject jsonData = (JSONObject) new JSONParser().parse(response);
						TelkomApiData apiData = new TelkomApiData();
						apiData.setAPI_Name(jsonData.get("name").toString());
						apiData.setAPI_Short_Desc(jsonData.get("description").toString());
						apiData.setAPI_Ver(jsonData.get("version").toString());
						apiData.setAPI_Status(jsonData.get("status").toString());
						apiData.setAPI_Mgr_ID(1);
						Map keyMap = new HashMap<String, Object>();
						keyMap.put("API_Name", apiData.getAPI_Name());
						database = new BridgeDatabaseService();
						List<TelkomApiData> api_data = database.loadBykey(TelkomApiData.class, keyMap);
						int api_id = 0;
						if (!api_data.isEmpty())
							api_id = api_data.get(0).getAPI_ID();
						keyMap.remove("API_Name");
						keyMap.put("API_ID", api_id);
						List<Telkomapi_Cat_Mapping> cat_data = database.loadBykey(Telkomapi_Cat_Mapping.class, keyMap);
						List<Telkomapi_Role_Mapping> role_data = database.loadBykey(Telkomapi_Role_Mapping.class,
								keyMap);
						if (!role_data.isEmpty()) {
							String role_ids = "";
							for (int i = 0; i < role_data.size(); i++) {
								role_ids += role_data.get(i).getROLE_ID();
								if (i < (role_data.size() - 1))
									role_ids += ",";
							}
							json.put("ROLE_IDS", role_ids);
						}
						if (!cat_data.isEmpty()) {
							String cat_ids = "";
							for (int i = 0; i < cat_data.size(); i++) {
								cat_ids += cat_data.get(i).getCAT_ID();
								if (i < (cat_data.size() - 1))
									cat_ids += ",";
							}
							json.put("CAT_IDS", cat_ids);
						}
						json.put("status", apiData.getAPI_Status());
						Gson gson = new Gson();
						json.put("error", "false");
						json.put("data", (JSONObject) new JSONParser().parse(gson.toJson(apiData)));
					} else {
						json.put("error", "true");
						json.put("message", "Invalid Parameters");
					}
				}
			} else {
				json.put("error", "true");
				json.put("message", "Please follow correct syntax");
			}
		} catch (Exception e) {
			json.put("error", "true");
			json.put("message", "Error in getApiData()");
			e.printStackTrace();
		} finally {
			if (database != null && database.getSession() != null)
				database.closeSession();
		}
		return Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();
	}

	@GET
	@Path("/getAccessCode")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAccessCode(@QueryParam("clientId") String clientId,
			@QueryParam("clientSecret") String clientSecret, @QueryParam("userid") String userid,
			@QueryParam("validity_period") String validity_period) throws Exception {
		BridgeDatabaseService database = null;
		JSONObject json = null;
		try {
			database = new BridgeDatabaseService();
			Map<String, Object> keyMap = new HashMap<String, Object>();
			keyMap.put("User_ID", userid);
			List<Telkomapi_Users> users = database.loadBykey(Telkomapi_Users.class, keyMap);
			String password = "";
			if (!users.isEmpty()) {
				Telkomapi_Users user = users.get(0);
				System.out.println(user);
				password = user.getPassword();
				if (clientId != null && clientSecret != null) {
					HttpURLConnection c = null;
					URL u = new URL(WSO2_BASE_URL + "/WSO2GatewayPlugin/rest/WSO2TokenService/getAccessCode?clientId="
							+ clientId + "&clientSecret=" + clientSecret + "&userid=" + userid + "&password=" + password
							+ "&validity_period=" + validity_period);
					c = (HttpURLConnection) u.openConnection();
					c.setRequestMethod("POST");
					c.setRequestProperty("Content-length", "0");
					c.setUseCaches(false);
					c.setAllowUserInteraction(false);
					c.connect();
					int status = c.getResponseCode();
					StringBuilder sb = null;
					System.out.println(status);
					switch (status) {
					case 200:
					case 201:
						BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
						sb = new StringBuilder();
						String line;
						while ((line = br.readLine()) != null) {
							sb.append(line + "\n");
						}
						br.close();
						json = (JSONObject) new JSONParser().parse(sb.toString());
						if (json.get("data") != null) {
							String accesscode = ((JSONObject) new JSONParser().parse(json.get("data").toString()))
									.get("access_token").toString();
							String expires_in = ((JSONObject) new JSONParser().parse(json.get("data").toString()))
									.get("expires_in").toString();
							user.setAccess_Code(accesscode);
							user.setValidity(expires_in);
							database.add(user);
						}
						System.out.println(json);
						if (json.containsKey("data")) {
							json.put("error", "false");
						} else {
							json.put("error", "true");
							json.put("message", "Error While generating Access Code");
						}
					}
				} else {
					json = new JSONObject();
					json.put("error", "true");
					json.put("message", "Invalid Data, Please follow syntax");
				}
			} else {
				json = new JSONObject();
				json.put("error", "true");
				json.put("message", "Invalid User");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (database != null && database.getSession() != null)
				database.closeSession();
		}
		return Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();
	}

	@POST
	@Path("/userSignup")
	@Produces(MediaType.APPLICATION_JSON)
	public Response userSignup(@QueryParam("username") String username, @QueryParam("firstName") String firstName,
			@QueryParam("lastName") String lastName, @QueryParam("email") String email) throws Exception {
		firstName = firstName.replace(" ", "");
		lastName = lastName.replace(" ", "");
		JSONObject json = null;
		MainBridgeController bridge = new MainBridgeController();
		BridgeDatabaseService database = null;
		try {
			String password = firstName + "WSO2";
			if (username != null && password != null && email != null) {
				Response response = bridge.getAllSubcriptions("mediation.hub@mainapi.net");
				JSONObject accessJson = (JSONObject) bridge.getAccessCode("uSvipu7NDya7eIwynLFyyGLnCkIa",
						"UArgfMBm9tvCYjy_a3ZU3lw6zMUa", "mediation.hub@mainapi.net", "3600").getEntity();
				JSONObject jsonResponse = (JSONObject) response.getEntity();
				System.out.println(accessJson);
				if (jsonResponse != null) {

				}

				HttpURLConnection c = null;
				String urlPath = WSO2_BASE_URL + "/WSO2GatewayPlugin/rest/WSO2StoreService/userSignupbyApi?accesscode="
						+ ((JSONObject) new JSONParser().parse(accessJson.get("data").toString())).get("access_token")
						+ "&email=" + email + "&password=" + password + "&fullname=" + firstName;
				URL u = new URL(urlPath);
				System.out.println(urlPath);
				c = (HttpURLConnection) u.openConnection();
				c.setRequestMethod("POST");
				c.setDoOutput(true);
				c.setUseCaches(false);
				c.setAllowUserInteraction(false);
				System.out.println(c.getRequestProperties());
				int status = c.getResponseCode();
				StringBuilder sb = null;
				System.out.println(status);
				System.out.println(sb);
				switch (status) {
				case 200:
				case 201:
					BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
					sb = new StringBuilder();
					String line;
					while ((line = br.readLine()) != null) {
						sb.append(line + "\n");
					}
					br.close();
					System.out.println(sb);
					json = (JSONObject) new JSONParser().parse(sb.toString());
					if (json.containsKey("res")) {
						Telkomapi_Users userdata = new Telkomapi_Users();
						userdata.setUser_ID(email);
						userdata.setPassword(password);
						database = new BridgeDatabaseService();
						database.add(userdata);
						json.put("error", "false");
					} else {
						json.put("error", "true");
						json.put("message", "Error While Registering User");
					}
				default:
					json = new JSONObject();
					json.put("error", "true");
					json.put("message", "Error While Registering User");
				}

			} else {
				json = new JSONObject();
				json.put("error", "true");
				json.put("message", "Invalid Data, Please follow syntax");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (database != null && database.getSession() != null)
				database.closeSession();
		}
		return Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();
	}

	@POST
	@Path("/addSubcriptions")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addSubcriptions(@QueryParam("apiName") String apiName, @QueryParam("apiVersion") String apiVersion,
			@QueryParam("tier") String tier, @QueryParam("applicationId") String applicationId,
			@QueryParam("userid") String userid) throws Exception {
		JSONObject json = null;
		try {
			BridgeDatabaseService database = new BridgeDatabaseService();
			Map<String, Object> keyMap = new HashMap<String, Object>();
			keyMap.put("User_ID", userid);
			List<Telkomapi_Users> users = database.loadBykey(Telkomapi_Users.class, keyMap);
			if (!users.isEmpty()) {
				String password = users.get(0).getPassword();
				if (apiName != null && apiVersion != null && tier != null && applicationId != null) {
					HttpURLConnection c = null;
					URL u = new URL(WSO2_BASE_URL + "/WSO2GatewayPlugin/rest/WSO2StoreService/addSubcriptions?apiName="
							+ apiName + "&apiVersion=" + apiVersion + "&tier=" + tier + "&applicationId="
							+ applicationId + "&userid=" + userid + "&password=" + password);
					c = (HttpURLConnection) u.openConnection();
					c.setRequestMethod("POST");
					c.setRequestProperty("Content-length", "0");
					c.setUseCaches(false);
					c.setAllowUserInteraction(false);
					c.connect();
					int status = c.getResponseCode();
					StringBuilder sb = null;
					switch (status) {
					case 200:
					case 201:
						BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
						sb = new StringBuilder();
						String line;
						while ((line = br.readLine()) != null) {
							sb.append(line + "\n");
						}
						br.close();
						System.out.println("While Adding Subscription" + sb);
						json = (JSONObject) new JSONParser().parse(sb.toString());
						if (json.containsKey("error") && !json.get("error").toString().equals("true")) {
							json.put("error", "false");
						} else {
							json.put("error", "true");
							json.put("message", "Error While Adding Subscription");
						}
					}
				} else {
					json = new JSONObject();
					json.put("error", "true");
					json.put("message", "Invalid Data, Please follow syntax");
				}
			} else {
				json = new JSONObject();
				json.put("error", "true");
				json.put("message", "Invalid User");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();
	}

	@POST
	@Path("/removeSubcriptions")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeSubcriptions(@QueryParam("apiName") String apiName,
			@QueryParam("apiVersion") String apiVersion, @QueryParam("applicationId") String applicationId,
			@QueryParam("userid") String userid) throws Exception {
		JSONObject json = null;
		try {
			BridgeDatabaseService database = new BridgeDatabaseService();
			Map<String, Object> keyMap = new HashMap<String, Object>();
			keyMap.put("User_ID", userid);
			List<Telkomapi_Users> users = database.loadBykey(Telkomapi_Users.class, keyMap);
			if (!users.isEmpty()) {
				String password = users.get(0).getPassword();
				if (apiName != null && apiVersion != null && applicationId != null) {
					HttpURLConnection c = null;
					URL u = new URL(
							WSO2_BASE_URL + "/WSO2GatewayPlugin/rest/WSO2StoreService/removeSubcriptions?apiName="
									+ apiName + "&apiVersion=" + apiVersion + "&applicationId=" + applicationId
									+ "&userid=" + userid + "&password=" + password);
					c = (HttpURLConnection) u.openConnection();
					c.setRequestMethod("POST");
					c.setRequestProperty("Content-length", "0");
					c.setUseCaches(false);
					c.setAllowUserInteraction(false);
					c.connect();
					int status = c.getResponseCode();
					StringBuilder sb = null;
					switch (status) {
					case 200:
					case 201:
						BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
						sb = new StringBuilder();
						String line;
						while ((line = br.readLine()) != null) {
							sb.append(line + "\n");
						}
						br.close();
						System.out.println("While Removing Subscription" + sb);
						json = (JSONObject) new JSONParser().parse(sb.toString());
						if (json.containsKey("error") && !json.get("error").toString().equals("true")) {
							json.put("error", "false");
						} else {
							json.put("error", "true");
							json.put("message", "Error While Removing Subsriptions");
						}
					}
				} else {
					json = new JSONObject();
					json.put("error", "true");
					json.put("message", "Invalid Data, Please follow syntax");
				}
			} else {
				json = new JSONObject();
				json.put("error", "false");
				json.put("message", "Invalid User");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();
	}

	@POST
	@Path("/getAllSubcriptions")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllSubcriptions(@QueryParam("userid") String userid) throws Exception {
		JSONObject json = null;
		try {
			System.out.println(userid);
			BridgeDatabaseService database = new BridgeDatabaseService();
			Map<String, Object> keyMap = new HashMap<String, Object>();
			keyMap.put("User_ID", userid);
			List<Telkomapi_Users> users = database.loadBykey(Telkomapi_Users.class, keyMap);
			if (!users.isEmpty()) {
				String password = users.get(0).getPassword();
				System.out.println(password);
				HttpURLConnection c = null;
				System.out.println(userid);
				System.out.println(password);
				URL u = new URL(WSO2_BASE_URL + "/WSO2GatewayPlugin/rest/WSO2StoreService/getAllSubcriptions?userid="
						+ userid + "&password=" + password);
				c = (HttpURLConnection) u.openConnection();
				c.setRequestMethod("POST");
				c.setRequestProperty("Content-length", "0");
				c.setUseCaches(false);
				c.setAllowUserInteraction(false);
				c.connect();
				int status = c.getResponseCode();
				StringBuilder sb = null;
				System.out.println(status);
				switch (status) {
				case 200:
				case 201:
					BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
					sb = new StringBuilder();
					String line;
					while ((line = br.readLine()) != null) {
						sb.append(line + "\n");
					}
					br.close();
					json = (JSONObject) new JSONParser().parse(sb.toString());
					if (json.containsKey("subscriptions")) {
						json.put("error", "false");
					} else {
						json.put("error", "true");
						json.put("message", "Error While Getting All Subscriptions");
					}
				}
			} else {
				json = new JSONObject();
				json.put("error", "true");
				json.put("message", "Invalid UserId");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();
	}

	@POST
	@Path("/getAllApplications")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllApplications(@QueryParam("userid") String userid) throws Exception {
		JSONObject json = null;
		try {
			BridgeDatabaseService database = new BridgeDatabaseService();
			Map<String, Object> keyMap = new HashMap<String, Object>();
			keyMap.put("User_ID", userid);
			List<Telkomapi_Users> users = database.loadBykey(Telkomapi_Users.class, keyMap);
			if (!users.isEmpty()) {
				String password = users.get(0).getPassword();
				HttpURLConnection c = null;
				URL u = new URL(WSO2_BASE_URL + "/WSO2GatewayPlugin/rest/WSO2StoreService/getAllApplications?userid="
						+ userid + "&password=" + password);
				c = (HttpURLConnection) u.openConnection();
				c.setRequestMethod("POST");
				c.setRequestProperty("Content-length", "0");
				c.setUseCaches(false);
				c.setAllowUserInteraction(false);
				c.connect();
				int status = c.getResponseCode();
				StringBuilder sb = null;
				switch (status) {
				case 200:
				case 201:
					BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
					sb = new StringBuilder();
					String line;
					while ((line = br.readLine()) != null) {
						sb.append(line + "\n");
					}
					br.close();
					json = (JSONObject) new JSONParser().parse(sb.toString());
					if (json.containsKey("applications")) {
						json.put("error", "false");
					} else {
						json.put("error", "true");
						json.put("message", "Error While getting user Application");
					}
				}
			} else {
				json = new JSONObject();
				json.put("error", "true");
				json.put("message", "Invalid User");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();
	}

	@POST
	@Path("/generateApplicationKey")
	@Produces(MediaType.APPLICATION_JSON)
	public Response generateApplicationKey(@QueryParam("keytype") String keytype,
			@QueryParam("callbackUrl") String callbackUrl, @QueryParam("authorizedDomains") String authorizedDomains,
			@QueryParam("validityTime") String validityTime, @QueryParam("userid") String userid) throws Exception {
		JSONObject json = null;
		try {
			BridgeDatabaseService database = new BridgeDatabaseService();
			Map<String, Object> keyMap = new HashMap<String, Object>();
			keyMap.put("User_ID", userid);
			List<Telkomapi_Users> users = database.loadBykey(Telkomapi_Users.class, keyMap);
			if (!users.isEmpty()) {
				String password = users.get(0).getPassword();
				HttpURLConnection c = null;
				URL u = new URL(
						WSO2_BASE_URL + "/WSO2GatewayPlugin/rest/WSO2StoreService/generateApplicationKey?keytype="
								+ keytype + "&callbackUrl=" + callbackUrl + "&authorizedDomains=" + authorizedDomains
								+ "&validityTime=" + validityTime + "&userid=" + userid + "&password=" + password);
				c = (HttpURLConnection) u.openConnection();
				c.setRequestMethod("POST");
				c.setRequestProperty("Content-length", "0");
				c.setUseCaches(false);
				c.setAllowUserInteraction(false);
				c.connect();
				int status = c.getResponseCode();
				StringBuilder sb = null;
				switch (status) {
				case 200:
				case 201:
					BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
					sb = new StringBuilder();
					String line;
					while ((line = br.readLine()) != null) {
						sb.append(line + "\n");
					}
					br.close();
					json = (JSONObject) new JSONParser().parse(sb.toString());
					if (json.containsKey("applications")) {
						json.put("error", "false");
					} else {
						json.put("error", "true");
						json.put("message", "Error While getting user Application");
					}
				}
			} else {
				json = new JSONObject();
				json.put("error", "true");
				json.put("message", "Invalid User");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();
	}

	@POST
	@Path("/addUser")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addUser(@QueryParam("userid") String userid, @QueryParam("firstname") String firstName,
			@QueryParam("lastname") String lastName, @QueryParam("email") String email) throws Exception {
		JSONObject json = null;
		try {
			if (userid != null && !userid.equals("")) {
				Map<String, Object> keyMap = new HashMap<String, Object>();
				keyMap.put("User_ID", userid);
				BridgeDatabaseService database = new BridgeDatabaseService();
				List<Telkomapi_Users> list = database.loadBykey(Telkomapi_Users.class, keyMap);
				if (list.isEmpty()) {
					MainBridgeController bridge = new MainBridgeController();
					Response response = bridge.userSignup(userid, firstName, lastName, email);
					JSONObject responseData = (JSONObject) response.getEntity();
					System.out.println("UserSignup");
					if (responseData.containsKey("error") && responseData.get("error").toString().equals("false")) {
						Telkomapi_Users user = new Telkomapi_Users();
						user.setUser_ID(userid);
						user.setPassword(firstName + "WSO2");
						database.add(user);
					}
					json = new JSONObject();
					json.put("error", "false");
					json.put("message", "User Signup Successfull");
				} else {
					json = new JSONObject();
					json.put("error", "true");
					json.put("message", "User Already Exists");
				}
			} else {
				json = new JSONObject();
				json.put("error", "true");
				json.put("message", "Invalid UserId");
			}

		} catch (Exception e) {
			e.printStackTrace();
			json = new JSONObject();
			json.put("error", "true");
			json.put("message", "Error in addUser(). Check Stacktrace");
		}
		return Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();
	}

	@GET
	@Path("/getPricingData")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPricingData(@QueryParam("apiName") String apiName, @QueryParam("version") String version)
			throws Exception {
		JSONArray json = null;
		JSONObject jsonData = new JSONObject();
		try {
			MainBridgeController bridge = new MainBridgeController();
			BridgeDatabaseService database = new BridgeDatabaseService();
			Map<String, Object> keyMap = new HashMap<String, Object>();
			keyMap.put("User_ID", "mediation.hub@mainapi.net");
			List<Telkomapi_Users> users = database.loadBykey(Telkomapi_Users.class, keyMap);
			if (!users.isEmpty()) {
				JSONObject subJson = (JSONObject) bridge.getAllSubcriptions("mediation.hub@mainapi.net").getEntity();
				JSONObject subData = (JSONObject) ((JSONArray) ((JSONObject) subJson.get("subscriptions"))
						.get("applications")).get(0);
				System.out.println(subData);
				JSONObject accesscodeRes = (JSONObject) ((JSONObject) bridge
						.getAccessCode(subData.get("prodConsumerKey").toString(),
								subData.get("prodConsumerSecret").toString(), users.get(0).getUser_ID(), "-1")
						.getEntity()).get("data");
				System.out.println(accesscodeRes);
				HttpURLConnection c = null;
				String urlData = WSO2_BASE_URL + "/WSO2GatewayPlugin/rest/WSO2StoreService/getPricingData?apiName="
						+ apiName + "&version=" + version + "&accesscode=" + accesscodeRes.get("access_token");
				System.out.println(urlData);
				URL u = new URL(urlData);
				c = (HttpURLConnection) u.openConnection();
				c.setRequestMethod("POST");
				c.setRequestProperty("Content-length", "0");
				c.setUseCaches(false);
				c.setAllowUserInteraction(false);
				c.connect();
				int status = c.getResponseCode();
				StringBuilder sb = null;
				switch (status) {
				case 200:
				case 201:
					BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
					sb = new StringBuilder();
					String line;
					while ((line = br.readLine()) != null) {
						sb.append(line + "\n");
					}
					br.close();
					json = (JSONArray) new JSONParser().parse(sb.toString());
					System.out.println(json);
					if (json.isEmpty()) {
						jsonData.put("error", "true");
						jsonData.put("message", "Error While getting user Application");
						return Response.status(200).entity(jsonData).header("Access-Control-Allow-Origin", "*").build();
					}
				}
			} else {
				jsonData = new JSONObject();
				jsonData.put("error", "true");
				jsonData.put("message", "Invalid User");
				return Response.status(200).entity(jsonData).header("Access-Control-Allow-Origin", "*").build();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();
	}

	public static void main(String[] args) throws Exception {
		MainBridgeController bridgeController = new MainBridgeController();
		bridgeController.getPricingData("SMSNotification", "1.0.0");
		// System.out.println("FINAL DATA : " + bridgeController
		// .getAccessCode("whmNIcoAoH2ymTsi_qJvgcSyfkwa",
		// "IxZtkpABvETjvYwAJByNz2iMPnUa", "test@liferay.com", "30")
		// .getEntity());
		// System.out.println(bridgeController.addUser("suman.das2", "Suman",
		// "Kumar Das", "suman.das2@wipro.com"));
		// JSONObject jsonApi = (JSONObject) new
		// JSONParser().parse("{\"API_ID\":\"123\",\"API_Name\":\"TESTING\"}");
		// TelkomApiData api = new TelkomApiData();
		// Iterator iterator = jsonApi.keySet().iterator();
		// while (iterator.hasNext()) {
		// String field = (String)iterator.next();
		//
		// Field fieldData = TelkomApiData.class.getDeclaredField(field);
		// if (fieldData.getName() != "API_ID") {
		// Method method = TelkomApiData.class.getMethod("set" + field,
		// fieldData.getType());
		// Class classData = fieldData.getType();
		// if (classData == Boolean.class) {
		// method.invoke(api,
		// Boolean.parseBoolean(jsonApi.get(field).toString().trim()));
		// } else if (classData == int.class) {
		// method.invoke(api,
		// Integer.parseInt(jsonApi.get(field).toString().trim()));
		// } else if (classData == boolean.class) {
		// method.invoke(api,
		// Boolean.parseBoolean(jsonApi.get(field).toString().trim()));
		// } else if (classData == byte.class) {
		// method.invoke(api,
		// Byte.parseByte(jsonApi.get(field).toString().trim()));
		// } else if (classData == char.class) {
		// method.invoke(api, jsonApi.get(field).toString().trim().charAt(0));
		// } else if (classData == short.class) {
		// method.invoke(api,
		// Short.parseShort(jsonApi.get(field).toString().trim()));
		// } else if (classData == long.class) {
		// method.invoke(api,
		// Long.parseLong(jsonApi.get(field).toString().trim()));
		// } else if (classData == float.class) {
		// method.invoke(api,
		// Float.parseFloat(jsonApi.get(field).toString().trim()));
		// } else if (classData == double.class) {
		// method.invoke(api,
		// Double.parseDouble(jsonApi.get(field).toString().trim()));
		// } else {
		// method.invoke(api, classData.cast(jsonApi.get(field)));
		// }
		// }
		// }
		// System.out.println(api);
	}

}
