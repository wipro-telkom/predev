
<%@page import="com.liferay.portal.kernel.model.Role"%>
<%@page import="com.liferay.portal.kernel.util.WebKeys"%>
<%@page import="com.liferay.portal.kernel.model.User"%>
<%@page import="java.util.Base64"%>
<%@page import="com.liferay.portal.kernel.util.PropsUtil"%>
<%@page import="com.liferay.asset.kernel.model.AssetEntry"%>
<%@page
	import="com.liferay.asset.kernel.service.AssetEntryLocalServiceUtil"%>
<%@page import="java.util.List"%>
<%@page import="com.liferay.asset.kernel.model.AssetTag"%>
<%@page
	import="com.liferay.asset.kernel.service.AssetTagLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.util.StringPool"%>
<%@page import="java.net.InetSocketAddress"%>
<%@page import="java.net.Proxy"%>
<%@page import="com.liferay.portal.kernel.json.JSONFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.json.JSONFactory"%>
<%@page
	import="com.liferay.portal.kernel.portlet.JSONPortletResponseUtil"%>
<%@page import="com.liferay.portal.kernel.json.JSONObject"%>
<%@page import="com.liferay.portal.kernel.json.JSONArray"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="javax.net.ssl.HttpsURLConnection"%>
<%@page import="java.net.URL"%>
<%@page import="java.net.HttpURLConnection"%>
<%@ include file="/init.jsp"%>
<%@page import="com.liferay.portal.kernel.util.PortalUtil"%>
<script src='<%=request.getContextPath()%>/js/jquery.scrollto.js'></script>
<style>
.detailapipage .code {
	color: #c7254e;
	background-color: #f9f2f4;
	border-radius: 4px;
}

.detailapipage {
	max-width: 1042px;
	padding-top: 20px;
}

.detailapipage .floatLeft {
	float: left;
}

.detailapipage .apiName h4 {
	padding: 0 0 20px 0 !important;
}

.detailapipage .journal-content-article h4 {
	color: #e62037;
	padding: 20px 0;
	font-weight: 600;
}

.detailapipage .journal-content-article p {
	font-size: 14px;
	text-align: justify;
	font-family: 'Titillium Web', "Lato", "Helvetica Neue", Helvetica, Arial,
		sans-serif;
}

.detailapipage .APISummary h5.subheadingapi {
	padding-bottom: 20px;
}

.detailapipage h5.subheadingapi {
	padding-bottom: 20px;
}

.detailapipage .api-tabs .nav-tabs>li.active>a, .api-tabs .nav-tabs>li.active>a:hover,
	.api-tabs .nav-tabs>li.active>a:focus {
	color: #2c3e50;
	background-color: #fff;
	border: 1px solid #e62037;
	border-bottom-color: transparent;
	cursor: default;
}

.detailapipage .api-tabs div#apidetailpart2, .api-tabs div#apidetailpart4
	{
	border: 1px solid #e62037;
	left: 0 !important;
}

.detailapipage .api-tabs .tab-content, .api-tabs .tab-contentlable2,
	.api-tabs .tab-contentlable3 {
	width: 100%;
	padding: 15px;
	top: 0;
	background: #fff;
	position: inherit;
}

.clearfix {
	clear: both;
}

.detailapipage .accesstokengenerate {
	width: 100%;
	margin: 10px 0px;
}

.detailapipage .httpMethod .thumbnail {
	width: 50%;
	float: left;
}

.detailapipage .httpMethod .thumbnail table, .httpMethod .thumbnail .httpMethod-curl
	{
	margin-top: 10px;
}

.detailapipage .httpMethod .panel-body, .httpMethod .thumbnail {
	background-color: #d9edf7;
	border-color: #bce8f1;
}

.detailapipage #apidetailpart3 {
	padding: 20px 0px;
}

.detailapipage .bookmarkbutton .trynow a, .detailapipage .trynow button
	{
	padding: 7px 5px 8px;
	margin: 5px;
	letter-spacing: 1.15px;
}

.detailapipage .bookmarkbutton .trynow a, .trynow button {
	color: #000;
	display: inline-block;
	margin: 4px;
	border: none;
}

.detailapipage .bookmarkImg {
	height: 41px;
	width: 60px;
}

.detailapipage .btn-primary:hover {
	color: #fff;
	background-color: #e62037;
	border-color: #161f29;
}

.detailapipage .key {
	display: inline-block;
	width: 70px;
}

.detailapipage .label-default {
	background-color: #4e5d6c;
}

.apiNotFound {
	padding: 15% 0%;
	text-align: center;
}

.modal {
	display: none;
	position: fixed;
	z-index: 100;
	padding-top: 100px;
	left: 0;
	top: 0;
	width: 100%;
	height: 100%;
	overflow: auto;
	background-color: rgb(0, 0, 0);
	background-color: rgba(0, 0, 0, 0.4);
}

.modal-content {
	background-color: #fefefe;
	margin: auto;
	padding: 20px;
	border: 1px solid #888;
	width: 26%;
	height: 28%;
	text-align: center;
}

.close {
	margin-top: -10px;
	color: #aaaaaa;
	float: right;
	font-size: 28px;
	font-weight: bold;
}

.close:hover, .close:focus {
	color: #000;
	text-decoration: none;
	cursor: pointer;
}

.okbtn {
	padding-left: 22px;
	padding-right: 22px;
	margin-top: 35px;
}
/* =========================== */
#sidebar {
	width: 300px;
	float: left;
}

.pagesection.detailapipage {
	float: right;
	padding-left: 24px;
}

.api_document nav {
	width: 100%;
	background-color: rgb(240, 240, 240);
	border: solid 1px rgb(220, 220, 220);
	padding: 0 12px;
	max-width: 208px
}

.api_document nav.stick {
	position: fixed;
	top: 0;
	z-index: 10000;
	margin-top: 70px;
}

.api_document nav ul {
	list-style-type: none;
	margin: 0;
	padding: 0;
}

.api_document nav li a {
	color: rgb(50, 50, 50);
	font-weight: 700;
}

.api_document a.nav-active {
	color: #ccc;
}

.api_document .list1, .list2 {
	height: 213px;
	float: left;
	width: 34%;
	background-repeat: no-repeat;
	padding-top: 145px;
}

.api_document .list1 {
	background-image: url("./img/php.png");
	background-size: 100%;
}

.api_document .list2 {
	background-image: url("./img/java.png");
	margin-left: 20px;
}

.api_document .trynow {
	margin: 0 auto;
	width: 49%;
}

.api_document a.buttontype {
	border: 2px solid #e62037 !important;
	padding: 8px 15px;
	max-width: 128px;
	min-width: 80px;
	background: 0;
	color: #000;
	font-size: 14px;
	border-radius: 2px !important;
	margin-right: 15px;
}
/* ====== */
.apidocument_telkom .midSection {
	width: 1277px;
	max-width: 100%;
}

.api_document {
	margin: 30px auto 100px;
}

.pagesection {
	margin: 0px;
}

.detailapipage {
	padding-top: 0px;
}

.api_document a.buttontype:hover, .buttontype:active:hover {
	color: #fff !important;
	background-color: #e62037;
}

.api_document .trynow {
	text-align: right;
}

.api_document a.buttontype:focus {
	text-decoration: none;
}

.api_document .buttontype:hover, .buttontype:active, .buttontype:active:focus,
	.buttontype:active:hover {
	color: #fff !important;
	background-color: #e62037 !important;
}

#sidebar {
	width: 21%;
}

.api_document nav {remove width;
	padding: 0;
	border: none;
}

.api_document #sidebar li {
	border-bottom: 1px solid black;
}

.api_document li.last {
	border: none;
}

.pagesection.detailapipage {
	width: 79%;
	float: right;
}

.api_detail_para .smsNotification .smsImage {
	height: 100% !important;
	padding-bottom: 0px;
	border: 1px solid #e60237;
	margin-bottom: 20px;
}

.detailapipage .api-tabs .nav-tabs>li>a {
	color: #2c3e50 !important;
	background-color: #fff !important;
	border: 1px solid #e62037 !important;
	border-bottom-color: transparent;
	cursor: default;
}

.detailapipage .api-tabs .nav-tabs>li.active>a, .api-tabs .nav-tabs>li.active>a:hover,
	.api-tabs .nav-tabs>li.active>a:focus {
	padding: 10px 15px;
	border: 1px solid #fff !important;
	border-radius: 0;
	background: #e62037 !important;
	color: #fff !important;
	font-size: 18px;
	border-bottom-color: transparent;
	cursor: default;
}

.swagger-ui .info hgroup.main a {
	display: none;
}

#swagger-frame {
	min-height: 500px;
}
.api_document a.nav-active {color: #e62037;background-color: #000;text-decoration: none;}
.api_document nav li a{color: rgb(50, 50, 50);font-weight: 700; padding: 3.5%;display: block;}
.api_document nav.stick {margin-top: 70px; }
.detailapipage .btn-primary:focus,.detailapipage .btn-primary.focus {
    background-color: #e62037;
}


</style>
<%
	try {
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy2.wipro.com", 8080));
		String apiName, version = null;
		HttpServletRequest httpReq = PortalUtil.getOriginalServletRequest(request);
		apiName = httpReq.getParameter("apiName");
		version = httpReq.getParameter("version");
		// 		apiName = "SMSNotification";
		// 		version = "1.0.0";
		User userData = ((User) request.getAttribute(WebKeys.USER));
		System.out.println("User:" + userData);
		Boolean flag = false;
		String prodBearerCode = "";
		String sandBearerCode = "";
		JSONObject jsonUser = null;
		JSONObject currentSub = null;
		String accesstoken = "";
		JSONObject applicationsJson = null;
		JSONArray subJson = null;
		if (apiName != null && version != null) {
			HttpURLConnection conn, connsub, connuser = null;
			String urlPath = "http://wso2-predev-website.2f96.telkom.openshiftapps.com/WSO2GatewayPlugin/rest/WSO2PublisherService/getApiDetails?API_NAME="
					+ apiName + "&version=" + version + "&provider=master@mainapi.net";

			URL url = new URL(urlPath);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.connect();
			String responseData = "";
			String responseDatasub = "";
			String responseDataUser = "";
			if (conn.getResponseCode() == HttpsURLConnection.HTTP_OK) {
				String line;
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while ((line = br.readLine()) != null) {
					responseData += line;
				}
			}
			if (userData != null) {
				String urlPath_sub = "http://wso2-predev-website.2f96.telkom.openshiftapps.com/GatewayBridgePlugin/rest/BridgeController/getAllSubcriptions?userid="
						+ userData.getEmailAddress();
				System.out.println(urlPath_sub);
				URL url_sub = new URL(urlPath_sub);
				connsub = (HttpURLConnection) url_sub.openConnection();
				connsub.setRequestMethod("POST");
				connsub.connect();
				if (connsub.getResponseCode() == HttpsURLConnection.HTTP_OK) {
					String line;
					BufferedReader br = new BufferedReader(new InputStreamReader(connsub.getInputStream()));
					while ((line = br.readLine()) != null) {
						responseDatasub += line;
					}
				}
				String urlPath_user = "http://wso2-predev-website.2f96.telkom.openshiftapps.com/GatewayBridgePlugin/rest/BridgeController/getTokenData?userid="
						+ userData.getEmailAddress();
				System.out.println(urlPath_user);
				URL url_user = new URL(urlPath_user);
				connuser = (HttpURLConnection) url_user.openConnection();
				connuser.setRequestMethod("GET");
				connuser.connect();
				if (connuser.getResponseCode() == HttpsURLConnection.HTTP_OK) {
					String line;
					BufferedReader br = new BufferedReader(new InputStreamReader(connuser.getInputStream()));
					while ((line = br.readLine()) != null) {
						responseDataUser += line;
					}
				}

				JSONObject jsonTemp = JSONFactoryUtil.createJSONObject(responseDatasub);
				JSONArray applications = (jsonTemp.getJSONObject("subscriptions")).getJSONArray("applications");
				if (applications.length() > 0) {
					applicationsJson = JSONFactoryUtil.createJSONObject(applications.get(0).toString());
					subJson = applicationsJson.getJSONArray("subscriptions");
				}
				jsonUser = JSONFactoryUtil
						.createJSONObject(JSONFactoryUtil.createJSONObject(responseDataUser).getString("data"));
				accesstoken = jsonUser.get("accesstoken") == null
						|| jsonUser.get("accesstoken").toString().equals("")
								? ""
								: jsonUser.get("accesstoken").toString();
				for (int i = 0; i < subJson.length(); i++) {
					JSONObject temp = JSONFactoryUtil.createJSONObject(subJson.get(i).toString());
					if (temp.get("name").toString().equals(apiName)) {
						currentSub = temp;
						flag = true;
						break;
					}
				}
				if (currentSub != null && currentSub.get("prodConsumerKey") != null
						&& currentSub.get("prodConsumerSecret") != null) {
					prodBearerCode = new String(Base64.getEncoder().encode(
							(currentSub.get("prodConsumerKey") + ":" + currentSub.get("prodConsumerSecret"))
									.getBytes()));
				}
				if (currentSub != null && currentSub.get("sandboxConsumerKey") != null
						&& currentSub.get("sandboxConsumerSecret") != null) {
					sandBearerCode = new String(Base64.getEncoder().encode((currentSub.get("sandboxConsumerKey")
							+ ":" + currentSub.get("sandboxConsumerSecret")).getBytes()));
				}

				if (prodBearerCode.equals("bnVsbDpudWxs")) {
					prodBearerCode = "";
				}
				if (sandBearerCode.equals("bnVsbDpudWxs")) {
					sandBearerCode = "";
				}
			}
			JSONObject json = JSONFactoryUtil.createJSONObject(responseData);
%>
<div class="api_document">
	<%
		if (flag) {
	%>
	<div id="myAlert" class="alert alert-info">You Are Subscribed to
		this API</div>

	<aui:script>
YUI().use(
  'aui-alert',
  function(Y) {
    new Y.Alert(
      {
        closeable: true,
        animated: true,
        render: true,
        srcNode: '#myAlert',
      }
    );
  }
);

</aui:script>
	




	
<%
		}
	%>
	<div id="sidebar">
		<div id="nav-anchor"></div>
		<nav>
			<ul>
				<li><a href="#intro">Introduction</a></li>
				<li><a href="#apiMethod">API Method</a></li>
				<li><a href="#usecase">Use Case</a></li>
				<li><a href="#sdk">SDK</a></li>
			</ul>
		</nav>
	</div>
	<div class="pagesection detailapipage journal-content-article">
		<div id="apidetailpart1" class="apiContent col-sm-12">
			<div class="apiHeaderPart">
				<div class="apiName floatLeft">
					<h4><%=json.get("name")%></h4>
				</div>
				<div class="trynow floatRight">
					<%
						if (flag) {
					%>
					<a href="#swagger" class="buttontype">Try Now</a>
					<%
						} else {
					%>
					<a href="#subscribe" class="buttontype apiSubscribe">Subscribe</a>
					<%
						}
					%>
					<a href="#openModal" class="bookmarkbutton" data-toggle="tooltip"
						title="Add Bookmark!"> <img class="bookmarkImg"
						src="<%=request.getContextPath()%>/images/Bookmarks_23670.png">
					</a>
				</div>
				<div class="clearfix"></div>
			</div>

			<section id="intro">
				<div class="api_detail_para">
					<liferay-portlet:runtime
						portletName="com_liferay_journal_content_web_portlet_JournalContentPortlet" />
				</div>
				<div class="clearfix"></div>
			</section>
		</div>
		<div class="clearfix"></div>

		<section id="apiMethod">
			<div class="apidetailpagetab">

				<div class="api-tabs tabContent col-sm-12">
					<ul class="nav nav-tabs">
						<li class="active"><a data-toggle="tab" href="#menu1"><b>API
									Method</b></a></li>
						<li><a data-toggle="tab" href="#menu2"><b>API Request</b></a></li>
					</ul>

					<div class="tab-content" id="apidetailpart2">
						<div id="menu1" class="tab-pane fade in active">
							<h4>Access Token</h4>
							<p>In order to make a call to our API, you need to send
								access token along with the API call. With MainAPI however, the
								access token is by default changing for every hour as a security
								measure. Therefore, in order to get the last updated token you
								need to send a token request using the following details:</p>
							<table class="aui table table-bordered">
								<tbody>
									<tr>
										<td>URL</td>
										<td colspan="4"><code>
												https://api.mainapi.net/token</code></td>
									</tr>
									<%
										if (flag) {
									%>
									<tr>
										<td>Header</td>
										<td colspan="4">""</td>
									</tr>
									<%
										} else {
									%>
									<tr>
										<td>Header</td>
										<td colspan="4"><code>
												"Authorization: Basic
												<%=prodBearerCode%>"
											</code></td>
									</tr>
									<%
										}
									%>
									<tr>
										<td>Body</td>
										<td colspan="4"><code>"grant_type=client_credentials"</code></td>
									</tr>
									<tr>
										<td>Method</td>
										<td colspan="4"><code>POST</code></td>
									</tr>
								</tbody>
							</table>
							<div class="belowpara">
								<p>You can also use CURL to send get token request:
								<div class="code">
									curl -k -d "grant_type=client_credentials" -H "Authorization:
									Basic
									<%=prodBearerCode%>" https://api.mainapi.net/token
								</div>
								</p>
								<p>
									The following is the format of response message:<br>{
									"scope": "string", "token_type": "string", "expires_in":
									"timestamp", "access_token": "string" }<br>The following
									is the format of error message:<br>{ "error": "string",
									"error_description": "string" }
								</p>
							</div>
						</div>
						<div id="menu2" class="tab-pane fade in">
							<h4>Send SMS</h4>
							<p>To make a call to SMS Notification API, send HTTP request
								using the following details:</p>
							<table class="aui table table-bordered">
								<tbody>
									<tr>
										<td>URL</td>
										<td colspan="4"><code>
												https://api.mainapi.net<%=json.get("context")%></code></td>
									</tr>
									<tr>
										<td>Endpoint</td>
										<td colspan="4"><code>/messages</code></td>
									</tr>
									<tr>
										<td>Header</td>
										<td colspan="4">
											<ul>
												<li><code>(Optional) X-MainAPI-Username:
														&lt;given username&gt;</code></li>
												<li><code>(Optional) X-MainAPI-Password:
														&lt;given password&gt;</code></li>
												<li><code>(Optional) X-MainAPI-Senderid:
														&lt;registered masking&gt;</code></li>
												<li><code>Accept: application/json</code></li>
												<li><code>Content-Type:
														application/x-www-form-urlencoded</code></li>
												<li><code>Authorization: Bearer &lt;access
														token&gt;</code></li>
											</ul>
										</td>
									</tr>
									<tr>
										<td>Body</td>
										<td colspan="4">
											<ul>
												<li><code>msisdn=&lt;phone number&gt;</code></li>
												<li><code>content=&lt;message to send&gt;</code></li>
											</ul>
										</td>
									</tr>
									<tr>
										<td>Method</td>
										<td colspan="4"><code>POST</code></td>
									</tr>
								</tbody>
							</table>
							<div class="belowpara">
								<p>You can also use CURL to send SMS Notification API
									request:</p>
								<div class="code">
									curl -X POST --header "Content-Type:
									application/x-www-form-urlencoded" --header "Accept:
									application/json" --header "Authorization: Bearer &lt;access
									token&gt;" -d "msisdn=&lt;phone number&gt;&content=&lt;message
									to send&gt;" "https://api.mainapi.net/<%=json.get("context")%>/messages"
								</div>
							</div>
						</div>
					</div>


				</div>
			</div>
			<div class="clearfix"></div>
		</section>
		<%
			if (flag && jsonUser != null && currentSub != null) {

						int validity = jsonUser.getString("validity") == null
								|| jsonUser.getString("validity").equals("")
										? 3600
										: Integer.parseInt(jsonUser.getString("validity"));
		%><section id="usecase">
			<div class="api_detail_para col-sm-12" id="apidetailpart3">
				<div class="api-tabs tabContent col-sm-12">
					<ul class="nav nav-tabs">
						<li class="active"><a data-toggle="tab" href="#menu21"><b>Production</b></a></li>
						<li><a data-toggle="tab" href="#menu22"><b>Sandbox</b></a></li>
					</ul>

					<div class="tab-content" id="apidetailpart4">
						<div id="menu21" class="tab-pane fade in active production">
							<h4>Access Token</h4>
							<p>Create access tokens to applications. Because an
								applications is a logical collection of APIs, you can use a
								single access token to invoke multiple APIs and to subscribe to
								one API multiple times with different SLA levels.</p>
							<%
								if (prodBearerCode != null && !prodBearerCode.equals("")) {
							%>
							<div class="row">
								<div class="clientId col-md-6 col-xs-12" id="apiClientid">
									<label>Client ID :</label><input type="text"
										class="clientId form-control" disabled="disabled"
										value=<%=currentSub.get("prodConsumerKey")%>>
								</div>
								<div class="accesstoken col-md-6 col-xs-12" id="apiAccesstoken">
									<label>Access Token :</label><input type="text"
										class="accesstoken form-control" disabled="disabled"
										value=<%=accesstoken%>>
								</div>
								<div class="clientSecret col-md-6 col-xs-12"
									id="apiClientSecret">
									<label>Client Secret :</label><input type="text"
										class="clientSecret form-control" disabled="disabled"
										value=<%=currentSub.get("prodConsumerSecret")%>>
								</div>
								<div class="validityTime col-md-6 col-xs-12"
									id="apiValidityTime">
									<label>Validity Time :</label><input type="text"
										class="validityTime form-control" value=<%=validity%>>
								</div>
								<div class="col-md-12 col-xs-12">
									<button class="btn btn-primary accesstokengenerate">Regenerate</button>
								</div>
								<div class="col-md-12 col-xs-12 httpMethod">
									<div class="panel panel-primary">
										<div class="panel-heading">Send the following to
											generate Access Token</div>
										<div class="panel-body">
											<div class="thumbnail" style="overflow-x: auto;">
												<h4>
													<span class="label label-default">HTML</span>
												</h4>
												<table>
													<tr>
														<td class="key">URL :</td>
														<td class="value">https://api.mainapi.net/token</td>
													</tr>
													<tr>
														<td class="key">Method :</td>
														<td class="value">POST</td>
													</tr>
													<tr>
														<td class="key">Header :</td>
														<td class="value">"Authorization: Basic <%=prodBearerCode%>"
														</td>
													</tr>
													<tr>
														<td class="key">Body :</td>
														<td class="value">"grant_type=client_credentials"</td>
													</tr>
												</table>
											</div>
											<div class="thumbnail" style="overflow-x: auto;">
												<h4>
													<span class="label label-default">CURL</span>
												</h4>
												<p class="httpMethod-curl">
													curl -k -d "grant_type=client_credentials" -H
													"Authorization: Basic
													<%=prodBearerCode%>" https://api.mainapi.net/token
												</p>
											</div>
										</div>
									</div>
								</div>
							</div>
							<%
								} else {
							%>
							<div class="row">
								<div class="col-md-12 col-xs-12">
									<button class="btn btn-primary appkeycreate">Create
										Key</button>
								</div>
							</div>
							<%
								}
							%>
						</div>
						<div id="menu22" class="tab-pane fade in sandbox">
							<h4>Access Token</h4>
							<p>Create access tokens to applications. Because an
								applications is a logical collection of APIs, you can use a
								single access token to invoke multiple APIs and to subscribe to
								one API multiple times with different SLA levels.</p>

							<%
								if (sandBearerCode != null && !sandBearerCode.equals("")) {
							%>
							<div class="row">
								<div class="clientId col-md-6 col-xs-12" id="apiClientid">
									<label>Client ID :</label><input type="text"
										class="clientId form-control" disabled="disabled"
										value=<%=currentSub.get("sandboxConsumerKey")%>>
								</div>
								<div class="accesstoken col-md-6 col-xs-12" id="apiAccesstoken">
									<label>Access Token :</label><input type="text"
										class="accesstoken form-control" disabled="disabled"
										value=<%=accesstoken%>>
								</div>
								<div class="clientSecret col-md-6 col-xs-12"
									id="apiClientSecret">
									<label>Client Secret :</label><input type="text"
										class="clientSecret form-control" disabled="disabled"
										value=<%=currentSub.get("sandboxConsumerSecret")%>>
								</div>
								<div class="validityTime col-md-6 col-xs-12"
									id="apiValidityTime">
									<label>Validity Time :</label><input type="text"
										class="validityTime form-control" value=<%=validity%>>
								</div>
								<div class="col-md-12 col-xs-12">
									<button class="btn btn-primary accesstokengenerate">Regenerate</button>
								</div>
								<div class="col-md-12 col-xs-12 httpMethod">
									<div class="panel panel-primary">
										<div class="panel-heading">Send the following to
											generate Access Token</div>
										<div class="panel-body">
											<div class="thumbnail" style="overflow-x: auto;">
												<h4>
													<span class="label label-default">HTML</span>
												</h4>
												<table>
													<tr>
														<td class="key">URL :</td>
														<td class="value">https://api.mainapi.net/token</td>
													</tr>
													<tr>
														<td class="key">Method :</td>
														<td class="value">POST</td>
													</tr>
													<tr>
														<td class="key">Header :</td>
														<td class="value">"Authorization: Basic <%=sandBearerCode%></td>
													</tr>
													<tr>
														<td class="key">Body :</td>
														<td class="value">"grant_type=client_credentials"</td>
													</tr>
												</table>
											</div>
											<div class="thumbnail" style="overflow-x: auto;">
												<h4>
													<span class="label label-default">CURL</span>
												</h4>
												<p class="httpMethod-curl">
													curl -k -d "grant_type=client_credentials" -H
													"Authorization: Basic
													<%=sandBearerCode%>" https://api.mainapi.net/token
												</p>
											</div>
										</div>
									</div>
								</div>
							</div>
							<%
								} else {
							%>
							<div class="row">
								<div class="col-md-12 col-xs-12">
									<button class="btn btn-primary appkeycreate">Create
										Key</button>
								</div>
							</div>
							<%
								}
							%>
						</div>
					</div>
				</div>
			</div>
			<div class="clearfix"></div>
		</section>
		<section id="swagger">
			<%
				if (flag) {
			%>
			<script>
			function iFrameResize(iframe){
				setTimeout(function(){
				iframe.height = iframe.contentWindow.document.body.scrollHeight+50 + "px";
				console.log(iframe.height);
			},1000);
			}
			</script>
			<iframe id="swagger-frame"
				src="/pluginmanager/<%=json.get("name")%>.html"
				style="border: 0; width: 100%;" onload="iFrameResize(this)"></iframe>
			<%
				}
			%>
			<div class="clearfix"></div>
		</section>
		<section id="sdk">
			<div class="clearfix"></div>
		</section>
	</div>
	<div class="clearfix"></div>
</div>
<%
	} else {
%>
<div></div>
<%
	}
%>
<script>
AUI()
.ready(function() {
	$('[data-toggle="tooltip"]').tooltip();
	<%if (userData != null) {%>
		$(".accesstokengenerate").on("click",function() {
			var parent = $(this).parents(".tab-pane");
			var clientId = $(parent).find("input.clientId").val();
			var clientSecret = $(parent).find("input.clientSecret").val();
			var validity_period = $(parent).find("input.validityTime").val();
			$.ajax({
					type : "GET",
					url : "http://wso2-predev-website.2f96.telkom.openshiftapps.com/GatewayBridgePlugin/rest/BridgeController/getAccessCode?clientId="
						+ clientId
						+ "&clientSecret="
						+ clientSecret
						+ "&userid=<%=userData.getEmailAddress()%>"
						+ "&validity_period="
						+ validity_period,
					contentType : 'application/json',
					dataType : "json",
					beforeSend : function(request) {
					request.setRequestHeader("Access-Control-Allow-Origin","*");
					},
					success : function(result) {
							$(parent).find("input.accesstoken").val(result.data.access_token);
							$(parent).find("input.validityTime").val(result.data.expires_in);
							$('#subscribe.buttontype').html("Unsubscribe");
						},
					error : function(result) {
						alert('Error');
						}
					});
	});
		$(".appkeycreate").on("click",function() {
			var parent = $(this).parents(".tab-pane");
			if($(parent).hasClass("production")){
				keytype = 'PRODUCTION';
			}else{
				keytype = 'SANDBOX';
			}
			$.ajax({
					type : "POST",
					url : "http://wso2-predev-website.2f96.telkom.openshiftapps.com/GatewayBridgePlugin/rest/BridgeController/generateApplicationKey?keytype="
						+ keytype
						+ "&callbackUrl="
						+ "&authorizedDomains=ALL"
						+ "&validityTime=360000"
						+"&userid=<%=userData.getEmailAddress()%>",
					contentType : 'application/json',
					dataType : "json",
					beforeSend : function(request) {
					request.setRequestHeader("Access-Control-Allow-Origin","*");
					},
					success : function(result) {
						location.reload();
						},
					error : function(result) {
						alert('Error');
						}
					});
	});
	$(".detailapipage .trynow .apiSubscribe").on("click",function(){
		var _this = this;
		if(confirm("Are you Sure?")){
			if($(this).html()=="Subscribe"){
				$.ajax({
					type : "POST",
					url : "http://wso2-predev-website.2f96.telkom.openshiftapps.com/GatewayBridgePlugin/rest/BridgeController/addSubcriptions?apiName=<%=apiName%>&apiVersion=<%=version%>&tier=Unlimited&applicationId=<%=applicationsJson.get("id").toString()%>&userid=<%=userData.getEmailAddress()%>",
																contentType : 'application/json',
																dataType : "json",
																beforeSend : function(
																		request) {
																	request
																			.setRequestHeader(
																					"Access-Control-Allow-Origin",
																					"*");
																},
																success : function(
																		result) {
																	if (result.error == "false") {
																		$(_this)
																				.remove();
																		location
																				.reload();
																	}
																},
																error : function(
																		result) {
																	alert('Error while Subscribing');
																}
															});
												}
											}
										});
<%} else {%>
	$(".detailapipage .trynow .apiSubscribe").on("click",
								function() {
									window.location.replace("/c/portal/login");
								});
<%}%>
	//	===========================================POOJAN'S JS

						/** 
						 * This part does the "fixed navigation after scroll" functionality
						 * We use the jQuery function scroll() to recalculate our variables as the 
						 * page is scrolled/
						 */
						$(window).scroll(function() {
							var window_top = $(window).scrollTop() + 12; // the "12" should equal the margin-top value for nav.stick
							var div_top = $('#nav-anchor').offset().top;
							if (window_top > div_top) {
								$('nav').addClass('stick');
							} else {
								$('nav').removeClass('stick');
							}
						});

						/**
						 * This part causes smooth scrolling using scrollto.js
						 * We target all a tags inside the nav, and apply the scrollto.js to it.
						 */
						$(".api_document nav a,.trynow a").click(function(evn) {
							evn.preventDefault();
							console.log(this.hash);
							$('html,body').scrollTo(this.hash, this.hash);
						});

						/**
						 * This part handles the highlighting functionality.
						 * We use the scroll functionality again, some array creation and 
						 * manipulation, class adding and class removing, and conditional testing
						 */
						var aChildren = $("#sidebar nav li").children(); // find the a children of the list items
						var aArray = []; // create the empty aArray
						for (var i = 0; i < aChildren.length; i++) {
							var aChild = aChildren[i];
							var ahref = $(aChild).attr('href');
							aArray.push(ahref);
						} // this for loop fills the aArray with attribute href values

						$(window)
								.scroll(
										function() {
											var windowPos = $(window)
													.scrollTop(); // get the offset of the window from the top of page
											var windowHeight = $(window)
													.height(); // get the height of the window
											var docHeight = $(document)
													.height();

											for (var i = 0; i < aArray.length; i++) {
												var theID = aArray[i];
												var divPos = $(theID).offset().top; // get the offset of the div from the top of page
												var divHeight = $(theID)
														.height(); // get the height of the div in question
												if (windowPos >= divPos
														&& windowPos < (divPos + divHeight)) {
													$("a[href='" + theID + "']")
															.addClass(
																	"nav-active");
												} else {
													$("a[href='" + theID + "']")
															.removeClass(
																	"nav-active");
												}
											}

											if (windowPos + windowHeight == docHeight) {
												if (!$("nav li:last-child a")
														.hasClass("nav-active")) {
													var navActiveCurrent = $(
															".nav-active")
															.attr("href");
													$(
															"a[href='"
																	+ navActiveCurrent
																	+ "']")
															.removeClass(
																	"nav-active");
													$("nav li:last-child a")
															.addClass(
																	"nav-active");
												}
											}
										});

						function setHeight() {
							windowHeight = $(window).innerHeight();
							windowHeight = windowHeight
									- $("footer").outerHeight();
							$('.api_document nav.stick').css('min-height',
									windowHeight);
						}
						setHeight();

						$(window).resize(function() {
							setHeight();
						});

						//   ======================================END
					});
</script>
<%
	} else {
%>
<div class="apiNotFound">API NOT FOUND. PLEASE CHECK AGAIN.</div>
<%
	}
	} catch (Exception e) {
		e.printStackTrace();
	}
%>
