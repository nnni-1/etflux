package ru.alexeev.etflux.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;

import static ru.alexeev.etflux.dictionary.UserRoleEnum.ADMIN;
import static ru.alexeev.etflux.dictionary.UserRoleEnum.MODERATOR;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    private static final String LOGIN_PATH = "/api/v1/login";

    private final List<String> ANONYMOUS_PATHS = List.of(
            LOGIN_PATH, "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**");

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(httpRequests ->
                        httpRequests
                                .requestMatchers("/admin/*", "/api/admin/*").hasRole(ADMIN.name())
                                .requestMatchers("/login", "/register").anonymous()
                                .requestMatchers("/api/v1/users", "/logout").permitAll()
                                .anyRequest().permitAll())
                .formLogin(formLogin -> formLogin
                        .loginPage("/c-login")
                        .loginProcessingUrl("/login")
                        .permitAll())
//                .addFilterAt(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//                .addFilterAt(loginAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .rememberMe(Customizer.withDefaults())
                .cors(AbstractHttpConfigurer::disable)
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutSuccessUrl("/c-login"))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Algorithm algorithm(@Value("${jwt.secret}") String secret) {
        return Algorithm.HMAC256(secret);
    }

    @Bean
    public AuthenticationManager authenticationManager(List<AuthenticationProvider> providers){
        return new ProviderManager(providers);
    }

//    @Bean
//    public LoginAuthenticationFilter loginAuthenticationFilter(
//            AuthenticationManager authenticationManager,
//            @Qualifier("loginAuthenticationSuccessHandler") AuthenticationSuccessHandler successHandler,
//            AuthenticationFailureHandler failureHandler) {
//        return new LoginAuthenticationFilter(
//                LOGIN_PATH, authenticationManager, successHandler, failureHandler);
//    }
//
//    @Bean
//    public TokenAuthenticationFilter tokenAuthenticationFilter(
//            JwtService jwtService,
//            AuthenticationManager authenticationManager,
//            AuthenticationFailureHandler failureHandler) {
//        RequestMatcher requestMatcher = new AntPathRequestMatcher("/**/admin/**");
//
//        return new TokenAuthenticationFilter(
//                requestMatcher, jwtService, authenticationManager, failureHandler);
//    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(
            PasswordEncoder passwordEncoder,
            UserDetailsService userDetailsService) {
        var provider = new DaoAuthenticationProvider(passwordEncoder);

        provider.setUserDetailsService(userDetailsService);

        return provider;
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new SimpleUrlAuthenticationFailureHandler();
    }

    @Bean
    public JWTVerifier jwtVerifier(Algorithm algorithm) {
        return JWT.require(algorithm).build();
    }

    @Bean
    public JavaMailSender mailSender(){
        return new JavaMailSenderImpl();
    }


}
