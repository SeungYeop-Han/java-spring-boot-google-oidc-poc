package com.poc.googleoidc.user.service.registration;

import com.poc.googleoidc.user.domain.model.SocialAccount;
import com.poc.googleoidc.user.domain.model.User;
import com.poc.googleoidc.user.domain.model.enums.AuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SocialLoginService {


    private final UserAndAccountRegistrationStrategyFactory userAndAccountRegistrationStrategyFactory;

    @Transactional
    public Pair<User, SocialAccount> loginOrRegister(AuthProvider provider, OidcUser oidcUser) {

        UserAndAccountRegistrationStrategy userAndAccountRegistrationStrategy
                = userAndAccountRegistrationStrategyFactory.get(provider);

        return userAndAccountRegistrationStrategy.tryRegistration(oidcUser);
    }
}
