package org.macula.cloud.oauth2.domain;

import java.io.Serializable;
import java.util.List;

import org.macula.cloud.core.principal.SubjectPrincipal;
import org.macula.cloud.core.session.SessionId;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class SubjectUserDetails extends User implements Serializable {
	private static final long serialVersionUID = 4756434945055344523L;

	private final List<OAuth2User> users;

	private OAuth2User currentUser;

	public SubjectUserDetails(String username, List<OAuth2User> users) {
		super(username, SessionId.createNewSessionId(), AuthorityUtils.createAuthorityList("macula-cloud-gateway"));
		this.users = users;
	}

	public void alreadyAuthenticated() {
		if (users != null && users.size() == 1) {
			setCurrentUser(users.get(0));
		}
	}

	public List<OAuth2User> getUsers() {
		return users;
	}

	public void setCurrentUser(OAuth2User user) {
		this.currentUser = user;
	}

	public OAuth2User getCurrentUser() {
		return currentUser;
	}

	public SubjectPrincipal createPrincipal() {
		return new SubjectPrincipal(currentUser.getUsername(), currentUser.getSource());
	}

}
