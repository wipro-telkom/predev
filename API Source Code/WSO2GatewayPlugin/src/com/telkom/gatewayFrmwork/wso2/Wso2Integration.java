package com.telkom.gatewayFrmwork.wso2;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.wso2.apiManager.plugin.client.APIManagerClient;
import org.wso2.apiManager.plugin.constants.APIConstants;

public class Wso2Integration {
	 Logger logger = Logger.getLogger(Wso2Integration.class.getName());

	public JSONObject getAllApis(String BASE_URL, String type) throws Exception {
		logger.info("Wso2Integration.java::getAllApis() starts...");
		APIManagerClient api = APIManagerClient.getInstance();
		org.json.simple.JSONObject jsonObject = api.getAllPublishedAPIs(BASE_URL, Wso2Constants.GETAPI_USERNAME,
				Wso2Constants.GETAPI_USERPASS, "", "1.0.0", type);
		return jsonObject;
	}

	public JSONObject getApiDetails(String API_NAME,String API_Version,String provider) throws Exception {
		System.out.println("Wso2Integration.java::getApiDetails() starts...");
		APIManagerClient api = APIManagerClient.getInstance();
		org.json.simple.JSONObject jsonObject = api.WSO2getApi(API_NAME, Wso2Constants.GETAPI_PUBLISHER_BASE_URL,
				Wso2Constants.GETAPI_PUBLISHER_BASE_URL + APIConstants.API_PUBLISHER_API_LIST_URL, "",
				Wso2Constants.GETAPI_USERNAME, Wso2Constants.GETAPI_USERPASS,API_Version,provider);
		return jsonObject;
	}

	@SuppressWarnings("unchecked")
	public JSONObject userSignup(String getapiStoreBaseUrl, String userName, String password, String firstName,
			String lastName, String email) {
		APIManagerClient api = APIManagerClient.getInstance();
		JSONObject json = new JSONObject();
		try {
			json = api.userSignup(Wso2Constants.GETAPI_STORE_BASE_URL,
					Wso2Constants.GETAPI_STORE_BASE_URL + APIConstants.API_STORE_API_LIST_URL, userName,
					password, firstName, lastName, email);
		} catch (Exception e) {
			json.put("error", "true");
			json.put("message", e.getMessage());
		}
		return json;
	}
	


	public JSONObject logout(String type) throws Exception {
		APIManagerClient api = APIManagerClient.getInstance();
		JSONObject json = new JSONObject();
		json = api.logout(Wso2Constants.GETAPI_STORE_BASE_URL,type);
		return json;
	}

	public static void main(String[] args) throws Exception {
//		Wso2Integration obj = new Wso2Integration();
		try {
			// Publisher API
//			obj.getAllApis(Wso2Constants.GETAPI_PUBLISHER_BASE_URL, "Publisher");
//			APIManagerClient api = APIManagerClient.getInstance();
//			System.out.println(api.WSO2getApi("WorldBank", Wso2Constants.GETAPI_PUBLISHER_BASE_URL,
//					Wso2Constants.GETAPI_PUBLISHER_BASE_URL + APIConstants.API_PUBLISHER_API_LIST_URL, "",
//					Wso2Constants.GETAPI_USERNAME, Wso2Constants.GETAPI_USERPASS).toJSONString());

			// Store API
//			obj.test();
//			 obj.getAllApis(Wso2Constants.GETAPI_STORE_BASE_URL,"Store");
//			System.out.println("TEST"+obj.userSignup(Wso2Constants.GETAPI_STORE_BASE_URL, "sashdgasgdasgjdfajsgdasjdas686879123", "Pasasdasdaswsafasford!!", "Sasfudasdasdipta", "Paafsl",
//					"asdkhagsdjkgassudipta.pallsakhdjaskdgadsdasadsajks12@wipro.com"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
