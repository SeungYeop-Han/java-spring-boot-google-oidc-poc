package com.poc.googleoidc.common.security;

import com.poc.googleoidc.common.security.jwt.JwtProperties;
import com.poc.googleoidc.user.domain.model.SocialAccount;
import com.poc.googleoidc.user.domain.model.User;
import com.poc.googleoidc.user.domain.model.enums.AuthProvider;
import com.poc.googleoidc.user.service.issuance.AccessTokenService;
import com.poc.googleoidc.user.service.issuance.AuthCookieService;
import com.poc.googleoidc.user.service.registration.SocialLoginService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OidcAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final SocialLoginService socialLoginService;
    private final AccessTokenService accessTokenService;
    private final AuthCookieService authCookieService;
    private final JwtProperties jwtProperties;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();

        System.out.println("oidcUser.getIdToken() = " + oidcUser.getIdToken());
        System.out.println("oidcUser.getClaims() = " + oidcUser.getClaims());
        System.out.println("oidcUser.getSubject() = " + oidcUser.getSubject());
        System.out.println("oidcUser.getNonce() = " + oidcUser.getNonce());
        System.out.println("oidcUser.getEmail() = " + oidcUser.getEmail());
        System.out.println("oidcUser.getProfile() = " + oidcUser.getProfile());

        // 1. User & SocialAccount 등록 및 연동
        AuthProvider provider = resolveAuthProvider(authentication);
        Pair<User, SocialAccount> userAndSocialAccount = socialLoginService.loginOrRegister(provider, oidcUser);

        // 2. access JWT 생성
        String accessToken = accessTokenService.create(userAndSocialAccount.getFirst());

        // 3) refresh token 원문 생성 + hash 저장
        // 4) access_token 쿠키 set
        HttpHeaders headers = new HttpHeaders();
        authCookieService.addAccessTokenCookie(headers, accessToken, jwtProperties.accessTokenTtl());
        headers.forEach((name, values) ->
                values.forEach(value -> response.addHeader(name, value))
        );

        // 5) refresh_token 쿠키 set
        // 6) 필요하면 OIDC용 세션 invalidate
        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }

        // 7) 프론트 페이지로 redirect
        response.sendRedirect("https://localhost:8443/login/success");
    }

    // ----- helpers

    private static AuthProvider resolveAuthProvider(Authentication authentication) {
        if (authentication instanceof OAuth2AuthenticationToken o) {
            return AuthProvider.of(o.getAuthorizedClientRegistrationId());
        }

        // To Do: throw new AuthException(...);
        return null;
    }
}
