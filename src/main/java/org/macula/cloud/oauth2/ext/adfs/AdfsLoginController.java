package org.macula.cloud.oauth2.ext.adfs;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.macula.cloud.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.infinitusint.emp.sdk.adfs.api.SsoServiceFacade;

@RestController
@RequestMapping("/login")
public class AdfsLoginController {

	@Autowired
	private AdfsProperties properties;

	@Autowired
	private SsoServiceFacade ssoServiceFacade;

	@RequestMapping("/adfs/authorize")
	public void render(@RequestParam(value = "redirect", required = false) String redirect, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		if (StringUtils.isEmpty(redirect)) {
			this.ssoServiceFacade.toLogin(properties.getCallbackUrl(), request, response);
		} else {
			this.ssoServiceFacade.toLogin(properties.getCallbackUrl() + "?redirect=" + redirect, request, response);
		}
	}

}
