package ru.alexeev.etflux.web.interceptor;

import lombok.RequiredArgsConstructor;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
public class OkHttpHeaderInterceptor implements Interceptor {

    private final String headerName;
    private final String headerValue;

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request requestWithHeader = originalRequest
                .newBuilder()
                .header(headerName, headerValue)
                .build();
        try (Response response = chain.proceed(requestWithHeader)) {
            return response;
        }
    }
}
