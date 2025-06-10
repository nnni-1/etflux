package ru.alexeev.etflux.exception.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ErrorDto{
    private LocalDateTime time;
    private int status;
    private String error;
    private String message;
    private String path;
}
