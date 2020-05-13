package org.macula.cloud.oauth2.repository;

import javax.transaction.Transactional;

import org.macula.cloud.oauth2.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	RefreshToken findByTokenId(String refreshToken);

	@Transactional
	int deleteByTokenId(String refreshToken);

}
