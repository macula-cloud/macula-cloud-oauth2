package org.macula.cloud.oauth2.service;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.macula.cloud.oauth2.domain.AccessToken;
import org.macula.cloud.oauth2.domain.RefreshToken;
import org.macula.cloud.oauth2.repository.AccessTokenRepository;
import org.macula.cloud.oauth2.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OAuth2TokenStore implements TokenStore {

	@Autowired
	private AccessTokenRepository accessTokenRepository;
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	@Autowired
	private AuthenticationKeyGenerator authenticationKeyGenerator;

	@Override
	public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
		return readAuthentication(token.getValue());
	}

	@Override
	public OAuth2Authentication readAuthentication(String token) {
		OAuth2Authentication authentication = null;
		try {
			AccessToken oauth2AccessToken = accessTokenRepository.findByTokenId(extractTokenKey(token));
			if (oauth2AccessToken == null) {
				if (log.isInfoEnabled()) {
					log.info("Failed to find access token for token " + token);
				}
			} else {
				authentication = oauth2AccessToken.getOauth2Authentication();
			}
		} catch (IllegalArgumentException e) {
			log.warn("Failed to deserialize authentication for " + token, e);
			removeAccessToken(token);
		}
		return authentication;
	}

	@Override
	public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
		Map<String, Object> additionalInfo = new HashMap<>();
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		String sessionId = "";
		if (attr != null) {
			sessionId = attr.getRequest().getSession(true).getId();
		}
		additionalInfo.put("createTime", new Date());
		additionalInfo.put("sessionId", sessionId);
		((DefaultOAuth2AccessToken) token).setAdditionalInformation(additionalInfo);

		String refreshToken = null;
		if (token.getRefreshToken() != null) {
			refreshToken = token.getRefreshToken().getValue();
		}

		if (readAccessToken(token.getValue()) != null) {
			removeAccessToken(token.getValue());
		}

		AccessToken accessToken = new AccessToken();
		accessToken.setTokenId(extractTokenKey(token.getValue()));
		accessToken.setOauth2Token(token);
		accessToken.setAuthenticationId(authenticationKeyGenerator.extractKey(authentication));
		if (!authentication.isClientOnly()) {
			accessToken.setUserName(authentication.getName());
		}
		accessToken.setClientId(authentication.getOAuth2Request().getClientId());
		accessToken.setOauth2Authentication(authentication);
		accessToken.setRefreshToken(extractTokenKey(refreshToken));

		accessTokenRepository.save(accessToken);
	}

	@Override
	public OAuth2AccessToken readAccessToken(String tokenValue) {
		OAuth2AccessToken accessToken = null;

		try {
			AccessToken oauth2Token = accessTokenRepository.findByTokenId(extractTokenKey(tokenValue));
			if (oauth2Token == null) {
				if (log.isInfoEnabled()) {
					log.info("Failed to find access token for token " + tokenValue);
				}
			} else {
				accessToken = oauth2Token.getOauth2Token();
			}
		} catch (IllegalArgumentException e) {
			log.warn("Failed to deserialize access token for " + tokenValue, e);
			removeAccessToken(tokenValue);
		}
		return accessToken;
	}

	@Override
	public void removeAccessToken(OAuth2AccessToken token) {
		removeAccessToken(token.getValue());
	}

	public void removeAccessToken(String tokenValue) {
		accessTokenRepository.deleteByTokenId(extractTokenKey(tokenValue));
	}

	@Override
	public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
		RefreshToken token = new RefreshToken();
		token.setTokenId(extractTokenKey(refreshToken.getValue()));
		token.setOauth2Authentication(authentication);
		token.setOauth2Token(refreshToken);
		refreshTokenRepository.save(token);
	}

	@Override
	public OAuth2RefreshToken readRefreshToken(String tokenValue) {
		OAuth2RefreshToken refreshToken = null;

		try {
			RefreshToken token = refreshTokenRepository.findByTokenId(extractTokenKey(tokenValue));
			if (token == null) {
				if (log.isInfoEnabled()) {
					log.info("Failed to find refresh token for token " + tokenValue);
				}
			} else {
				refreshToken = token.getOauth2Token();
			}
		} catch (IllegalArgumentException e) {
			log.warn("Failed to deserialize refresh token for token " + tokenValue, e);
			removeRefreshToken(tokenValue);
		}
		return refreshToken;
	}

	public void removeRefreshToken(String token) {
		refreshTokenRepository.deleteByTokenId(extractTokenKey(token));
	}

	@Override
	public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
		return readAuthenticationForRefreshToken(token.getValue());
	}

	@Override
	public void removeRefreshToken(OAuth2RefreshToken token) {
		removeRefreshToken(token.getValue());
	}

	public OAuth2Authentication readAuthenticationForRefreshToken(String value) {
		OAuth2Authentication authentication = null;
		try {
			RefreshToken token = refreshTokenRepository.findByTokenId(extractTokenKey(value));
			if (token == null) {
				if (log.isInfoEnabled()) {
					log.info("Failed to find access token for token " + value);
				}
			} else {
				authentication = token.getOauth2Authentication();
			}
		} catch (IllegalArgumentException e) {
			log.warn("Failed to deserialize access token for " + value, e);
			removeRefreshToken(value);
		}
		return authentication;
	}

	@Override
	public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
		removeAccessTokenUsingRefreshToken(refreshToken.getValue());
	}

	public void removeAccessTokenUsingRefreshToken(String refreshToken) {
		accessTokenRepository.deleteByRefreshToken(extractTokenKey(refreshToken));
	}

	@Override
	public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
		OAuth2AccessToken accessToken = null;

		String key = authenticationKeyGenerator.extractKey(authentication);
		try {
			AccessToken token = accessTokenRepository.findByAuthenticationId(key);
			if (token == null) {
				if (log.isDebugEnabled()) {
					log.debug("Failed to find access token for authentication " + authentication);
				}
			} else {
				accessToken = token.getOauth2Token();
			}
		} catch (IllegalArgumentException e) {
			log.error("Could not extract access token for authentication " + authentication, e);
		}

		if (accessToken != null
				&& !key.equals(authenticationKeyGenerator.extractKey(readAuthentication(accessToken.getValue())))) {
			removeAccessToken(accessToken.getValue());
			// Keep the store consistent (maybe the same user is represented by this
			// authentication but the details have
			// changed)
			storeAccessToken(accessToken, authentication);
		}
		return accessToken;
	}

	@Override
	public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
		final List<OAuth2AccessToken> accessTokens = new ArrayList<OAuth2AccessToken>();

		List<AccessToken> tokens = accessTokenRepository.findByUserNameAndClientId(userName, clientId);
		if (CollectionUtils.isEmpty(tokens)) {
			if (log.isInfoEnabled()) {
				log.info("Failed to find access token for clientId " + clientId + " and userName " + userName);
			}
		} else {
			tokens.forEach(t -> accessTokens.add(t.getOauth2Token()));
		}

		return removeNulls(accessTokens);
	}

	@Override
	public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
		final List<OAuth2AccessToken> accessTokens = new ArrayList<OAuth2AccessToken>();

		List<AccessToken> tokens = accessTokenRepository.findByClientId(clientId);
		if (CollectionUtils.isEmpty(tokens)) {
			if (log.isInfoEnabled()) {
				log.info("Failed to find access token for clientId " + clientId);
			}
		} else {
			tokens.forEach(t -> accessTokens.add(t.getOauth2Token()));
		}

		return removeNulls(accessTokens);
	}

	private List<OAuth2AccessToken> removeNulls(List<OAuth2AccessToken> accessTokens) {
		List<OAuth2AccessToken> tokens = new ArrayList<OAuth2AccessToken>();
		for (OAuth2AccessToken token : accessTokens) {
			if (token != null) {
				tokens.add(token);
			}
		}
		return tokens;
	}

	protected String extractTokenKey(String value) {
		if (value == null) {
			return null;
		}
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).");
		}

		try {
			byte[] bytes = digest.digest(value.getBytes("UTF-8"));
			return String.format("%032x", new BigInteger(1, bytes));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("UTF-8 encoding not available.  Fatal (should be in the JDK).");
		}
	}
}
