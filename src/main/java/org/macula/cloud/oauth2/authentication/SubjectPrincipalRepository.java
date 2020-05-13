package org.macula.cloud.oauth2.authentication;

import java.util.List;

import org.macula.cloud.oauth2.domain.OAuth2User;
import org.macula.cloud.oauth2.policy.PasswordValidationPolicy;
import org.macula.cloud.oauth2.repository.OAuth2UserRepository;
import org.macula.cloud.core.principal.SubjectCredential;
import org.macula.cloud.core.principal.SubjectPrincipal;
import org.macula.cloud.core.principal.SubjectPrincipalLoadRepository;
import org.macula.cloud.core.principal.SubjectPrincipalSessionStorage;
import org.macula.cloud.core.principal.SubjectType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Service
public class SubjectPrincipalRepository implements SubjectPrincipalLoadRepository {

	private final SubjectPrincipalSessionStorage principalStorage;
	private final OAuth2UserRepository oauth2UserRepository;
	private final List<PasswordValidationPolicy> passwordValidationPolicies;

	@Override
	public SubjectPrincipal load(SubjectCredential credential) {
		String username = credential.getUsername();
		String source = credential.getSource();
		SubjectType subjectType = credential.getType();

		if (subjectType == SubjectType.USER) {
			List<OAuth2User> users = oauth2UserRepository.findByLogin(username.toLowerCase());
			SubjectPrincipal userPrincipal = users.stream().filter(user -> checkSource(user, source)).findFirst()
					.map(user -> new SubjectPrincipal(user.getUsername(), user.getSource())).orElse(null);

			if (userPrincipal != null) {
				userPrincipal.addAuthority("macula-cloud-gateway");
				credential.setSource(userPrincipal.getSource());
				credential.makePasswordProtected();
				userPrincipal.setCredential(credential);
				principalStorage.commit(userPrincipal);
				return userPrincipal;
			}
		}

		if (subjectType == SubjectType.CLIENT) {
			log.error("Unsupport {} load...", subjectType);
		}

		return null;
	}

	@Override
	public SubjectPrincipal login(SubjectCredential credential) {
		String username = credential.getUsername();
		String password = credential.getPassword();
		SubjectType subjectType = credential.getType();

		if (subjectType == SubjectType.USER) {
			List<OAuth2User> users = oauth2UserRepository.findByLogin(username.toLowerCase());
			SubjectPrincipal subjectPrincipal = users.stream().filter(user -> checkPassword(user, password)).findFirst()
					.map(user -> new SubjectPrincipal(user.getUsername(), user.getSource())).orElse(null);

			if (subjectPrincipal != null) {
				subjectPrincipal.addAuthority("macula-cloud-gateway");
				credential.setSource(subjectPrincipal.getSource());
				credential.makePasswordProtected();
				subjectPrincipal.setCredential(credential);
				principalStorage.commit(subjectPrincipal);
				return subjectPrincipal;
			}
		}

		if (subjectType == SubjectType.CLIENT) {
			log.error("Unsupport {} login...", subjectType);
		}

		return null;

	}

	protected boolean checkSource(OAuth2User user, String source) {
		return source != null && source.equalsIgnoreCase(user.getSource());
	}

	protected boolean checkPassword(OAuth2User user, String presentedPassword) {
		if (!CollectionUtils.isEmpty(passwordValidationPolicies)) {
			for (PasswordValidationPolicy passwordValidationPolicy : passwordValidationPolicies) {
				if (passwordValidationPolicy.support(user.getSource())
						&& passwordValidationPolicy.validate(user, presentedPassword)) {
					return true;
				}
			}
		}
		return false;
	}

}
