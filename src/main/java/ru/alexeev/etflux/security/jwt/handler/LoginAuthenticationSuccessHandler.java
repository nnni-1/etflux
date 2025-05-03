package ru.alexeev.etflux.security.jwt.handler;

import com.nimbusds.common.contenttype.ContentType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ru.alexeev.etflux.dto.JwtTokenPairDto;
import ru.alexeev.etflux.security.jwt.service.JwtService;
import ru.alexeev.etflux.util.JsonUtil;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        JwtTokenPairDto tokenPair = jwtService.getTokenPair(username);
        System.out.println(JsonUtil.write(tokenPair));
        response.setStatus(200);
        response.setContentType(ContentType.APPLICATION_JSON.getType());
        response.addHeader("Authorization", "Bearer " + JsonUtil.write(tokenPair));
    }

}
