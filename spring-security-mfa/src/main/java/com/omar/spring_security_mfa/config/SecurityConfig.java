package com.omar.spring_security_mfa.config;

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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

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



}