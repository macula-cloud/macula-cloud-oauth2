package org.macula.cloud.oauth2.repository;

import org.macula.cloud.oauth2.domain.OAuth2Social;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuth2SocialRepository extends JpaRepository<OAuth2Social, Long> {

}
