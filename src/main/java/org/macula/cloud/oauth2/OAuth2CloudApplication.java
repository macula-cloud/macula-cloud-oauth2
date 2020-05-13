package org.macula.cloud.oauth2;

import org.macula.cloud.security.access.EnableSecurityAccess;
import org.macula.cloud.security.feign.EnableOAuth2ClientFeign;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringCloudApplication
@EnableWebSecurity
@EnableOAuth2ClientFeign
@EnableResourceServer
@EnableSecurityAccess
@EnableJpaRepositories
@EnableScheduling
@EntityScan("org.macula.cloud.oauth2")
@EnableFeignClients("org.macula.cloud.oauth2")
@EnableSwagger2
public class OAuth2CloudApplication {

	public static void main(String[] args) {
		SpringApplication.run(OAuth2CloudApplication.class, args);
	}

}