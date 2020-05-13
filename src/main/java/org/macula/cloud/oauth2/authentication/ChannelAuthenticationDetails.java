package org.macula.cloud.oauth2.authentication;

import javax.servlet.http.HttpServletRequest;

import org.macula.cloud.core.utils.HttpRequestUtils;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class ChannelAuthenticationDetails extends WebAuthenticationDetails {

	private static final long serialVersionUID = 6975601077710753878L;

	private String clientId;
	private String grantType;
	private String channel;

	private String login;
	private String password;
	private String sms;
	private String captcha;
	private String captchaCode;

	private String redirectToUrl;

	private String sign;

	public ChannelAuthenticationDetails(HttpServletRequest request) {
		super(request);
		clientId = request.getParameter("client_id");
		grantType = request.getParameter("grant_type");
		channel = request.getParameter("channel");
		login = request.getParameter("login");
		password = request.getParameter("password");
		sms = request.getParameter("sms");
		captcha = request.getParameter("captcha");
		captchaCode = (String) request.getSession().getAttribute("captchaCode");
		request.getSession().removeAttribute("captchaCode");
		redirectToUrl = HttpRequestUtils.getRedirectToValue(request);
		sign = request.getParameter("sign");
	}

	public String getCaptcha() {
		return captcha;
	}

	public String getCaptchaCode() {
		return captchaCode;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String username) {
		this.login = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getClientId() {
		return clientId;
	}

	public String getGrantType() {
		return grantType;
	}

	public String getChannel() {
		return channel;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getRedirectToUrl() {
		return redirectToUrl;
	}

	public void setRedirectToUrl(String redirectToUrl) {
		this.redirectToUrl = redirectToUrl;
	}

	public String getSms() {
		return sms;
	}

	public void setSms(String sms) {
		this.sms = sms;
	}
}
