package org.macula.cloud.oauth2.service;

import javax.transaction.Transactional;

import org.macula.cloud.oauth2.domain.OAuth2User;
import org.macula.cloud.oauth2.repository.OAuth2UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.zhyd.oauth.model.AuthUser;

@Service
public class OAuth2UserService {

	private boolean createAuthUser = true;

	@Autowired
	private OAuth2UserRepository userRepository;

	@Transactional
	public boolean createAuthUser(AuthUser user) {
		if (createAuthUser) {
			OAuth2User oauth2User = new OAuth2User();
			oauth2User.setAccount(user.getUsername());
			oauth2User.setUsername(user.getUsername());
			oauth2User.setAvatar(user.getAvatar());
			oauth2User.setGender(user.getGender().name());
			oauth2User.setEmail(user.getEmail());
			oauth2User.setRealname(user.getNickname());
			oauth2User.setNickname(user.getNickname());
			oauth2User.setSource(user.getSource().name());
			oauth2User.setEnabled(true);
			userRepository.save(oauth2User);
		}
		return createAuthUser;
	}

}
