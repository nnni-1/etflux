package ru.alexeev.etflux.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(httpRequests ->
                        httpRequests
                                .requestMatchers("/hello").hasRole("ADMIN")
                                .requestMatchers("/login").anonymous()
                                .requestMatchers("/register").permitAll()
                                .anyRequest().permitAll())
                .formLogin(formLogin -> formLogin
                        .loginPage("/c-login")
                        .loginProcessingUrl("/login")
                        .permitAll())
                .sessionManagement(Customizer.withDefaults())
                .rememberMe(Customizer.withDefaults())
                .cors(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
