package com.telkom.gatewayFrmwork.wso2;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.wso2.apiManager.plugin.client.APIManagerClient;
import org.wso2.apiManager.plugin.constants.APIConstants;


@SuppressWarnings("unchecked")
public class Wso2Integration {
	Logger logger = Logger.getLogger(Wso2Integration.class.getName());

	public JSONObject getAllApis(String BASE_URL, String type) throws Exception {
		logger.info("Wso2Integration.java::getAllApis() starts...");
		APIManagerClient api = APIManagerClient.getInstance();
		org.json.simple.JSONObject jsonObject = api.getAllPublishedAPIs(BASE_URL, Wso2Constants.GETAPI_USERNAME,
				Wso2Constants.GETAPI_USERPASS, "", "1.0.0", type);
		return jsonObject;
	}

	public JSONObject getApiDetails(String API_NAME, String API_Version, String provider) throws Exception {
		System.out.println("Wso2Integration.java::getApiDetails() starts...");
		APIManagerClient api = APIManagerClient.getInstance();
		org.json.simple.JSONObject jsonObject = api.WSO2getApi(API_NAME, Wso2Constants.GETAPI_PUBLISHER_BASE_URL,
				Wso2Constants.GETAPI_PUBLISHER_BASE_URL + APIConstants.API_PUBLISHER_API_LIST_URL, "",
				Wso2Constants.GETAPI_USERNAME, Wso2Constants.GETAPI_USERPASS, API_Version, provider);
		return jsonObject;
	}

	public JSONObject userSignup(String getapiStoreBaseUrl, String userName, String password, String firstName,
			String lastName, String email) {
		APIManagerClient api = APIManagerClient.getInstance();
		JSONObject json = new JSONObject();
		try {
			json = api.userSignup(Wso2Constants.GETAPI_STORE_BASE_URL,
					Wso2Constants.GETAPI_STORE_BASE_URL + APIConstants.API_STORE_API_LIST_URL, userName, password,
					firstName, lastName, email);
		} catch (Exception e) {
			json.put("error", "true");
			json.put("message", e.getMessage());
		}
		return json;
	}

	public JSONObject logout(String type) throws Exception {
		APIManagerClient api = APIManagerClient.getInstance();
		JSONObject json = new JSONObject();
		json = api.logout(Wso2Constants.GETAPI_STORE_BASE_URL, type);
		return json;
	}

	public JSONObject getApplications() {
		APIManagerClient api = APIManagerClient.getInstance();
		JSONObject json = new JSONObject();
		try {
			json = api.getApplications(Wso2Constants.GETAPI_STORE_BASE_URL);
		} catch (Exception e) {
			json.put("error", "true");
			json.put("message", e.getMessage());
		}
		return json;
	}

	public JSONObject getAllSubcriptions() {
		APIManagerClient api = APIManagerClient.getInstance();
		JSONObject json = new JSONObject();
		try {
			json = api.getAllSubcriptions(Wso2Constants.GETAPI_STORE_BASE_URL);
		} catch (Exception e) {
			json.put("error", "true");
			json.put("message", e.getMessage());
		}
		return json;
	}

	public static void main(String[] args) throws Exception {
		Wso2Integration obj = new Wso2Integration();
		try {
			System.out.println(obj.removeSubcriptions("wifi.id_Locator", "0.0.1", "689"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public JSONObject removeSubcriptions(String apiName, String apiVersion, String applicationId) {
		APIManagerClient api = APIManagerClient.getInstance();
		JSONObject json = new JSONObject();
		try {
			json = api.removeSubcriptions(Wso2Constants.GETAPI_STORE_BASE_URL, apiName, apiVersion, applicationId);
		} catch (Exception e) {
			json.put("error", "true");
			json.put("message", e.getMessage());
		}
		return json;
	}

	public JSONObject addSubcriptions(String apiName, String apiVersion, String applicationId, String tier) {
		APIManagerClient api = APIManagerClient.getInstance();
		JSONObject json = new JSONObject();
		try {
			json = api.addSubcriptions(Wso2Constants.GETAPI_STORE_BASE_URL, apiName, apiVersion, applicationId, tier);
		} catch (Exception e) {
			json.put("error", "true");
			json.put("message", e.getMessage());
		}
		return json;
	}

}
