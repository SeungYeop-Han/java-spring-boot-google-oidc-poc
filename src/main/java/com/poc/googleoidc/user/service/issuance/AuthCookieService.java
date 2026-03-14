package com.poc.googleoidc.user.service.issuance;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class AuthCookieService {

    private static final String ACCESS_TOKEN_COOKIE_NAME = "access_token";

    public void addAccessTokenCookie(
            HttpHeaders headers,
            String accessToken,
            Duration ttl
    ) {
        ResponseCookie cookie = ResponseCookie.from(ACCESS_TOKEN_COOKIE_NAME, accessToken)
                .httpOnly(true)
                .secure(false)  // To Do: HTTPS 적용 후 true 로 변경
                .path("/")
                .sameSite("Lax")
                .maxAge(ttl)
                .build();

        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public void expireAccessTokenCookie(HttpHeaders headers) {
        ResponseCookie cookie = ResponseCookie.from(ACCESS_TOKEN_COOKIE_NAME, "")
                .httpOnly(true)
                .secure(false)  // To Do: HTTPS 적용 후 true 로 변경
                .path("/")
                .sameSite("Lax")
                .maxAge(0)
                .build();

        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
