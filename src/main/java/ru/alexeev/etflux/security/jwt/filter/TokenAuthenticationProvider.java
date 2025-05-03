package ru.alexeev.etflux.security.jwt.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import ru.alexeev.etflux.security.details.UserDetailsImpl;
import ru.alexeev.etflux.security.details.UserDetailsServiceImpl;
import ru.alexeev.etflux.security.jwt.JwtAuthenticationToken;
import ru.alexeev.etflux.security.jwt.service.JwtService;



@Component
@RequiredArgsConstructor
public class TokenAuthenticationProvider implements AuthenticationProvider {

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String rawToken = (String) authentication.getCredentials();

        String username = getUsername(rawToken);

        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);

        return new JwtAuthenticationToken(userDetails);
    }

    private String getUsername(String rawToken) {
        try {
            return jwtService.getUsername(rawToken);
        } catch (JWTVerificationException e) {
            throw new BadCredentialsException("Invalid token");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
