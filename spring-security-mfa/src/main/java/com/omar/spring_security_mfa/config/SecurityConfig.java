package com.omar.spring_security_mfa.config;

import com.omar.spring_security_mfa.security.ott.PinOneTimeTokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ott.OneTimeTokenService;
import org.springframework.security.config.annotation.authorization.EnableMultiFactorAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.FactorGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.time.Duration;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * 🔐 Spring Security Configuration for MFA Playground.
 *
 * <p>This configuration demonstrates how to set up Spring Security 7 with:
 * - Form login
 * - Multi-Factor Authentication (MFA) using password + one-time token (OTT)
 * - Role-based access control (USER/ADMIN)
 * - In-memory test users</p>
 *
 * <p>It also registers a custom {@link PinOneTimeTokenService} with 3-minute token expiration.</p>
 */
@Configuration
@EnableWebSecurity
@EnableMultiFactorAuthentication(authorities = {
        FactorGrantedAuthority.PASSWORD_AUTHORITY,
        FactorGrantedAuthority.OTT_AUTHORITY
})
public class SecurityConfig {


    // ===================================================================================
    // 🔑 Security Filter Chain
    // ===================================================================================

    /**
     * 🎛️ Defines HTTP security rules and login flows.
     * <p>
     * - "/" and "/ott/sent" → public access
     * - "/admin/**" → only accessible to ADMIN role
     * - Enables form login and one-time token login (OTT)
     *
     * @param http the {@link HttpSecurity} builder
     * @return configured {@link SecurityFilterChain}
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) {
        return http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/", "/ott/sent").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                )
                .formLogin(withDefaults())
                .oneTimeTokenLogin(withDefaults())
                .build();
    }

    // ===================================================================================
    // 👤 In-Memory Users
    // ===================================================================================

    /**
     * 👥 Defines in-memory users for testing MFA flows.
     * <p>
     * - "user" → USER role
     * - "admin" → ADMIN + USER roles
     * <p>
     * ⚠️ Passwords are stored as plaintext ({noop}) for demonstration only.
     *
     * @return {@link UserDetailsService} with preloaded users
     */
    @Bean
    UserDetailsService userDetailsService() {
        var user = User.withUsername("user")
                .password("{noop}password")
                .roles("USER")
                .build();
        var admin = User.withUsername("admin")
                .password("{noop}password")
                .roles("ADMIN","USER")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }


    // ===================================================================================
    // 🔐 One-Time Token Service
    // ===================================================================================

    /**
     * 🎟️ Registers a custom {@link PinOneTimeTokenService} bean.
     * <p>
     * - Generates 5-digit OTT codes
     * - Default token expiration: 3 minutes
     *
     * @return {@link PinOneTimeTokenService} instance for MFA flow
     */
    @Bean
    public OneTimeTokenService oneTimeTokenService() {
        PinOneTimeTokenService service = new PinOneTimeTokenService();
        service.setTokenExpiresIn(Duration.ofMinutes(3)); // ⏳ token validity
        return service;
    }

}