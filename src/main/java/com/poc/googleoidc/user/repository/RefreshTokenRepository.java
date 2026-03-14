package com.poc.googleoidc.user.repository;

import com.poc.googleoidc.user.domain.model.RefreshToken;
import com.poc.googleoidc.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByUser(User user);

    Optional<RefreshToken> findByTokenHash(String tokenHash);
}
