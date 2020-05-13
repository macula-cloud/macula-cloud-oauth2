package org.macula.cloud.oauth2.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.macula.cloud.oauth2.domain.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {

	AccessToken findByTokenId(String accessToken);

	@Transactional
	int deleteByTokenId(String accessToken);

	@Transactional
	int deleteByRefreshToken(String refreshToken);

	AccessToken findByAuthenticationId(String authenticationId);

	List<AccessToken> findByUserNameAndClientId(String userName, String clientId);

	List<AccessToken> findByClientId(String clientId);

}
