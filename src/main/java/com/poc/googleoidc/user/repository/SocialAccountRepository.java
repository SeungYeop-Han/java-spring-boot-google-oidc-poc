package com.poc.googleoidc.user.repository;

import com.poc.googleoidc.user.domain.model.SocialAccount;
import com.poc.googleoidc.user.domain.model.enums.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {

    Optional<SocialAccount> findByProviderAndSub(AuthProvider provider, String sub);
}
