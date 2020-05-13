package org.macula.cloud.oauth2.config;

import lombok.RequiredArgsConstructor;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.config.AuthSource;
import me.zhyd.oauth.enums.AuthResponseStatus;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.request.AuthAlipayRequest;
import me.zhyd.oauth.request.AuthBaiduRequest;
import me.zhyd.oauth.request.AuthCodingRequest;
import me.zhyd.oauth.request.AuthDingTalkRequest;
import me.zhyd.oauth.request.AuthDouyinRequest;
import me.zhyd.oauth.request.AuthFacebookRequest;
import me.zhyd.oauth.request.AuthGiteeRequest;
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.request.AuthGoogleRequest;
import me.zhyd.oauth.request.AuthLinkedinRequest;
import me.zhyd.oauth.request.AuthMiRequest;
import me.zhyd.oauth.request.AuthMicrosoftRequest;
import me.zhyd.oauth.request.AuthOschinaRequest;
import me.zhyd.oauth.request.AuthPinterestRequest;
import me.zhyd.oauth.request.AuthQqRequest;
import me.zhyd.oauth.request.AuthRenrenRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.request.AuthStackOverflowRequest;
import me.zhyd.oauth.request.AuthTaobaoRequest;
import me.zhyd.oauth.request.AuthTeambitionRequest;
import me.zhyd.oauth.request.AuthTencentCloudRequest;
import me.zhyd.oauth.request.AuthToutiaoRequest;
import me.zhyd.oauth.request.AuthWeChatRequest;
import me.zhyd.oauth.request.AuthWeiboRequest;

@RequiredArgsConstructor
public class AuthRequestFactory {

	private final AuthRequestProperties properties;

	public AuthRequest get(String source) {
		return get(AuthSource.valueOf(source.toUpperCase()));
	}

	public AuthRequest get(AuthSource source) {
		AuthConfig config = properties.getType().get(source);
		switch (source) {
		case GITHUB:
			return new AuthGithubRequest(config);
		case WEIBO:
			return new AuthWeiboRequest(config);
		case GITEE:
			return new AuthGiteeRequest(config);
		case DINGTALK:
			return new AuthDingTalkRequest(config);
		case BAIDU:
			return new AuthBaiduRequest(config);
		case CODING:
			return new AuthCodingRequest(config);
		case TENCENT_CLOUD:
			return new AuthTencentCloudRequest(config);
		case OSCHINA:
			return new AuthOschinaRequest(config);
		case ALIPAY:
			return new AuthAlipayRequest(config);
		case QQ:
			return new AuthQqRequest(config);
		case WECHAT:
			return new AuthWeChatRequest(config);
		case TAOBAO:
			return new AuthTaobaoRequest(config);
		case GOOGLE:
			return new AuthGoogleRequest(config);
		case FACEBOOK:
			return new AuthFacebookRequest(config);
		case DOUYIN:
			return new AuthDouyinRequest(config);
		case LINKEDIN:
			return new AuthLinkedinRequest(config);
		case MICROSOFT:
			return new AuthMicrosoftRequest(config);
		case MI:
			return new AuthMiRequest(config);
		case TOUTIAO:
			return new AuthToutiaoRequest(config);
		case TEAMBITION:
			return new AuthTeambitionRequest(config);
		case RENREN:
			return new AuthRenrenRequest(config);
		case PINTEREST:
			return new AuthPinterestRequest(config);
		case STACK_OVERFLOW:
			return new AuthStackOverflowRequest(config);
		default:
			throw new AuthException(AuthResponseStatus.UNSUPPORTED);
		}
	}
}
