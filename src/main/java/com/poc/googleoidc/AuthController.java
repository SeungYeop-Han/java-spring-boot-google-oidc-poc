package com.poc.googleoidc;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    /**
     * 루트 경로에 대한 핸들러
     */
    @GetMapping("/")
    public String rooooot() {
        return "→ /";
    }

    /**
     * 구글 OIDC 최초 진입 경로에 대한 핸들러
     *
     */
    @GetMapping("/oauth2/authorization/google")
    public String auth() {
        return "→ /oauth2/authorization/google";
    }
}
