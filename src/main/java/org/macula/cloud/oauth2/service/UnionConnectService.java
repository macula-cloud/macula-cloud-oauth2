package org.macula.cloud.oauth2.service;

import org.macula.cloud.oauth2.domain.UnionConnect;
import org.macula.cloud.oauth2.repository.UnionConnectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.zhyd.oauth.model.AuthUser;

@Service
public class UnionConnectService {

	@Autowired
	private UnionConnectRepository ucRepository;

	public UnionConnect createUnionConnect(AuthUser user) {
		UnionConnect uc = new UnionConnect();
		uc.setConnectId(user.getSource().name());
		uc.setUnionId(user.getUuid());
		uc.setOpenId(user.getUuid());
		uc.setNickname(user.getNickname());
		uc.setAvatar(user.getAvatar());
		uc.setEmail(user.getEmail());
		uc.setUsername(user.getUsername());
		uc.setLocation(user.getLocation());
		uc.setGender(user.getGender().name());
		return ucRepository.save(uc);
	}
}
