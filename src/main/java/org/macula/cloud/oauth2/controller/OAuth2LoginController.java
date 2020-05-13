package org.macula.cloud.oauth2.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.assertj.core.util.Maps;
import org.macula.cloud.core.principal.SubjectPrincipal;
import org.macula.cloud.core.utils.StringUtils;
import org.macula.cloud.security.authentication.CaptchaValidationPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RefreshScope
public class OAuth2LoginController {

	@Autowired
	private CaptchaValidationPolicy captchaValidationPolicy;

	@Autowired
	private AuthenticationSuccessHandler authenticationSuccessHandler;

	@RequestMapping("/login")
	public String loginPage(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			Authentication authentication, Model model) throws IOException, ServletException {

		if (authentication != null && authentication.getPrincipal() instanceof SubjectPrincipal) {
			authenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
			return null;
		}
		// 默认登录页面
		String username = (String) session.getAttribute("username");
		String errorCode = (String) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION + "-CODE");
		String error = (String) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

		if (errorCode == null && error != null) {
			errorCode = error;
		}

		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION + "-CODE");
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		session.removeAttribute("username");

		if (StringUtils.isNotEmpty(username)) {
			model.addAttribute("isNeedCaptcha", captchaValidationPolicy.isNeedCaptchaValidate(username));
		}

		if (errorCode != null) {
			model.addAllAttributes(Maps.newHashMap(errorCode, error));
		}

		return "login-default";
	}
	
	
	

}
