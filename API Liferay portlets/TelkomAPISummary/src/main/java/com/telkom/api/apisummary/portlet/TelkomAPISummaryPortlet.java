package com.telkom.api.apisummary.portlet;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import javax.portlet.Portlet;

import org.osgi.service.component.annotations.Component;

/**
 * @author SUDAS746
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.Telkom_APIs",
		"com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=TelkomAPISummary Portlet",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.resource-bundle=content.Language",
		"com.liferay.portlet.icon=/Images/sms1.PNG",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class TelkomAPISummaryPortlet extends MVCPortlet {
}