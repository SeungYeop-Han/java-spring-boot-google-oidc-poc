package com.poc.googleoidc.user.service.social_login;

import com.poc.googleoidc.user.domain.model.SocialAccount;
import com.poc.googleoidc.user.domain.model.User;
import org.springframework.data.util.Pair;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

// 확장성과 가독성을 개선하기 위해 전략 패턴 사용
public interface UserAndAccountRegistrationStrategy {

    Pair<User, SocialAccount> tryRegistration(OidcUser oidcUser);
}
