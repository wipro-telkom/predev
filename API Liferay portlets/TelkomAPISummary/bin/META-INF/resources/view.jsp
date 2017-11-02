<%---- 
/*  Name 		: APIs Summary on landing Page
 *  Description : Users can view enabled Apis based on their roles and categories 
 *  Middleware Provider : MW Database 
 *  Urls invoked :: 
 *				get all category : http://10.138.30.11:9846/GatewayBridgePlugin/rest/BridgeController/getApicategoryData
 *				get all apis	 : http://10.138.30.11:9846/GatewayBridgePlugin/rest/BridgeController/getAllApiData
 * 				
 *	Developer  	: Suman Kumar Das 
  *	Created Date: 11th Oct 2017
 *	Modified Date: 
 *	Modified by  : 

--%>
<%@page import="java.net.InetSocketAddress"%>
<%@page import="java.net.Proxy"%>
<%@page import="com.liferay.portal.kernel.json.JSONArray"%>
<%@page import="com.liferay.portal.kernel.json.JSONFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.json.JSONObject"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.net.URL"%>
<%@page import="java.net.HttpURLConnection"%>
<%@ include file="/init.jsp"%>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme"%>
<%@ page import="com.liferay.portal.kernel.model.User"%>
<%@ page import="com.liferay.portal.kernel.util.WebKeys"%>
<%@ page import="com.liferay.portal.kernel.theme.ThemeDisplay"%>
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"
	rel="stylesheet" type="text/css">

<link href="https://fonts.googleapis.com/icon?family=Material+Icons"
	rel="stylesheet">
<link
	href="https://fonts.googleapis.com/css?family=Montserrat:500|Raleway"
	rel="stylesheet">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/css/apiSummary.css">
<%
Proxy PROXY = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy2.wipro.com", 8080));
	//StringBuffer assignedRoleids = null;
	String assgnRleIds = "";
	long[] roleIds = ((ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY)).getUser().getRoleIds();
	//out.println(roleIds.length);
	
	for (int roleIdCnt = 0; roleIdCnt < roleIds.length; roleIdCnt++) {
		//out.println(roleIds[roleIdCnt]);

		if (roleIdCnt == (roleIds.length - 1)) {
			assgnRleIds += roleIds[roleIdCnt] + "";
		} else {
			assgnRleIds += roleIds[roleIdCnt] + ",";
		}

	}
	JSONArray catData = null;
	JSONArray apiDatas = null;
	Boolean flag = false;
	try {

		HttpURLConnection c1 = null;
		String urlData1 = "http://wso2-predev-website.2f96.telkom.openshiftapps.com/GatewayBridgePlugin/rest/BridgeController/getApicategoryData";
		URL u1 = new URL(urlData1);
		c1 = (HttpURLConnection) u1.openConnection();
		System.out.println("TESTING");
		c1.setRequestMethod("GET");
		c1.setRequestProperty("Content-length", "0");
		c1.setUseCaches(false);
		c1.setAllowUserInteraction(false);
		c1.connect();
		int status1 = c1.getResponseCode();
		StringBuilder sb1 = null;
		switch (status1) {
			case 200 :
			case 201 :
				BufferedReader br1 = new BufferedReader(new InputStreamReader(c1.getInputStream()));
				sb1 = new StringBuilder();
				String line1;
				while ((line1 = br1.readLine()) != null) {
					sb1.append(line1 + "\n");
				}
				br1.close();
		}
		c1.disconnect();
		System.out.println(sb1.toString());
		catData = JSONFactoryUtil.createJSONArray(
				JSONFactoryUtil.createJSONObject(sb1.toString()).getJSONArray("data").toJSONString());

		HttpURLConnection c = null;
		String urlData = "http://wso2-predev-website.2f96.telkom.openshiftapps.com/GatewayBridgePlugin/rest/BridgeController/getAllApiData?api=WSO2&cache=true&role="
				+ assgnRleIds;
		URL u = new URL(urlData);
		System.out.println(urlData);
		c = (HttpURLConnection) u.openConnection();
		c.setRequestMethod("GET");
		c.setRequestProperty("Content-length", "0");
		c.setUseCaches(false);
		c.setAllowUserInteraction(false);
		c.connect();

		int status = c.getResponseCode();
		StringBuilder sb = null;
		switch (status) {
			case 200 :
			case 201 :
				BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
				sb = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();
		}
		c.disconnect();
		apiDatas = JSONFactoryUtil.createJSONArray(
				JSONFactoryUtil.createJSONObject(sb.toString()).getJSONArray("data").toJSONString());
		System.out.println(apiDatas);
	} catch (Exception e) {
		e.printStackTrace();
		System.out.println("Error while Getting all api data...");
	}
	JSONObject data = null;
%>

<div class="pageDes">
	<h3 class="mainHeading">Our API</h3>
	<div class="categotryOption">
		<h4>CATEGORY</h4>
		&nbsp; <select>
			<option value="">ALL</option>
			<%
				if (catData != null) {
					for (int i = 0; i < catData.length(); i++) {
						JSONObject cdata = JSONFactoryUtil.createJSONObject(catData.get(i).toString());
						System.out.println(cdata);
			%>
			<option value="<%=cdata.get("CAT_ID")%>"><%=cdata.get("CAT_NAME")%></option>
			<%
				}
				}
			%>
		</select>
	</div>
	<div class="sortOption">
		<h4>SORT</h4>
		&nbsp; <select>
			<option value="">Most Popular</option>
			<option value="">Communication</option>
			<option value="">Media</option>
		</select>
	</div>
	<div class="searchSection">
		<div class="searchbox">
			<div class="searchInput">
				<input placeholder="Search Products..." id="txtGlobalSearch"
					type="search">
			</div>
			<div class="searchIcon">
				<a class="fa fa-search" id="GlobalSearch" href=""></a>
			</div>
			<div class="clearfix"></div>
		</div>
	</div>

	<div class="viewOption buttons">
		<h4>VIEW</h4>
		&nbsp;
		<button class="grid">
			<i class="fa fa-th"></i>
		</button>
		&nbsp;&nbsp;
		<button class="list">
			<i class="fa fa-th-list"></i>
		</button>


	</div>
	<hr>
	<div class="mainBox">

		<ul class="grid">
			<%
			System.out.println("Data:"+(apiDatas.length()!=0));
				if (apiDatas.length()!=0){
					System.out.println("Working");
					for (int i = 0; i < apiDatas.length(); i++) {
						data = JSONFactoryUtil.createJSONObject(apiDatas.get(i).toString());
						System.out.println(data);
						String desc = data.get("API_Short_Desc").toString();
						if (desc.toString().length() > 140) {
							desc = desc.substring(0, 140) + "...";
						}
			%><li><a
				href="/web/telkom/telkomapi-docs?apiName=<%=data.get("API_Name")%>&version=<%=data.get("API_Ver")%>">
					<div class="Box img-transform--hovereffect">
						<div class="listgroup">
							<h5 class="boxHeading"><%=data.get("API_Name")%></h5>
							<h5 class="subHeading"><%=data.get("API_Ver")%></h5>

							<div class="paralist">
								<p><%=desc%></p>
							</div>
						</div>
						<div class="boxImage">

							<img src="<%=request.getContextPath()%>/Images/<%=data.get("API_Name") %>.png"
								alt="Image" class="" onerror="this.src='<%=request.getContextPath()%>/Images/No_image.png';"/>

						</div>
						<div class="clearfix"></div>
						<div class="paragrid">
							<p><%=desc%></p>
						</div>
					</div>
			</a></li>
			<%
				}
				}else{
					%>
			<p class="apiNotFound" style="padding: 30px; text-align: center;">API
				NOT FOUND,PLEASE TRY AGAIN LATER</p>
			<%
				}
			%>

		</ul>

		<div class="clearfix"></div>

		<div class="buttonLoad">
			<button class="buttontype">Loading More</button>
		</div>
	</div>
</div>
<script type="text/javascript">
	AUI()
			.ready(
					function() {
						function summaryLayout(){
                        if($('.pageDes ul').hasClass('grid')){
                            $('.paralist').hide();
                            $('.paragrid').show();
                            $('.fa-th').css({'color':'black'});
                            $('.fa-th-list').css({'color':'darkgray'});
                        }
                                                else if ($('.pageDes ul').hasClass('list')){
                                                                $('.paralist').show();
                            $('.paragrid').hide();
                            $('.fa-th').css({'color':'darkgray'});
                            $('.fa-th-list').css({'color':'black'});
                                                }
                                                $('button').click(function() {
                                                                if ($(this).hasClass('grid')) {
                                                                                $('.pageDes ul').removeClass('list').addClass('grid');
                                $('.paralist').hide();
                                $('.paragrid').show();
                                $('.fa-th').css({'color':'black'});
                                $('.fa-th-list').css({'color':'darkgray'});
                            } 
                                                                else if ($(this).hasClass('list')) {
                                $('.pageDes ul').removeClass('grid').addClass('list');
                                                                                $('.paralist').show();
                                $('.paragrid').hide();
                                                                                $('.fa-th').css({'color':'darkgray'});
                                $('.fa-th-list').css({'color':'black'});
                            }
                                                });

						}
						summaryLayout();
						$(".categotryOption select")
								.on(
										"change",
										function() {
											$.ajax({
														type : "GET",
														url : "http://wso2-predev-website.2f96.telkom.openshiftapps.com/GatewayBridgePlugin/rest/BridgeController/getAllApiData?api=WSO2&cache=true&role=<%=assgnRleIds%>"
																+ "&category="
																+ $(this).val(),
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
															var data = '';
															$.each(result.data,function(i,apidata){
																var desc = apidata.API_Short_Desc;
																if(desc.length>140){
																	desc = desc.substring(0,140)+'...';
																}
																<% if(data !=null){%>
																data += '<li><a href="/web/telkom/telkomapi-docs?apiName='+apidata.API_Name+'&version='+apidata.API_Ver+'"><div class="Box img-transform--hovereffect"><div class="listgroup"><h5 class="boxHeading">'+apidata.API_Name+'</h5><h5 class="subHeading">'+apidata.API_Ver+'</h5><div class="paralist"><p>'+desc+'</p></div></div><div class="boxImage"><img src="<%=request.getContextPath()%>/Images/'+apidata.API_Name+'.png" alt="Image" class="" onerror="this.src=\'<%=request.getContextPath()%>/Images/No_image.png\';"/></div><div class="clearfix"></div><div class="paragrid"><p>'
																						+ desc
																						+ '</p></div></div></a></li>';
<%}%>
	});
															if (data == '') {
																data = '<p class="apiNotFound" style="padding: 30px;text-align: center;">API NOT FOUND,PLEASE TRY AGAIN LATER</p>';
															}
															$(".mainBox ul")
																	.html(data);
															summaryLayout();
														},
														error : function(result) {
															alert('Error');
														}
													});
										});
						$(".mainBox .boxImage img").each(function(imageData){
							
						});
					});
</script>