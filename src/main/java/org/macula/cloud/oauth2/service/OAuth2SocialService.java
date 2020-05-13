package org.macula.cloud.oauth2.service;

import org.macula.cloud.oauth2.domain.OAuth2Social;
import org.macula.cloud.oauth2.repository.OAuth2SocialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.zhyd.oauth.model.AuthUser;

@Service
public class OAuth2SocialService {

	@Autowired
	private OAuth2SocialRepository ucRepository;

	public OAuth2Social createOAuth2Social(AuthUser user) {
		OAuth2Social uc = new OAuth2Social();
		uc.setSocialId(user.getSource().name());
		user.setUuid(user.getUuid());
		if (user.getToken() != null) {
			uc.setUnionId(user.getToken().getUnionId());
			uc.setOpenId(user.getToken().getOpenId());
			uc.setSessionKey(user.getToken().getAccessToken());
		}
		uc.setNickname(user.getNickname());
		uc.setAvatar(user.getAvatar());
		uc.setEmail(user.getEmail());
		uc.setUsername(user.getUsername());
		uc.setLocation(user.getLocation());
		uc.setGender(user.getGender().name());
		uc.setCompany(user.getCompany());
		uc.setBlog(user.getBlog());
		uc.setRemark(user.getRemark());
		return ucRepository.save(uc);
	}
}
