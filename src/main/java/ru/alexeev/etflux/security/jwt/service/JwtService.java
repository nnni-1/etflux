package ru.alexeev.etflux.security.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import ru.alexeev.etflux.dto.JwtTokenPairDto;


import java.util.Date;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class JwtService {

    private final static String AUTH_HEADER = "Authorization";
    private final static String AUTH_HEADER_PREFIX = "Bearer ";

    private static final String USERNAME_CLAIM = "username";
    private static final String TYPE_CLAIM = "type";

    @Value("${jwt.access-token.ttl}")
    private Long accessTokenTtl;

    private final Algorithm algorithm;
    private final JWTVerifier jwtVerifier;

    public JwtTokenPairDto getTokenPair(String username) {
        return new JwtTokenPairDto(
                createAccessToken(username),
                createRefreshToken(username));
    }

    private String createAccessToken(String username) {
        return JWT.create()
                .withExpiresAt(new Date(
                        new Date().getTime() + accessTokenTtl))
                .withClaim(USERNAME_CLAIM, username)
                .withClaim(TYPE_CLAIM, "access")
                .sign(algorithm);
    }

    private String createRefreshToken(String username) {
        return JWT.create()
                .withExpiresAt(new Date(
                                new Date().getTime() + 1000 * 60 * 60 * 24 * 10)) // TODO
                .withClaim(USERNAME_CLAIM, username)
                .withClaim(TYPE_CLAIM, "refresh")
                .sign(algorithm); // TODO
    }

    public String getRawToken(HttpServletRequest request) {
        String headerJson = request.getHeader(AUTH_HEADER);

        if (headerJson != null && headerJson.startsWith(AUTH_HEADER_PREFIX)) {
            return headerJson.substring(AUTH_HEADER_PREFIX.length());
        }

        throw new BadCredentialsException("Token not found");
    }

    public String getUsername(String token) {
        return Optional.of(jwtVerifier.verify(token))
                .map(jwt -> jwt.getClaim(USERNAME_CLAIM))
                .map(Claim::asString)
                .orElse(null);
    }

}
