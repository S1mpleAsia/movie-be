package dev.hust.simpleasia.entity.event;

import dev.hust.simpleasia.core.utils.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BaseEvent implements Serializable {
    private String orderId;
    private String status;
    private EventType eventType;
}
