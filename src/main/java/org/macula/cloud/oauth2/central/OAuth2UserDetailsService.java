package org.macula.cloud.oauth2.central;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.macula.cloud.core.oauth2.SubjectPrincipal;
import org.macula.cloud.core.utils.SystemUtils;
import org.macula.cloud.oauth2.domain.OAuth2User;
import org.macula.cloud.oauth2.repository.OAuth2UserRepository;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthUser;
import top.dcenter.ums.security.core.oauth.exception.RegisterUserFailureException;
import top.dcenter.ums.security.core.oauth.service.UmsUserDetailsService;

@Slf4j
@Service
@AllArgsConstructor
public class OAuth2UserDetailsService implements UserDetailsService, UmsUserDetailsService {

	private final OAuth2UserRepository oauth2UserRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public SubjectPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
		OAuth2User user = oauth2UserRepository.findByLogin(username.toLowerCase());

		if (user == null || !Boolean.TRUE.equals(user.getEnabled())) {
			return null;
		}

		boolean accountNonExpired = (user.getInactiveDate() == null || user.getInactiveDate().after(SystemUtils.getCurrentTime()))
				&& (user.getEffectiveDate() == null || user.getEffectiveDate().before(SystemUtils.getCurrentTime()));

		// TODO need load authorities ???
		List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList("macula-cloud-gateway");

		SubjectPrincipal principal = new SubjectPrincipal(user.getUsername(), user.getPassword(), !Boolean.FALSE.equals(user.getEnabled()),
				accountNonExpired, true, Boolean.FALSE.equals(user.getLocked()), authorityList);
		principal.setSource(user.getSource());
		principal.setAvatar(user.getAvatar());
		principal.setEmail(user.getEmail());
		principal.setLanguage(user.getLocale());
		principal.setMobile(user.getMobile());
		principal.setNickname(user.getNickname());
		principal.setOrganizationId(user.getOrgCode());
		principal.setTimeZone(user.getTimezone());
		principal.setTheme(user.getTheme());
		principal.setUserId(user.getId().toString());
		return principal;
	}

	@Override
	public UserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
		return this.loadUserByUsername(userId);
	}

	@Override
	public List<Boolean> existedByUsernames(String... usernames) throws IOException {
		List<Boolean> existedUsernames = new ArrayList<>(usernames.length);
		for (int i = 0; i < usernames.length; i++) {
			existedUsernames.add(oauth2UserRepository.findByLogin(usernames[i].toLowerCase()) != null);
		}
		return existedUsernames;
	}

	@Override
	public UserDetails registerUser(@NonNull AuthUser authUser, @NonNull String username, @NonNull String defaultAuthority, String decodeState)
			throws RegisterUserFailureException {

		// 第三方授权登录不需要密码, 这里随便设置的, 生成环境按自己的逻辑
		String encodedPassword = passwordEncoder.encode(authUser.getUuid());

		// 这里的 decodeState 可以根据自己实现的 top.dcenter.ums.security.core.oauth.service.Auth2StateCoder 接口的逻辑来传递必要的参数.
		// 比如: 第三方登录成功后的跳转地址
		final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		// 假设 decodeState 就是 redirectUrl, 我们直接把 redirectUrl 设置到 request 上
		// 后续经过成功处理器时直接从 requestAttributes.getAttribute("redirectUrl", RequestAttributes.SCOPE_REQUEST) 获取并跳转
		if (requestAttributes != null) {
			requestAttributes.setAttribute("redirectUrl", decodeState, RequestAttributes.SCOPE_REQUEST);
		}
		// 当然 decodeState 也可以传递从前端传到后端的用户信息, 注册到本地用户

		List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(defaultAuthority);

		// ... 用户注册逻辑
		OAuth2User user = oauth2UserRepository.findByLogin(username);
		if (user == null) {
			user = new OAuth2User();
		}
		if (user.getPassword() == null) {
			user.setPassword(encodedPassword);
		}
		user.setLocked(false);
		if (user.getUsername() == null) {
			user.setUsername(authUser.getUsername());
		}
		if (user.getAccount() == null) {
			user.setAccount(authUser.getUsername());
		}
		if (user.getAvatar() == null) {
			user.setAvatar(authUser.getAvatar());
		}
		user.setEnabled(true);
		user.setLocked(false);
		user.setSource(authUser.getSource());
		user.setNickname(authUser.getNickname());
		user.setEmail(authUser.getEmail());
		user.setGender(authUser.getGender().name());
		oauth2UserRepository.save(user);
		log.info("======>: 用户名：{}, 注册成功", username);
		return loadUserByUsername(username);
	}

	/**
	 * {@link #existedByUsernames(String...)} usernames 生成规则.
	 * 如需自定义重新实现此逻辑
	 * @param authUser     第三方用户信息
	 * @return  返回一个 username 数组
	 */
	@Override
	public String[] generateUsernames(AuthUser authUser) {
		return new String[] { authUser.getUsername(),
				// providerId = authUser.getSource()
				authUser.getUsername() + "_" + authUser.getSource(),
				// providerUserId = authUser.getUuid()
				authUser.getUsername() + "_" + authUser.getSource() + "_" + authUser.getUuid() };
	}
}
