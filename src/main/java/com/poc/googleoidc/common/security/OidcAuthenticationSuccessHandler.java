package com.poc.googleoidc.common.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OidcAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

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

        // 1) User / SocialAccount 조회 또는 생성
        // 2) access JWT 생성
        // 3) refresh token 원문 생성 + hash 저장
        // 4) access_token 쿠키 set
        // 5) refresh_token 쿠키 set
        // 6) 필요하면 OIDC용 세션 invalidate
        // 7) 프론트 페이지로 redirect
        response.sendRedirect("https://localhost:8443/login/success");
    }
}
