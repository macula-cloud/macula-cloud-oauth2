package org.macula.cloud.oauth2.controller;

import org.macula.cloud.core.principal.SubjectPrincipal;
import org.macula.cloud.core.principal.SubjectPrincipalSessionStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuth2UserProfileController {

	@Autowired
	private SubjectPrincipalSessionStorage sessionStorage;

	@RequestMapping(value = { "/user/me", "/api/profile/me" })
	public Object user(Authentication authentication) {
		if (authentication != null && authentication.getPrincipal() instanceof SubjectPrincipal) {
			String guid = ((SubjectPrincipal) authentication.getPrincipal()).getUserId();
			SubjectPrincipal principal = sessionStorage.checkoutPrincipal(guid);
			if (principal != null) {
				return principal;
			}
		}

		return authentication;
	}

}
