package org.macula.cloud.oauth2.repository;

import java.util.List;

import org.macula.cloud.oauth2.domain.OAuth2User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OAuth2UserRepository extends JpaRepository<OAuth2User, Long> {
 
	@Query("from OAuth2User u where lower(u.username) = ?1 or lower(u.email) =?1 or lower(u.mobile) = ?1")
	List<OAuth2User> findByLogin(String login);

}
