package org.macula.cloud.oauth2.channel;

import javax.servlet.http.HttpServletRequest;

import org.macula.cloud.core.oauth2.LoginCredential;
import org.macula.cloud.core.utils.HttpRequestUtils;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import lombok.Getter;

@Getter
public class ChannelAuthenticationDetails extends WebAuthenticationDetails {

	private static final long serialVersionUID = 6975601077710753878L;

	private LoginCredential credential = new LoginCredential();

	public ChannelAuthenticationDetails(HttpServletRequest request) {
		super(request);

		credential.setClientId(request.getParameter("client_id"));
		credential.setUsername(request.getParameter("username"));
		credential.setPassword(request.getParameter("password"));
		credential.setMobile(request.getParameter("mobile"));
		credential.setSms(request.getParameter("sms"));
		credential.setCaptcha(request.getParameter("captcha"));
		credential.setCaptchaCode((String) request.getSession().getAttribute("captchaCode"));
		credential.setRedirectToUrl(HttpRequestUtils.getRedirectToValue(request));
		credential.setSign(request.getParameter("sign"));
		credential.setSessionId(this.getSessionId());
		credential.setRemoteAddress(this.getRemoteAddress());

		request.getSession().removeAttribute("captchaCode");
	}

}
