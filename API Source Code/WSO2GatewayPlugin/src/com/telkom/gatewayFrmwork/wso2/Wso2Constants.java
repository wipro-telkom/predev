package com.telkom.gatewayFrmwork.wso2;

import java.net.InetSocketAddress;
import java.net.Proxy;

public final class Wso2Constants {
	public final static String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:54.0) Gecko/20100101 Firefox/54.0";
	public static final Proxy PROXY = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy2.wipro.com", 8080));
	
	public final static String AUTHORIZATION_CODE = "Bearer 6f6b06fe-131e-314b-9ef8-42f2cbdcfc18";
	public final static String ACCEPT_LANGUAGE = "en-US,en;q=0.5";
	
	//API's endpoint url
	public final static String GETAPI_PUBLISHER_BASE_URL = "https://pub.mainapi.net/publisher";
	public final static String GETAPI_STORE_BASE_URL = "https://pub.mainapi.net/store";
	public final static String GETAPI_USERNAME = "mediation.hub@mainapi.net";
	public final static char[] GETAPI_USERPASS = "CH4ng3!!!".toCharArray();
	
	

}
