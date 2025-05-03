package ru.alexeev.etflux.security.jwt;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import ru.alexeev.etflux.security.details.UserDetailsImpl;


public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private String token;
    private UserDetailsImpl userDetails;

    public JwtAuthenticationToken(String token) {
        super(null);
        this.token = token;
    }

    public JwtAuthenticationToken(UserDetailsImpl userDetails) {
        super(null);
        this.userDetails = userDetails;
        setAuthenticated(true);
    }

    public String getToken(){
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return userDetails;
    }

}
