package ru.alexeev.etflux.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import java.util.List;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(httpRequests ->
                        httpRequests
                                .requestMatchers("/hello").hasRole("ADMIN")
                                .requestMatchers("/login").anonymous())
                .formLogin(Customizer.withDefaults())
                .sessionManagement(Customizer.withDefaults())
                .rememberMe(Customizer.withDefaults())
                .build();
    }

    @Bean
    public AuthenticationManager authenticationProvider(List<AuthenticationProvider> providers) {
        return new ProviderManager(providers);
    }


    @Bean
    public Algorithm algorithm(@Value("@{jwt.secret}") String secret) {
        return Algorithm.HMAC256(secret);
    }

    @Bean
    public JWTVerifier jwtVerifier(Algorithm algorithm) {
        return JWT.require(algorithm).build();
    }
}
