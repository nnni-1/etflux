package ru.alexeev.etflux.entity;

import lombok.*;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Accessors(chain = true)
public class EventDto implements Event {
    private String event;
    private String type;
    private String payload;
}
