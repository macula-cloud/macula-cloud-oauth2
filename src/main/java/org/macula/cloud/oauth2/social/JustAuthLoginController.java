package org.macula.cloud.oauth2.social;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.macula.cloud.core.utils.J2CacheUtils;
import org.macula.cloud.oauth2.config.AuthRequestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;

@RestController
@RequestMapping("/login")
public class JustAuthLoginController {

	@Autowired
	private AuthRequestFactory authRequestFactory;

	@RequestMapping("/{source}/authorize")
	public void render(@PathVariable("source") String source,
			@RequestHeader(name = "Referer", required = false) String referer, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		AuthRequest authRequest = authRequestFactory.get(source);
		String state = AuthStateUtils.createState();
		if (StringUtils.isNotEmpty(referer)) {
			J2CacheUtils.set("state", state, referer);
		}
		response.sendRedirect(authRequest.authorize(state));
	}

}
