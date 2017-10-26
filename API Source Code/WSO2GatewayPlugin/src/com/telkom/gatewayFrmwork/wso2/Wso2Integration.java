package com.telkom.gatewayFrmwork.wso2;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
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
		logger.info("Wso2Integration.java::getApiDetails() starts...");
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

	public JSONObject getApplications(String username, String password) {
		APIManagerClient api = APIManagerClient.getInstance();
		JSONObject json = new JSONObject();
		try {
			json = api.getApplications(Wso2Constants.GETAPI_STORE_BASE_URL, username, password);
		} catch (Exception e) {
			json.put("error", "true");
			json.put("message", e.getMessage());
		}
		return json;
	}

	public JSONObject getAllSubcriptions(String userid, String password) {
		APIManagerClient api = APIManagerClient.getInstance();
		JSONObject json = new JSONObject();
		try {
			json = api.getAllSubcriptions(Wso2Constants.GETAPI_STORE_BASE_URL, userid, password);
		} catch (Exception e) {
			json.put("error", "true");
			json.put("message", e.getMessage());
		}
		return json;
	}

	public JSONObject removeSubcriptions(String apiName, String apiVersion, String applicationId, String userid,
			String password) {
		APIManagerClient api = APIManagerClient.getInstance();
		JSONObject json = new JSONObject();
		try {
			json = api.removeSubcriptions(Wso2Constants.GETAPI_STORE_BASE_URL, apiName, apiVersion, applicationId,
					userid, password);
		} catch (Exception e) {
			json.put("error", "true");
			json.put("message", e.getMessage());
		}
		return json;
	}

	public JSONObject addSubcriptions(String apiName, String apiVersion, String applicationId, String tier,
			String userid, String password) {
		APIManagerClient api = APIManagerClient.getInstance();
		JSONObject json = new JSONObject();
		try {
			json = api.addSubcriptions(Wso2Constants.GETAPI_STORE_BASE_URL, apiName, apiVersion, applicationId, tier,
					userid, password);
		} catch (Exception e) {
			json.put("error", "true");
			json.put("message", e.getMessage());
		}
		return json;
	}

	public JSONObject getAccessCode(String getapiTokenBaseUrl, String clientId, String clientSecret, String userid,
			String password, String validity_period) {
		APIManagerClient api = APIManagerClient.getInstance();
		JSONObject json = new JSONObject();
		try {
			json = api.getAccessCode(Wso2Constants.GETAPI_TOKEN_BASE_URL, clientId, clientSecret, userid, password,
					validity_period);
		} catch (Exception e) {
			json.put("error", "true");
			json.put("message", e.getMessage());
		}
		return json;
	}

	public JSONObject userSignupbyApi(String accesscode, String password, String firstName, String email) {
		APIManagerClient api = APIManagerClient.getInstance();
		JSONObject json = new JSONObject();
		try {
			json = api.userSignupbyApi(accesscode, password, firstName, email);
		} catch (Exception e) {
			json.put("error", "true");
			json.put("message", e.getMessage());
		}
		return json;
	}

	public JSONArray getPricingData(String provider, String apiName, String version, String accesscode) {
		APIManagerClient api = APIManagerClient.getInstance();
		JSONArray json = new JSONArray();
		try {
			json = api.getPricingData(provider, apiName, version, accesscode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	public JSONObject generateApplicationKey(String keytype, String callbackUrl, String authorizedDomains,
			String userid, String password) {
		APIManagerClient api = APIManagerClient.getInstance();
		JSONObject json = new JSONObject();
		try {
			json = api.generateApplicationKey(keytype, callbackUrl, authorizedDomains, userid, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	public static void main(String[] args) throws Exception {
	}

}
