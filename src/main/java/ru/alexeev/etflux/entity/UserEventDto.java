package ru.alexeev.etflux.entity;

import lombok.*;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Accessors(chain = true)
public class UserEventDto implements Event {
    private Long id;
    private String username;
    private String description;
    private String event;
}
