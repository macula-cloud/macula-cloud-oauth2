package org.macula.cloud.oauth2.repository;

import org.macula.cloud.oauth2.domain.OAuth2Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuth2ClientRepository extends JpaRepository<OAuth2Client, Long> {

	OAuth2Client findByClientId(String clientId);

}
