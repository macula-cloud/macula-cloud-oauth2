package org.macula.cloud.oauth2.central;

import org.macula.cloud.core.principal.SubjectPrincipal;
import org.macula.cloud.oauth2.domain.OAuth2User;
import org.macula.cloud.oauth2.repository.OAuth2UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class OAuth2UserDetailsService implements UserDetailsService {

	private final OAuth2UserRepository oauth2UserRepository;

	public OAuth2UserDetailsService(OAuth2UserRepository oauth2UserRepository) {
		this.oauth2UserRepository = oauth2UserRepository;
	}

	@Override
	public SubjectPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
		OAuth2User user = oauth2UserRepository.findByLogin(username.toLowerCase());
		if (user == null) {
			return null;
		}
		SubjectPrincipal principal = new SubjectPrincipal(user.getUsername(), user.getPassword());
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

	//	private final SubjectPrincipalSessionStorage principalStorage;
	//	private final List<SourceLoginStrategy> passwordValidationPolicies;
	//
	//	@Override
	//	public SubjectPrincipal load(SubjectCredential credential) {
	//		String username = credential.getUsername();
	//		String source = credential.getSource();
	//		SubjectType subjectType = credential.getType();
	//
	//		if (subjectType == SubjectType.USER) {
	//			List<OAuth2User> users = oauth2UserRepository.findByLogin(username.toLowerCase());
	//			SubjectPrincipal userPrincipal = users.stream().filter(user -> checkSource(user, source)).findFirst()
	//					.map(user -> new SubjectPrincipal(user.getUsername(), user.getSource())).orElse(null);
	//
	//			if (userPrincipal != null) {
	//				userPrincipal.addAuthority("macula-cloud-gateway");
	//				credential.setSource(userPrincipal.getSource());
	//				credential.makePasswordProtected();
	//				userPrincipal.setCredential(credential);
	//				principalStorage.commit(userPrincipal);
	//				return userPrincipal;
	//			}
	//		}
	//
	//		if (subjectType == SubjectType.CLIENT) {
	//			log.error("Unsupport {} load...", subjectType);
	//		}
	//
	//		return null;
	//	}
	//
	//	@Override
	//	public SubjectPrincipal login(SubjectCredential credential) {
	//		String username = credential.getUsername();
	//		String password = credential.getPassword();
	//		SubjectType subjectType = credential.getType();
	//
	//		if (subjectType == SubjectType.USER) {
	//
	//			if (subjectPrincipal != null) {
	//				subjectPrincipal.addAuthority("macula-cloud-gateway");
	//				credential.setSource(subjectPrincipal.getSource());
	//				credential.makePasswordProtected();
	//				subjectPrincipal.setCredential(credential);
	//				principalStorage.commit(subjectPrincipal);
	//				return subjectPrincipal;
	//			}
	//		}
	//
	//		if (subjectType == SubjectType.CLIENT) {
	//			log.error("Unsupport {} login...", subjectType);
	//		}
	//
	//		return null;
	//
	//	}
	//
	//	protected boolean checkSource(OAuth2User user, String source) {
	//		return source != null && source.equalsIgnoreCase(user.getSource());
	//	}
	//
	//	protected boolean loginValidation(OAuth2User user, String presentedPassword) {
	//		if (!CollectionUtils.isEmpty(passwordValidationPolicies)) {
	//			for (SourceLoginStrategy passwordValidationPolicy : passwordValidationPolicies) {
	//				if (passwordValidationPolicy.support(user.getSource()) && passwordValidationPolicy.validate(user, presentedPassword)) {
	//					return true;
	//				}
	//			}
	//		}
	//		return false;
	//	}

}
