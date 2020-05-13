package org.macula.cloud.oauth2.repository;

import org.macula.cloud.oauth2.domain.UnionConnect;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnionConnectRepository extends JpaRepository<UnionConnect, Long> {

	UnionConnect findByConnectIdAndOpenId(String connectId, String openId);

	UnionConnect findByConnectIdAndUnionId(String connectId, String unionId);
	
}
