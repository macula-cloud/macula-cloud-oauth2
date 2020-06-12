package org.macula.cloud.oauth2.service;

import javax.transaction.Transactional;

import org.macula.cloud.core.command.CreateSocialUserCommand;
import org.macula.cloud.oauth2.domain.OAuth2User;
import org.macula.cloud.oauth2.domain.UserSocial;
import org.macula.cloud.oauth2.repository.OAuth2UserRepository;
import org.macula.cloud.oauth2.repository.UserSocialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.json.JSONUtils;

import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthUser;

@Service
@Slf4j
public class UserSocialService {

	@Autowired
	private UserSocialRepository ucRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private OAuth2UserRepository userRepository;

	@Transactional
	public String createSocialUser(CreateSocialUserCommand command) {
		OAuth2User user = userRepository.findByLogin(command.getUser().getUsername());
		if (user == null) {
			user = new OAuth2User();
		}
		user.clone(command.getUser());
		if (user.getPassword() == null) {
			user.setPassword(command.getSocial().getOpenId());
		}
		user.setLocked(false);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);

		log.info("Created user: {}", JSONUtils.toJSONString(user));

		UserSocial social = ucRepository.findSocialUser(user.getUsername(), command.getSocial().getOpenId());
		if (social == null) {
			social = new UserSocial();
		}
		social.clone(command.getSocial());
		social.setUserId(String.valueOf(user.getId()));
		social.setUsername(user.getUsername());
		ucRepository.save(social);
		log.info("Created social: {}", JSONUtils.toJSONString(social));
		return user.getUsername();
	}

	public UserSocial createOAuth2Social(AuthUser user) {
		UserSocial uc = new UserSocial();
		uc.setAppid(user.getSource().name());
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
