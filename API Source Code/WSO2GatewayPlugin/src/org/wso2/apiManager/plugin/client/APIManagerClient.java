/*
*  Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

package org.wso2.apiManager.plugin.client;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.support.StringUtils;
import com.telkom.gatewayFrmwork.wso2.Wso2Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.wso2.apiManager.plugin.constants.APIConstants;
import org.wso2.apiManager.plugin.dataObjects.APIInfo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for connecting to WSO2 API Manager and fetching
 * APIs and their definitions
 */
@SuppressWarnings("deprecation")
public class APIManagerClient {
	private static APIManagerClient apiManagerClient = null;
	private HttpContext httpContext = new BasicHttpContext();
	private HttpClient httpClient;

	private APIManagerClient() {
		CookieStore cookieStore = new BasicCookieStore();
		httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
	}

	public static APIManagerClient getInstance() {
		if (apiManagerClient == null) {
			apiManagerClient = new APIManagerClient();
		}
		return apiManagerClient;
	}

	/**
	 * This method will return all APIs of the given tenant domain
	 *
	 * @param storeEndpoint
	 *            The endpoint of the API Store
	 * @param userName
	 *            The tenant aware user name
	 * @param password
	 *            the password of the user
	 * @param tenantDomain
	 *            The tenant domain of the store
	 * @return list of @link{APIInfo}
	 * @throws java.lang.Exception
	 *             if any error occurs
	 */
	public JSONObject getAllPublishedAPIs(String storeEndpoint, String userName, char[] password, String tenantDomain,
			String productVersion, String type) throws Exception {
		List<APIInfo> apiList = new ArrayList<APIInfo>();
		org.json.simple.JSONObject jsonObject = null;
		String tenantUserName = userName;

		/*
		 * The tenant domain can be empty. If the tenant domain is not empty
		 * then we use the tenant aware user name for authentication purposes.
		 * If it empty, then we assign super tenant domain name for that.
		 */
		if (!StringUtils.isNullOrEmpty(tenantDomain)) {
			tenantUserName = constructTenantUserName(userName, tenantDomain);
		} else {
			tenantDomain = APIConstants.CARBON_SUPER;
		}

		// If the authentication process is successful
		if (authenticate(storeEndpoint, tenantUserName, password)) {
			HttpClient httpClient = getHttpClient();
			HttpPost httpPost = new HttpPost(getAPIStoreListUrl(storeEndpoint, type));
			try {
				// Request parameters and other properties.
				List<NameValuePair> params = new ArrayList<NameValuePair>(3);
				if (type == "Publisher")
					params.add(
							new BasicNameValuePair(APIConstants.API_ACTION, APIConstants.ALL_PUBLISHER_API_GET_ACTION));
				else
					params.add(new BasicNameValuePair(APIConstants.API_ACTION, APIConstants.ALL_STORE_API_GET_ACTION));
				params.add(new BasicNameValuePair("tenant", tenantDomain));
				params.add(new BasicNameValuePair("start", Integer.toString(0)));
				params.add(new BasicNameValuePair("end", Integer.toString(Integer.MAX_VALUE)));
				httpPost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));

				HttpResponse response = httpClient.execute(httpPost, httpContext);
				HttpEntity entity = response.getEntity();
				String responseString = EntityUtils.toString(entity, StandardCharsets.UTF_8);
				System.out.println(responseString);
				String[] errorSection = responseString.split(",");
				boolean isError = Boolean.parseBoolean(errorSection[0].split(":")[1].split("}")[0].trim());
				if (isError) {
					String errorMsg = errorSection[1].split(":")[1].split("}")[0].trim();
					throw new Exception("Error occurred while getting the list of APIs " + errorMsg);
				}
				org.json.simple.JSONArray apiArray;
				try {
					jsonObject = (org.json.simple.JSONObject) org.json.simple.JSONValue.parse(responseString);
					// We expect an JSON array for api list
					apiArray = (org.json.simple.JSONArray) jsonObject.get("apis");
					for (Object apiJsonObject : apiArray) {
						org.json.simple.JSONObject apiJson = (org.json.simple.JSONObject) apiJsonObject;

						String apiName = apiJson.get("name").toString();
						String apiProvider = apiJson.get("provider").toString();
						String version = apiJson.get("version").toString();
						String description = apiJson.get("description") == null ? ""
								: apiJson.get("description").toString();

						APIInfo apiInfo = new APIInfo();
						apiInfo.setName(apiName);
						apiInfo.setProvider(apiProvider);
						apiInfo.setVersion(version);
						apiInfo.setDescription(description);
						apiInfo.setSwaggerDocLink(
								getSwaggerDocLink(storeEndpoint, apiName, version, apiProvider, productVersion));
						apiInfo.setProductVersion(productVersion);

						apiList.add(apiInfo);
					}
				} catch (ClassCastException e) {
					throw new Exception("Could not parse the results. Incompatible result", e);
				}
			} finally {
				// This is done to release the connections
				httpPost.reset();
			}
		}

		return jsonObject;
	}

	/**
	 * Method to authenticate with the given API Store
	 *
	 * @param storeEndpoint
	 *            The endpoint of the API Store
	 * @param userName
	 *            the username with the tenant domain
	 * @param password
	 *            the user password
	 * @return true if authentication is successful throws Exception if any
	 *         error happens
	 */
	public boolean authenticate(String storeEndpoint, String userName, char[] password) throws Exception {
		// create a post request to addAPI.
		HttpClient httpClient = getHttpClient();
		HttpPost httpPost = new HttpPost(getAPIStoreLoginUrl(storeEndpoint));
		try {
			// Request parameters and other properties.
			List<NameValuePair> params = new ArrayList<NameValuePair>(3);

			params.add(new BasicNameValuePair(APIConstants.API_ACTION, APIConstants.API_LOGIN_ACTION));
			params.add(new BasicNameValuePair(APIConstants.API_STORE_LOGIN_USERNAME, userName));
			params.add(new BasicNameValuePair(APIConstants.API_STORE_LOGIN_PASSWORD, new String(password)));
			httpPost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
			HttpResponse response = httpClient.execute(httpPost, httpContext);
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, StandardCharsets.UTF_8);
			String[] errorSection = responseString.split(",");
			boolean isError = Boolean.parseBoolean(errorSection[0].split(":")[1].split("}")[0].trim());
			if (isError) {
				String errorMsg = errorSection[1].split(":")[1].split("}")[0].trim();
				throw new Exception(" Authentication with external APIStore -  failed due to " + errorMsg);
			} else {
				return true;
			}
		} finally {
			// This is to release the connections.
			httpPost.reset();
		}
	}

	/**
	 * Method to initialize the http client. We use only one instance of http
	 * client since there can not be concurrent invocations
	 *
	 * @return @link{HttpClient} httpClient instance
	 */
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

	/**
	 * This method will construct the tenant user name Ex:-
	 * janaka@sampleTenant.com
	 *
	 * @param userName
	 *            The user name
	 * @param tenantDomain
	 *            The tenant domain of the user
	 * @return The tenant user name
	 */
	public String constructTenantUserName(String userName, String tenantDomain) {
		return userName + APIConstants.TENANT_DOMAIN_SEPARATOR + tenantDomain;
	}

	/**
	 * This method returns the login endpoint of the store Ex:-
	 * https://localgost:9443/store/site/blocks/user/login/ajax/login.jag
	 *
	 * @param baseUrl
	 *            The endpoint of the API Store
	 * @return the login endpoint URL
	 */
	private String getAPIStoreLoginUrl(String baseUrl) {
		if (baseUrl.endsWith("/")) {
			baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf('/'));
		}
		return baseUrl + APIConstants.API_PUBLISHER_LOGIN_URL;
	}

	/**
	 * This method returns the list URL of the API Store Ex:-
	 * https://localhost:9443/store/site/blocks/api/listing/ajax/list.jag
	 *
	 * @param baseUrl
	 *            The endpoint of the API Store
	 * @return The list endpoint of the API Store
	 */
	private String getAPIStoreListUrl(String baseUrl, String type) {
		if (baseUrl.endsWith("/")) {
			baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf('/'));
		}
		if (type == "Publisher")
			return baseUrl + APIConstants.API_PUBLISHER_API_LIST_URL;
		else
			return baseUrl + APIConstants.API_STORE_API_LIST_URL;
	}

	/**
	 * This method returns the swagger doc link of the API Ex:-
	 * https://localhost:9443/store/api-docs/janaka%40janaka.com/WikipediaAPI/1.
	 * 0.0
	 *
	 * @param baseUrl
	 *            The endpoint of the API Store
	 * @param apiName
	 *            The name of the API
	 * @param apiVersion
	 *            The version of the API
	 * @param apiProvider
	 *            The provider of the API
	 * @param productVersion
	 *            The version of the API Manager
	 * @return The swagger doc link of the API
	 */
	private String getSwaggerDocLink(String baseUrl, String apiName, String apiVersion, String apiProvider,
			String productVersion) {
		if (baseUrl.endsWith("/")) {
			baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf('/'));
		}
		if ("1.8.0".equals(productVersion)) {
			try {
				apiProvider = URLEncoder.encode(apiProvider, StandardCharsets.UTF_8.name());
			} catch (UnsupportedEncodingException e) {
				SoapUI.logError(e, "Error while generating the api-docs URL ");
			}
		} else {
			apiProvider = apiProvider.replace("@", "-AT-");
		}
		return baseUrl + "/api-docs/" + apiProvider + "/" + apiName + "/" + apiVersion;
	}

	public JSONObject WSO2getApi(String API_NAME, String loginUrl, String storeEndpoint, String tenantDomain,
			String userName, char[] password, String API_Version, String provider) throws Exception {
		JSONObject jsonObject = null;
		String tenantUserName = userName;

		/*
		 * The tenant domain can be empty. If the tenant domain is not empty
		 * then we use the tenant aware user name for authentication purposes.
		 * If it empty, then we assign super tenant domain name for that.
		 */
		if (!StringUtils.isNullOrEmpty(tenantDomain)) {
			tenantUserName = constructTenantUserName(userName, tenantDomain);
		} else {
			tenantDomain = APIConstants.CARBON_SUPER;
		}

		// If the authentication process is successful
		if (authenticate(loginUrl, tenantUserName, password)) {
			HttpClient httpClient = getHttpClient();
			HttpPost httpPost = new HttpPost(storeEndpoint);
			try {
				// Request parameters and other properties.
				List<NameValuePair> params = new ArrayList<NameValuePair>(3);

				params.add(new BasicNameValuePair(APIConstants.API_ACTION, "getAPI"));
				params.add(new BasicNameValuePair("name", API_NAME));
				params.add(new BasicNameValuePair("version", API_Version));
				params.add(new BasicNameValuePair("provider", provider));
				params.add(new BasicNameValuePair("tenant", tenantDomain));
				params.add(new BasicNameValuePair("start", Integer.toString(0)));
				params.add(new BasicNameValuePair("end", Integer.toString(Integer.MAX_VALUE)));
				httpPost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
				HttpResponse response = httpClient.execute(httpPost, httpContext);
				HttpEntity entity = response.getEntity();
				String responseString = EntityUtils.toString(entity, StandardCharsets.UTF_8);
				String[] errorSection = responseString.split(",");
				boolean isError = Boolean.parseBoolean(errorSection[0].split(":")[1].split("}")[0].trim());
				if (isError) {
					String errorMsg = errorSection[1].split(":")[1].split("}")[0].trim();
					throw new Exception("Error occurred while getting API Details " + errorMsg);
				}

				try {
					jsonObject = (org.json.simple.JSONObject) org.json.simple.JSONValue.parse(responseString);
				} catch (ClassCastException e) {
					throw new Exception("Could not parse the results. Incompatible result", e);
				}
			} finally {
				// This is done to release the connections
				httpPost.reset();
			}
		}
		return jsonObject;
	}

	public JSONObject userSignup(String getapiStoreBaseUrl, String loginUrl, String userName, String password,
			String firstName, String lastName, String email) throws Exception {
		JSONObject jsonObject = null;

		// If the authentication process is successful
		HttpClient httpClient = getHttpClient();
		HttpPost httpPost = new HttpPost(getapiStoreBaseUrl + APIConstants.API_STORE_USER_SIGNUP_URL);
		if (authenticate(getapiStoreBaseUrl + APIConstants.API_STORE_LOGIN_URL, userName, password.toCharArray())) {
			try {
				// Request parameters and other properties.
				List<NameValuePair> params = new ArrayList<NameValuePair>(3);
				params.add(new BasicNameValuePair("action", "addUser"));
				params.add(new BasicNameValuePair("username", userName));
				params.add(new BasicNameValuePair("password", password));
				params.add(new BasicNameValuePair("allFieldsValues", firstName + "|" + lastName + "|" + email));
				httpPost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
				System.out.println(params);
				HttpResponse response = httpClient.execute(httpPost, httpContext);
				HttpEntity entity = response.getEntity();
				String responseString = EntityUtils.toString(entity, StandardCharsets.UTF_8);
				System.out.println(responseString);
				String[] errorSection = responseString.split(",");
				boolean isError = Boolean.parseBoolean(errorSection[0].split(":")[1].split("}")[0].trim());
				if (isError) {
					String errorMsg = errorSection[1].split(":")[1].split("}")[0].trim();
					errorMsg = errorMsg.replace("\"", "");
					throw new Exception(errorMsg);
				}

				try {
					jsonObject = (org.json.simple.JSONObject) org.json.simple.JSONValue.parse(responseString);
				} catch (ClassCastException e) {
					throw new Exception("Could not parse the results. Incompatible result", e);
				}
			} finally {
				// This is done to release the connections
				httpPost.reset();
			}
		}
		return jsonObject;

	}

	public JSONObject logout(String getapiStoreBaseUrl, String type) throws Exception {
		JSONObject jsonObject = null;

		// If the authentication process is successful
		HttpClient httpClient = getHttpClient();
		HttpPost httpPost = null;
		if (type == "Publisher")
			httpPost = new HttpPost(getapiStoreBaseUrl + APIConstants.API_STORE_LOGIN_URL);
		else
			httpPost = new HttpPost(getapiStoreBaseUrl + APIConstants.API_STORE_LOGIN_URL);
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>(3);
			params.add(new BasicNameValuePair("action", "logout"));
			httpPost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
			System.out.println(params);
			HttpResponse response = httpClient.execute(httpPost, httpContext);
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, StandardCharsets.UTF_8);
			System.out.println(responseString);
			String[] errorSection = responseString.split(",");
			boolean isError = Boolean.parseBoolean(errorSection[0].split(":")[1].split("}")[0].trim());
			if (isError) {
				String errorMsg = errorSection[1].split(":")[1].split("}")[0].trim();
				errorMsg = errorMsg.replace("\"", "");
				throw new Exception(errorMsg);
			}
			try {
				jsonObject = (org.json.simple.JSONObject) org.json.simple.JSONValue.parse(responseString);
			} catch (ClassCastException e) {
				throw new Exception("Could not parse the results. Incompatible result", e);
			}
		} finally {
			// This is done to release the connections
			httpPost.reset();
		}
		return jsonObject;
	}

	public JSONObject getApplications(String getapiBaseUrl) throws Exception {
		JSONObject jsonObject = null;

		// If the authentication process is successful
		HttpClient httpClient = getHttpClient();
		HttpPost httpPost = null;
		httpPost = new HttpPost(
				getapiBaseUrl + "/site/blocks/application/application-list/ajax/application-list.jag");
		if (authenticate(Wso2Constants.GETAPI_STORE_BASE_URL, Wso2Constants.GETAPI_USERNAME, Wso2Constants.GETAPI_USERPASS)) {
			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>(3);
				params.add(new BasicNameValuePair("action", "getApplications"));
				httpPost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
				HttpResponse response = httpClient.execute(httpPost, httpContext);
				HttpEntity entity = response.getEntity();
				String responseString = EntityUtils.toString(entity, StandardCharsets.UTF_8);
				String[] errorSection = responseString.split(",");
				boolean isError = Boolean.parseBoolean(errorSection[0].split(":")[1].split("}")[0].trim());
				if (isError) {
					String errorMsg = errorSection[1].split(":")[1].split("}")[0].trim();
					errorMsg = errorMsg.replace("\"", "");
					throw new Exception(errorMsg);
				}
				try {
					jsonObject = (org.json.simple.JSONObject) org.json.simple.JSONValue.parse(responseString);
				} catch (ClassCastException e) {
					throw new Exception("Could not parse the results. Incompatible result", e);
				}
			} finally {
				// This is done to release the connections
				httpPost.reset();
			}
		}
		return jsonObject;
	}

	public JSONObject getAllSubcriptions(String getapiStoreBaseUrl) throws Exception {
		JSONObject jsonObject = null;

		// If the authentication process is successful
		HttpClient httpClient = getHttpClient();
		HttpPost httpPost = null;
		httpPost = new HttpPost(
				getapiStoreBaseUrl + "/site/blocks/subscription/subscription-list/ajax/subscription-list.jag");
		if (authenticate(Wso2Constants.GETAPI_STORE_BASE_URL, Wso2Constants.GETAPI_USERNAME, Wso2Constants.GETAPI_USERPASS)) {
			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>(3);
				params.add(new BasicNameValuePair("action", "getAllSubscriptions"));
				httpPost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
				HttpResponse response = httpClient.execute(httpPost, httpContext);
				HttpEntity entity = response.getEntity();
				String responseString = EntityUtils.toString(entity, StandardCharsets.UTF_8);
				String[] errorSection = responseString.split(",");
				boolean isError = Boolean.parseBoolean(errorSection[0].split(":")[1].split("}")[0].trim());
				if (isError) {
					String errorMsg = errorSection[1].split(":")[1].split("}")[0].trim();
					errorMsg = errorMsg.replace("\"", "");
					throw new Exception(errorMsg);
				}
				try {
					jsonObject = (org.json.simple.JSONObject) org.json.simple.JSONValue.parse(responseString);
				} catch (ClassCastException e) {
					throw new Exception("Could not parse the results. Incompatible result", e);
				}
			} finally {
				// This is done to release the connections
				httpPost.reset();
			}
		}
		return jsonObject;
	}

	public JSONObject removeSubcriptions(String getapiStoreBaseUrl,String apiName,String apiVersion,String applicationId) throws Exception {
		JSONObject jsonObject = null;

		// If the authentication process is successful
		HttpClient httpClient = getHttpClient();
		HttpPost httpPost = null;
		httpPost = new HttpPost(
				getapiStoreBaseUrl + "site/blocks/subscription/subscription-remove/ajax/subscription-remove.jag");
		if (authenticate(Wso2Constants.GETAPI_STORE_BASE_URL, Wso2Constants.GETAPI_USERNAME, Wso2Constants.GETAPI_USERPASS)) {
			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>(3);
				params.add(new BasicNameValuePair("action", "removeSubscription"));
				params.add(new BasicNameValuePair("name", apiName));
				params.add(new BasicNameValuePair("version", apiVersion));
				params.add(new BasicNameValuePair("provider", "master@mainapi.net@carbon.super"));
				params.add(new BasicNameValuePair("applicationId", applicationId));
				httpPost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
				HttpResponse response = httpClient.execute(httpPost, httpContext);
				HttpEntity entity = response.getEntity();
				String responseString = EntityUtils.toString(entity, StandardCharsets.UTF_8);
				String[] errorSection = responseString.split(",");
				boolean isError = Boolean.parseBoolean(errorSection[0].split(":")[1].split("}")[0].trim());
				if (isError) {
					String errorMsg = errorSection[1].split(":")[1].split("}")[0].trim();
					errorMsg = errorMsg.replace("\"", "");
					throw new Exception(errorMsg);
				}
				try {
					jsonObject = (org.json.simple.JSONObject) org.json.simple.JSONValue.parse(responseString);
				} catch (ClassCastException e) {
					throw new Exception("Could not parse the results. Incompatible result", e);
				}
			} finally {
				// This is done to release the connections
				httpPost.reset();
			}
		}
		return jsonObject;
	}
	public JSONObject addSubcriptions(String getapiStoreBaseUrl,String apiName,String apiVersion,String applicationId,String tier) throws Exception {
		JSONObject jsonObject = null;

		// If the authentication process is successful
		HttpClient httpClient = getHttpClient();
		HttpPost httpPost = null;
		httpPost = new HttpPost(
				getapiStoreBaseUrl + "site/blocks/subscription/subscription-remove/ajax/subscription-remove.jag");
		if (authenticate(Wso2Constants.GETAPI_STORE_BASE_URL, Wso2Constants.GETAPI_USERNAME, Wso2Constants.GETAPI_USERPASS)) {
			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>(3);
				params.add(new BasicNameValuePair("action", "addSubscription"));
				params.add(new BasicNameValuePair("name", apiName));
				params.add(new BasicNameValuePair("version", apiVersion));
				params.add(new BasicNameValuePair("tier", tier));
				params.add(new BasicNameValuePair("provider", "master@mainapi.net@carbon.super"));
				params.add(new BasicNameValuePair("applicationId", applicationId));
				httpPost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
				HttpResponse response = httpClient.execute(httpPost, httpContext);
				HttpEntity entity = response.getEntity();
				String responseString = EntityUtils.toString(entity, StandardCharsets.UTF_8);
				String[] errorSection = responseString.split(",");
				boolean isError = Boolean.parseBoolean(errorSection[0].split(":")[1].split("}")[0].trim());
				if (isError) {
					String errorMsg = errorSection[1].split(":")[1].split("}")[0].trim();
					errorMsg = errorMsg.replace("\"", "");
					throw new Exception(errorMsg);
				}
				try {
					jsonObject = (org.json.simple.JSONObject) org.json.simple.JSONValue.parse(responseString);
				} catch (ClassCastException e) {
					throw new Exception("Could not parse the results. Incompatible result", e);
				}
			} finally {
				// This is done to release the connections
				httpPost.reset();
			}
		}
		return jsonObject;
	}
}
