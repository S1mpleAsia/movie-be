package dev.hust.simpleasia.service;

import dev.hust.simpleasia.entity.domain.EventMessaging;
import dev.hust.simpleasia.repository.EventMessagingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventMessagingService {
    private final EventMessagingRepository repository;

    public void save(EventMessaging eventMessaging) {
        repository.save(eventMessaging);
    }
}
