package com.poc.googleoidc.user.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Instant;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(name = "uk_refresh_user", columnNames = "user_id"),
        indexes = @Index(name = "idx_refresh_token_expires_at", columnList = "expires_at")
)
@Accessors(fluent = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    // ----- fields

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false, length = 64, unique = true)
    private String tokenHash;

    @Column(nullable = false, updatable = false)
    private Instant issuedAt;

    // 만료일시
    @Column(nullable = false)
    private Instant expiresAt;

    // 폐기일시
    private Instant revokedAt;

    // 갱신일시
    @Column(nullable = false)
    private Instant updatedAt;

    // ----- constructors

    @Builder
    private RefreshToken(
            Long id,
            User user,
            String tokenHash,
            Instant issuedAt,
            Instant expiresAt,
            Instant revokedAt,
            Instant updatedAt
    ) {

        validateId(id);
        validateUser(user);
        validateTokenHash(tokenHash);
        validateTimes(issuedAt, expiresAt, revokedAt, updatedAt);

        this.id = id;
        this.user = user;
        this.tokenHash = tokenHash;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.revokedAt = revokedAt;
        this.updatedAt = updatedAt;
    }

    // ----- static factories

    public static RefreshToken createNew(
            User user,
            String tokenHash,
            long ttlInSec
    ) {

        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusSeconds(ttlInSec);

        return RefreshToken.builder()
                .user(user)
                .tokenHash(tokenHash)
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .build();
    }

    public static RefreshToken rebuild(
            User user,
            String tokenHash,
            Instant issuedAt,
            Instant expiresAt,
            Instant revokedAt,
            Instant updatedAt
    ) {

        return RefreshToken.builder()
                .user(user)
                .tokenHash(tokenHash)
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .revokedAt(revokedAt)
                .updatedAt(updatedAt)
                .build();
    }

    // ----- validators

    private static void validateId(Long id) {
        // null 허용
        // To Do: 유효하지 않으면 예외 발생
    }

    private static void validateUser(User user) {
        // To Do: 유효하지 않으면 예외 발생
    }

    private static void validateTokenHash(String tokenHash) {
        // To Do: 유효하지 않으면 예외 발생
    }

    private static void validateTimes(Instant issuedAt, Instant expiresAt, Instant revokedAt, Instant updatedAt) {
        // To Do: 유효하지 않으면 예외 발생
    }

    // ----- domain logics

    // To Do: revoke
    // To Do: update
}
