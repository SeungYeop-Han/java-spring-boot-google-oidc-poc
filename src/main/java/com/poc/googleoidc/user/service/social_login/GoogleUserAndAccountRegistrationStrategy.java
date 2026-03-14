package com.poc.googleoidc.user.service.social_login;

import com.poc.googleoidc.user.domain.model.SocialAccount;
import com.poc.googleoidc.user.domain.model.User;
import com.poc.googleoidc.user.domain.model.consts.UserAccountConstants;
import com.poc.googleoidc.user.domain.model.enums.AuthProvider;
import com.poc.googleoidc.user.repository.SocialAccountRepository;
import com.poc.googleoidc.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Optional;

@RequiredArgsConstructor
public class GoogleUserAndAccountRegistrationStrategy implements UserAndAccountRegistrationStrategy {

    // SocialLoginStrategyFactory 가 생성하면서 주입해줌
    private final UserRepository userRepository;
    private final SocialAccountRepository socialAccountRepository;

    @Override
    public Pair<User, SocialAccount> tryRegistration(OidcUser oidcUser) {

        String sub = oidcUser.getSubject();
        String email = oidcUser.getEmail();
        boolean isEmailVerified = oidcUser.getEmailVerified();
        String nickname = resolveNickname(oidcUser);

        validateGoogleClaims(sub, email);

        Optional<SocialAccount> socialAccountOptional
                = socialAccountRepository.findByProviderAndSub(AuthProvider.GOOGLE, sub);
        Optional<User> userOptional = userRepository.findByEmail(email);

        // 1. 둘 모두 있는 경우
        if (socialAccountOptional.isPresent() && userOptional.isPresent()) {
            // 등록하지 않고 기존 것 반환
            return Pair.of(userOptional.get(), socialAccountOptional.get());
        }
        
        // 2. User 만 있는 경우
        if (socialAccountOptional.isEmpty() && userOptional.isPresent()) {
            // SocialAccount 생성 및 기존 회원에 연결 후 반환
            SocialAccount newGoogleAccount
                    = SocialAccount.createNewGoogleAccount(userOptional.get(), sub, email, isEmailVerified);
            return Pair.of(userOptional.get(), newGoogleAccount);
        }
        
        // 3. SocialAccount 만 있는 경우(비정상)
        if (socialAccountOptional.isPresent() && userOptional.isEmpty()) {
            // To Do: throw new AuthException(...);
            throw new RuntimeException();
        }
        
        // 4. 둘 모두 없는 경우
        if (socialAccountOptional.isEmpty() && userOptional.isEmpty()) {
            // SocialAccount 와 User 를 생성 후 반환
            User newUser = User.createNew(email, nickname);
            SocialAccount newGoogleAccount
                    = SocialAccount.createNewGoogleAccount(newUser, sub, email, isEmailVerified);
            return Pair.of(newUser, newGoogleAccount);
        }

        // 위의 4 가지 경우 중 어느 것에도 해당하지 않는 경우 예외 발생
        // To Do: throw new AuthException(...);
        throw new RuntimeException();
    }

    // ----- helpers

    private String resolveNickname(OidcUser oidcUser) {

        String[] nicknameCandidates = new String[]{
                oidcUser.getFullName(),
                "%s %s %s".formatted(
                        oidcUser.getFamilyName(),
                        oidcUser.getMiddleName(),
                        oidcUser.getGivenName()
                ),
                oidcUser.getName(),
                oidcUser.getNickName(),
                oidcUser.getEmail()
        };

        for (String candidate : nicknameCandidates) {
            boolean neitherNullNorBlank = candidate != null && !candidate.isBlank();
            if (neitherNullNorBlank) {
                return truncate(candidate, UserAccountConstants.MAX_NICKNAME_LENGTH);
            }
        }

        return UserAccountConstants.DEFAULT_NICKNAME;
    }

    private String truncate(String target, int maxLength) {
        return target.length() <= maxLength ? target : target.substring(0, maxLength);
    }

    private void validateGoogleClaims(String sub, String email) {
        if (sub == null || sub.isBlank()) {
            // To Do: throw new AuthException(...);
        }
        if (email == null || email.isBlank()) {
            // To Do: throw new AuthException(...);
        }
    }
}
