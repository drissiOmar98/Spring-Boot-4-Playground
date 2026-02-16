package com.omar.spring_security_mfa.security.ott;

import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.ott.*;
import org.springframework.util.Assert;

import java.security.SecureRandom;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 🔐 Custom One-Time Token (OTT) Service using 5-digit PINs.
 *
 * <p>This service is used in Multi-Factor Authentication (MFA) flows to generate
 * short-lived 5-digit tokens. These tokens can be sent via email, SMS, or other
 * channels and consumed to verify additional authentication factors.</p>
 *
 * ⚠️ Notes:
 * - 5-digit PINs only provide 100,000 combinations. For high-traffic production,
 *   consider database-backed storage or collision detection.
 * - Default expiration is 5 minutes. Short expiration mitigates brute-force risks.
 * <p>
 * Implements {@link OneTimeTokenService} to integrate with Spring Security 7 MFA.
 */
public class PinOneTimeTokenService implements OneTimeTokenService {


    /*
     * The collision risk is higher with 5-digit PINs (100,000 possible values) compared to UUIDs.
     * For production with high traffic, you might want to add collision detection or use a database-backed implementation
     */
    private static final int PIN_LENGTH = 5;
    private static final int MAX_PIN_VALUE = 100_000;

    /** 🗃️ Store active tokens in memory (tokenValue -> OneTimeToken) */
    private final Map<String, OneTimeToken> oneTimeTokenByToken = new ConcurrentHashMap<>();

    /** 🎲 Secure random number generator for PIN generation */
    private final SecureRandom secureRandom = new SecureRandom();

    /** ⏰ Clock used for expiration checking */
    private Clock clock = Clock.systemUTC();
    // Consider setting a shorter expiration time for these PINs (typically 5-10 minutes for SMS codes) since they're more susceptible to brute force than UUIDs
    private Duration tokenExpiresIn = Duration.ofMinutes(5);


    /**
     * 🛠️ Generates a new one-time token for a given request.
     *
     * @param request the request containing username and additional info
     * @return the generated OneTimeToken
     */
    @Override
    public OneTimeToken generate(GenerateOneTimeTokenRequest request) {
        String token = generatePin(); // Generate secure 5-digit PIN
        Instant expiresAt = this.clock.instant().plus(this.tokenExpiresIn);
        OneTimeToken ott = new DefaultOneTimeToken(token, request.getUsername(), expiresAt);

        // Store the token in memory
        this.oneTimeTokenByToken.put(token, ott);

        // 🔄 Clean expired tokens if needed to save memory
        cleanExpiredTokensIfNeeded();
        return ott;
    }

    /**
     * ✅ Consumes a one-time token. Returns null if token is invalid or expired.
     *
     * @param authenticationToken the authentication token containing PIN
     * @return the consumed OneTimeToken or null if invalid/expired
     */
    @Override
    public @Nullable OneTimeToken consume(OneTimeTokenAuthenticationToken authenticationToken) {
        OneTimeToken ott = this.oneTimeTokenByToken.remove(authenticationToken.getTokenValue());
        if (ott == null || isExpired(ott)) {
            return null;
        }
        return ott;
    }

    /**
     * ⏳ Configure how long a token is valid.
     *
     * @param tokenExpiresIn duration before token expires
     */
    public void setTokenExpiresIn(Duration tokenExpiresIn) {
        Assert.notNull(tokenExpiresIn, "tokenExpiresIn cannot be null");
        Assert.isTrue(!tokenExpiresIn.isNegative() && !tokenExpiresIn.isZero(),
                "tokenExpiresIn must be positive");
        this.tokenExpiresIn = tokenExpiresIn;
    }

    /**
     * 🔢 Generate a random 5-digit PIN as a string.
     *
     * @return formatted PIN string
     */
    private String generatePin() {
        int pin = secureRandom.nextInt(MAX_PIN_VALUE);
        return String.format("%0" + PIN_LENGTH + "d", pin);
    }

    /**
     * 🧹 Clean expired tokens if memory map exceeds threshold.
     */
    private void cleanExpiredTokensIfNeeded() {
        if (this.oneTimeTokenByToken.size() < 100) {
            return;
        }
        for (Map.Entry<String, OneTimeToken> entry : this.oneTimeTokenByToken.entrySet()) {
            if (isExpired(entry.getValue())) {
                this.oneTimeTokenByToken.remove(entry.getKey());
            }
        }
    }

    private boolean isExpired(OneTimeToken ott) {
        return this.clock.instant().isAfter(ott.getExpiresAt());
    }

    public void setClock(Clock clock) {
        Assert.notNull(clock, "clock cannot be null");
        this.clock = clock;
    }

}