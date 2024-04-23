package dev.hust.simpleasia.service;

import dev.hust.simpleasia.core.utils.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventConnector {
    private final List<EventHandler> eventHandlerList;
    private Map<EventType, EventHandler> eventHandlerMap = new HashMap<>();

    @PostConstruct
    public void init() {
        eventHandlerMap = eventHandlerList.stream().collect(Collectors.toMap(EventHandler::eventType, Function.identity()));
    }

    public EventHandler get(EventType eventType) {
        return eventHandlerMap.get(eventType);
    }
}
