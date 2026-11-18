package ru.alexeev.etflux;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.alexeev.etflux.web.interceptor.OkHttpHeaderInterceptor;

import java.io.IOException;

@Configuration
public class ApplicationConfiguration {

    @Value("${api.tbank.token}")
    private String token;

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient().newBuilder()
                .addInterceptor(new OkHttpHeaderInterceptor("Authorization", token))
                .build();
    }

}
