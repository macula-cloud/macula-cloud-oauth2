package org.macula.cloud.oauth2.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.config.AuthSource;

@Getter
@Setter
@ConfigurationProperties(prefix = "justauth")
public class AuthRequestProperties {

	/**
	 * 是否启用 JustAuth
	 */
	private boolean enabled;

	/**
	 * JustAuth 配置
	 */
	private Map<AuthSource, AuthConfig> type = new HashMap<>();
}
