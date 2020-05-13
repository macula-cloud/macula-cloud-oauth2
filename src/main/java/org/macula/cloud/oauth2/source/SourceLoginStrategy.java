package org.macula.cloud.oauth2.source;

import org.macula.cloud.core.principal.LoginCredential;
import org.macula.cloud.core.principal.SubjectPrincipal;
import org.springframework.core.Ordered;

public interface SourceLoginStrategy extends Ordered {

	boolean support(String source);

	boolean validate(SubjectPrincipal principal, LoginCredential credential);
}
